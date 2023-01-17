package bluper.ftgu.registry.registries;

import java.util.function.Supplier;

import bluper.ftgu.FTGU;
import bluper.ftgu.world.item.DamagingThrowableItem;
import bluper.ftgu.world.item.FTGUBowItem;
import bluper.ftgu.world.item.FirestarterItem;
import bluper.ftgu.world.item.HammerItem;
import bluper.ftgu.world.item.UnlitCampfireItem;
import bluper.ftgu.world.item.tier.FTGUItemTier;
import bluper.ftgu.world.item.tier.HandleTier;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FTGUItems {
	public static final DeferredRegister<Item> R = DeferredRegister.create(ForgeRegistries.ITEMS, FTGU.MODID);
	public static final DeferredRegister<Item> OVR = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

	// Tabs
	static ItemGroup createTab(String name, Supplier<ItemStack> icon) {
		return new ItemGroup("ftgu." + name) {
			@Override
			public ItemStack makeIcon() {
				return icon.get();
			}
		};
	}

	// Helper constants
	private static final String[] WOODS = new String[] { "oak", "birch", "spruce", "jungle", "acacia", "dark_oak" };

	// Wood
	static {
		for (String i : WOODS) {
			R.register(i + "_branch", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
		}
	}
	public static final RegistryObject<Item> TWIG = R.register("twig", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

	// Grass & Earth
	public static final RegistryObject<Item> GRASS = R.register("grass", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> GRASS_ROPE = R.register("grass_rope", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> MUD_BALL = R.register("mud_ball", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> COB = R.register("cob", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> DIRT_PILE = R.register("dirt_pile", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> RED_CLAY_BALL = R.register("red_clay_ball", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

	// Stone
	public static final RegistryObject<Item> ROCK = R.register("rock", () -> new DamagingThrowableItem(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> RUBBLE = R.register("rubble", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

	// Bricks
	public static final RegistryObject<Item> WET_BRICK = R.register("wet_brick", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> WET_WHITE_BRICK = R.register("wet_white_brick", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> WHITE_BRICK = R.register("white_brick", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> ADOBE_BRICK = R.register("adobe_brick", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

	// Tools
	public static final RegistryObject<SwordItem> SHARP_FLINT = R.register("sharp_flint", () -> FTGUItemTier.PRIMITIVE.createKnife(HandleTier.NOT_APPLICABLE));
	public static final RegistryObject<HammerItem> LARGE_ROCK = R.register("large_rock", () -> FTGUItemTier.PRIMITIVE.createHammer(HandleTier.NOT_APPLICABLE));
	static {
		HandleTier[] hTiers = HandleTier.values();
		for (int i = 0; i < WOODS.length; i++) {
			String name = WOODS[i];
			int io = i + 1;
			R.register(name + "_flint_hatchet", () -> FTGUItemTier.PRIMITIVE.createHatchet(hTiers[io]));
			R.register(name + "_digging_stick", () -> FTGUItemTier.PRIMITIVE.createShovel(hTiers[io]));
			R.register(name + "_flint_chisel", () -> FTGUItemTier.PRIMITIVE.createChisel(hTiers[io]));
		}
	}
	public static final RegistryObject<UnlitCampfireItem> UNLIT_CAMPFIRE = R.register("unlit_campfire", () -> new UnlitCampfireItem(new Properties().tab(ItemGroup.TAB_TOOLS)));
	public static final RegistryObject<FTGUBowItem> PRIMITIVE_BOW = R.register("primitive_bow", () -> new FTGUBowItem(HandleTier.NOT_APPLICABLE, 32));
	public static final RegistryObject<FirestarterItem> BOW_DRILL = R.register("bow_drill", () -> new FirestarterItem(new Properties().tab(ItemGroup.TAB_TOOLS).durability(64), 0.1f));

	// Deletions
	static final RegistryObject<Item> I_SPLASH_POTION = OVR.register("splash_potion", () -> new Item(new Properties()));
	static final RegistryObject<Item> I_LINGERING_POTION = OVR.register("lingering_potion", () -> new Item(new Properties()));
	static final RegistryObject<Item> I_ENCHANTED_BOOK = OVR.register("enchanted_book", () -> new Item(new Properties()));
}