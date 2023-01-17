package bluper.ftgu.data.recipes;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import bluper.ftgu.data.DataUtils;
import bluper.ftgu.gui.FTGUCraftingInventory;
import bluper.ftgu.registry.registries.FTGURecipeTypes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.util.Size2i;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * A shaped recipe that requires a surface {@code Block} for crafting.
 * @author Bluperman949
 */
public class ShapedSurfaceRecipe implements IFTGUCraftingRecipe, IShapedRecipe<FTGUCraftingInventory> {
	static final int MAX_WIDTH = 3;
	static final int MAX_HEIGHT = 3;
	private final int width;
	private final int height;
	private final NonNullList<Ingredient> recipeItems;
	private final ItemStack result;
	private final ResourceLocation id;
	private final String group;
	private final Predicate<Block> surfaceTest;
	private final String surfaceTestLocation;

	ShapedSurfaceRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, Predicate<Block> surfaceTest, String surfaceTestLocation) {
		this.id = id;
		this.group = group;
		this.width = width;
		this.height = height;
		this.recipeItems = ingredients;
		this.result = result;
		this.surfaceTest = surfaceTest;
		this.surfaceTestLocation = surfaceTestLocation;
	}

	@Override
	public String getSurfaceTestLocation() {
		return surfaceTestLocation;
	}

	@Override
	public ItemStack getOutput() {
		return result;
	}

	@Override
	public boolean matches(FTGUCraftingInventory inv, World world) {
		for (int i = 0; i <= inv.getWidth() - this.width; ++i)
			for (int j = 0; j <= inv.getHeight() - this.height; ++j) {
				if (this.matches(inv, i, j, true) || this.matches(inv, i, j, false))
					return surfaceTest.test(inv.getSurface());
			}
		return false;
	}

	private boolean matches(FTGUCraftingInventory inv, int width, int height, boolean flipped) {
		for (int i = 0; i < inv.getWidth(); ++i) {
			for (int j = 0; j < inv.getHeight(); ++j) {
				int k = i - width;
				int l = j - height;
				Ingredient ingredient = Ingredient.EMPTY;
				if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
					if (flipped) {
						ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
					} else {
						ingredient = this.recipeItems.get(k + l * this.width);
					}
				}

				if (!ingredient.test(inv.getItem(i + j * inv.getWidth()))) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public ItemStack assemble(FTGUCraftingInventory inv) {
		return this.getResultItem().copy();
	}

	@Override
	public ItemStack getResultItem() {
		return this.result;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return FTGURecipeTypes.SHAPED_SURFACE_SERIALIZER.get();
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return w >= this.width && h >= this.height;
	}

	@Override
	public int getRecipeWidth() {
		return this.width;
	}

	@Override
	public int getRecipeHeight() {
		return this.height;
	}

	@Override
	public Size2i getSize() {
		return new Size2i(width, height);
	}

	private static NonNullList<Ingredient> dissolvePattern(String[] pattern, Map<String, Ingredient> map, int width, int height) {
		NonNullList<Ingredient> nonnulllist = NonNullList.withSize(width * height, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(map.keySet());
		set.remove(" ");

		for (int i = 0; i < pattern.length; ++i) {
			for (int j = 0; j < pattern[i].length(); ++j) {
				String s = pattern[i].substring(j, j + 1);
				Ingredient ingredient = map.get(s);
				if (ingredient == null) {
					throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
				}

				set.remove(s);
				nonnulllist.set(j + width * i, ingredient);
			}
		}

		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		} else {
			return nonnulllist;
		}
	}

	static String[] shrink(String... strings) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;

		for (int i1 = 0; i1 < strings.length; ++i1) {
			String s = strings[i1];
			i = Math.min(i, firstNonSpace(s));
			int j1 = lastNonSpace(s);
			j = Math.max(j, j1);
			if (j1 < 0) {
				if (k == i1) {
					++k;
				}

				++l;
			} else {
				l = 0;
			}
		}

		if (strings.length == l) {
			return new String[0];
		} else {
			String[] astring = new String[strings.length - l - k];

			for (int k1 = 0; k1 < astring.length; ++k1) {
				astring[k1] = strings[k1 + k].substring(i, j + 1);
			}

			return astring;
		}
	}

	private static int firstNonSpace(String s) {
		int i;
		for (i = 0; i < s.length() && s.charAt(i) == ' '; ++i) {
		}

		return i;
	}

	private static int lastNonSpace(String s) {
		int i;
		for (i = s.length() - 1; i >= 0 && s.charAt(i) == ' '; --i) {
		}

		return i;
	}

	private static String[] patternFromJson(JsonArray jsonArr) {
		String[] astring = new String[jsonArr.size()];
		if (astring.length > MAX_HEIGHT) {
			throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
		} else if (astring.length == 0) {
			throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		} else {
			for (int i = 0; i < astring.length; ++i) {
				String s = JSONUtils.convertToString(jsonArr.get(i), "pattern[" + i + "]");
				if (s.length() > MAX_WIDTH) {
					throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
				}

				if (i > 0 && astring[0].length() != s.length()) {
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				}

				astring[i] = s;
			}

			return astring;
		}
	}

	private static Map<String, Ingredient> keyFromJson(JsonObject json) {
		Map<String, Ingredient> map = Maps.newHashMap();

		for (Entry<String, JsonElement> entry : json.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}

			map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
		}

		map.put(" ", Ingredient.EMPTY);
		return map;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapedSurfaceRecipe> {
		public ShapedSurfaceRecipe fromJson(ResourceLocation rl, JsonObject json) {
			String group = JSONUtils.getAsString(json, "group", "");
			Map<String, Ingredient> map = ShapedSurfaceRecipe.keyFromJson(JSONUtils.getAsJsonObject(json, "key"));
			String[] pattern = ShapedSurfaceRecipe.shrink(ShapedSurfaceRecipe.patternFromJson(JSONUtils.getAsJsonArray(json, "pattern")));
			int i = pattern[0].length();
			int j = pattern.length;
			NonNullList<Ingredient> ingredients = ShapedSurfaceRecipe.dissolvePattern(pattern, map, i, j);
			ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
			String surfaceTestLocation = JSONUtils.getAsString(json, "surface", "");
			Predicate<Block> surfaceTest = DataUtils.createSurfaceTest(surfaceTestLocation);
			return new ShapedSurfaceRecipe(rl, group, i, j, ingredients, result, surfaceTest, surfaceTestLocation);
		}

		public ShapedSurfaceRecipe fromNetwork(ResourceLocation rl, PacketBuffer buf) {
			int i = buf.readVarInt();
			int j = buf.readVarInt();
			String group = buf.readUtf(32767);
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i * j, Ingredient.EMPTY);
			for (int k = 0; k < ingredients.size(); ++k) {
				ingredients.set(k, Ingredient.fromNetwork(buf));
			}
			ItemStack result = buf.readItem();
			String surfaceTestLocation = (String) buf.readCharSequence(0, Charset.defaultCharset());
			Predicate<Block> surfaceTest = DataUtils.createSurfaceTest(surfaceTestLocation);
			return new ShapedSurfaceRecipe(rl, group, i, j, ingredients, result, surfaceTest, surfaceTestLocation);
		}

		public void toNetwork(PacketBuffer buf, ShapedSurfaceRecipe recipe) {
			buf.writeVarInt(recipe.width);
			buf.writeVarInt(recipe.height);
			buf.writeUtf(recipe.group);
			for (Ingredient ingredient : recipe.recipeItems) {
				ingredient.toNetwork(buf);
			}
			buf.writeItem(recipe.result);
			buf.writeCharSequence(recipe.surfaceTestLocation, Charset.defaultCharset());
		}
	}
}