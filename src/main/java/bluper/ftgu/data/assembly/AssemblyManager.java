package bluper.ftgu.data.assembly;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import bluper.ftgu.data.DataUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.Property;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyManager extends JsonReloadListener {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final AssemblyManager INSTANCE = new AssemblyManager(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create());
	private Map<Item, Map<ResourceLocation, IAssembly>> assemblies = ImmutableMap.of();

	public AssemblyManager(Gson gson) {
		super(gson, "assemblies");
		LOGGER.debug("AssemblyManager instantiated");
	}

	public static AssemblyManager get() {
		return INSTANCE;
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonMap, IResourceManager rm, IProfiler profiler) {
		Map<Item, Builder<ResourceLocation, IAssembly>> map = Maps.newHashMap();
		for (Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
			ResourceLocation rl = entry.getKey();
			try {
				if (entry.getValue().isJsonObject() && !net.minecraftforge.common.crafting.CraftingHelper.processConditions(entry.getValue().getAsJsonObject(), "conditions")) {
					LOGGER.debug("Skipping loading assembly {} as its conditions were not met", rl);
					continue;
				}
				IAssembly assembly = fromJson(JSONUtils.convertToJsonObject(entry.getValue(), "top element"));
				if (assembly == null) {
					LOGGER.info("Skipping loading assembly {} as its serializer returned null", rl);
					continue;
				}
				map.computeIfAbsent(assembly.getAddition(), (b) -> {
					return ImmutableMap.builder();
				}).put(rl, assembly);
			} catch (IllegalArgumentException | JsonParseException | IllegalStateException | ClassCastException e) {
				LOGGER.error("Parsing error loading assembly {}", rl, e);
			}
			LOGGER.log(Level.INFO, "Loaded assemblies");
		}
		this.assemblies = map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (entry) -> {
			return entry.getValue().build();
		}));
	}

	@Nullable
	private static IAssembly fromJson(JsonObject json) throws IllegalStateException, ClassCastException {
		BlockState base = DataUtils.stateFromString(json.get("base").getAsString());
		Item addition = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("addition").getAsString()));
		BlockState result = DataUtils.stateFromString(json.get("result").getAsString());
		JsonElement popJson = json.get("pop");
		Item pop = popJson == null ? null : ForgeRegistries.ITEMS.getValue(new ResourceLocation(popJson.getAsString()));
		JsonElement soundJson = json.get("sound");
		SoundEvent sound = soundJson == null ? null : ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundJson.getAsString()));
		if (base != null && addition != null && result != null)
			return new Assembly(base, addition, result, pop, sound);
		else
			return null;
	}

	@Nullable
	public IAssembly getAssembly(BlockState state, Item item) {
		IAssembly ret = null;
		Map<ResourceLocation, IAssembly> as = assemblies.get(item);
		if (as != null)
			for (IAssembly a : as.values()) {
				if (!state.is(a.getBase().getBlock()))
					continue;
				boolean statesMatch = true;
				for (Property<?> p : state.getProperties())
					if (state.getValue(p) != a.getBase().getValue(p))
						statesMatch = false;
				if (statesMatch) {
					ret = a;
					break;
				}
			}
		return ret;
	}
}
