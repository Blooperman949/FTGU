package bluper.ftgu.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableSet;

import bluper.ftgu.FTGU;
import bluper.ftgu.jei.fakerecipes.FluidAdjacencyRecipe;
import bluper.ftgu.jei.fakerecipes.FluidAdjacencyRecipeCategory;
import bluper.ftgu.registry.registries.FTGUBlocks;
import bluper.ftgu.registry.registries.FTGUItems;
import bluper.ftgu.registry.registries.FTGURecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

/**
 * FTGU's {@code JEIPlugin}.
 * @author Bluperman949
 */
@JeiPlugin
public class JEIPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(FTGU.MODID, "jei_plugin");
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(FTGUItems.UNLIT_CAMPFIRE.get().getDefaultInstance(), VanillaRecipeCategoryUid.CAMPFIRE);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(new SurfaceCraftingRecipeCategory(guiHelper));
		registration.addRecipeCategories(new FluidAdjacencyRecipeCategory(guiHelper));
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Minecraft mc = Minecraft.getInstance();
		RecipeManager rm = Objects.requireNonNull(mc.level).getRecipeManager();
		registration.addRecipes(rm.getAllRecipesFor(FTGURecipeTypes.FTGU_CRAFTING), SurfaceCraftingRecipeCategory.UID);
		List<FluidAdjacencyRecipe> FARs = new ArrayList<FluidAdjacencyRecipe>();
		FARs.add(new FluidAdjacencyRecipe(FTGUBlocks.DIRT.get(), Fluids.WATER, FTGUBlocks.DIRT.get().getWet().getBlock()));
		FARs.add(new FluidAdjacencyRecipe(Blocks.WHITE_CONCRETE_POWDER, Fluids.WATER, Blocks.WHITE_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.LIGHT_GRAY_CONCRETE_POWDER, Fluids.WATER, Blocks.LIGHT_GRAY_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.GRAY_CONCRETE_POWDER, Fluids.WATER, Blocks.GRAY_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.BLACK_CONCRETE_POWDER, Fluids.WATER, Blocks.BLACK_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.BROWN_CONCRETE_POWDER, Fluids.WATER, Blocks.BROWN_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.RED_CONCRETE_POWDER, Fluids.WATER, Blocks.RED_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.ORANGE_CONCRETE_POWDER, Fluids.WATER, Blocks.ORANGE_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.YELLOW_CONCRETE_POWDER, Fluids.WATER, Blocks.YELLOW_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.LIME_CONCRETE_POWDER, Fluids.WATER, Blocks.LIME_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.GREEN_CONCRETE_POWDER, Fluids.WATER, Blocks.GREEN_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.CYAN_CONCRETE_POWDER, Fluids.WATER, Blocks.CYAN_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.LIGHT_BLUE_CONCRETE_POWDER, Fluids.WATER, Blocks.LIGHT_BLUE_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.BLUE_CONCRETE_POWDER, Fluids.WATER, Blocks.BLUE_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.PURPLE_CONCRETE_POWDER, Fluids.WATER, Blocks.PURPLE_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.MAGENTA_CONCRETE_POWDER, Fluids.WATER, Blocks.MAGENTA_CONCRETE));
		FARs.add(new FluidAdjacencyRecipe(Blocks.PINK_CONCRETE_POWDER, Fluids.WATER, Blocks.PINK_CONCRETE));
		registration.addRecipes(FARs, FluidAdjacencyRecipeCategory.UID);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		jeiRuntime.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM, ImmutableSet.of(FTGUItems.UNLIT_CAMPFIRE.get().getDefaultInstance()));
		jeiRuntime.getRecipeManager().hideRecipeCategory(new ResourceLocation("smelting"));
		jeiRuntime.getRecipeManager().hideRecipeCategory(new ResourceLocation("smoking"));
		jeiRuntime.getRecipeManager().hideRecipeCategory(new ResourceLocation("blasting"));
		jeiRuntime.getRecipeManager().hideRecipeCategory(new ResourceLocation("brewing"));
		jeiRuntime.getRecipeManager().hideRecipeCategory(new ResourceLocation("anvil"));
		jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM, ImmutableSet.of(Items.ELYTRA.getDefaultInstance(), Items.NETHERITE_INGOT.getDefaultInstance(), Items.NETHERITE_SCRAP.getDefaultInstance(), Items.GLOWSTONE_DUST.getDefaultInstance(), Items.ENDER_PEARL.getDefaultInstance(), Items.BLAZE_ROD.getDefaultInstance(), Items.ENDER_EYE.getDefaultInstance(), Items.EXPERIENCE_BOTTLE.getDefaultInstance(), Items.NETHER_STAR.getDefaultInstance(), Items.NETHER_BRICK.getDefaultInstance(), Items.PRISMARINE_SHARD.getDefaultInstance(), Items.PRISMARINE_CRYSTALS.getDefaultInstance(), Items.IRON_HORSE_ARMOR.getDefaultInstance(), Items.GOLDEN_HORSE_ARMOR.getDefaultInstance(), Items.DIAMOND_HORSE_ARMOR.getDefaultInstance(), Items.LEATHER_HORSE_ARMOR.getDefaultInstance(), Items.CHORUS_FRUIT.getDefaultInstance(), Items.POPPED_CHORUS_FRUIT.getDefaultInstance(), Items.SHULKER_SHELL.getDefaultInstance(), Items.HEART_OF_THE_SEA.getDefaultInstance(), Items.GOLDEN_APPLE.getDefaultInstance(), Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(), Items.ROTTEN_FLESH.getDefaultInstance(), Items.WOODEN_SWORD.getDefaultInstance(), Items.WOODEN_SHOVEL.getDefaultInstance(), Items.WOODEN_PICKAXE.getDefaultInstance(), Items.WOODEN_AXE.getDefaultInstance(), Items.WOODEN_HOE.getDefaultInstance(), Items.STONE_SWORD.getDefaultInstance(), Items.STONE_SHOVEL.getDefaultInstance(), Items.STONE_PICKAXE.getDefaultInstance(), Items.STONE_AXE.getDefaultInstance(), Items.STONE_HOE.getDefaultInstance(), Items.GOLDEN_SWORD.getDefaultInstance(), Items.GOLDEN_SHOVEL.getDefaultInstance(), Items.GOLDEN_PICKAXE.getDefaultInstance(), Items.GOLDEN_AXE.getDefaultInstance(), Items.GOLDEN_HOE.getDefaultInstance(), Items.IRON_SWORD.getDefaultInstance(), Items.IRON_SHOVEL.getDefaultInstance(), Items.IRON_PICKAXE.getDefaultInstance(), Items.IRON_AXE.getDefaultInstance(), Items.IRON_HOE.getDefaultInstance(), Items.DIAMOND_SWORD.getDefaultInstance(), Items.DIAMOND_SHOVEL.getDefaultInstance(), Items.DIAMOND_PICKAXE.getDefaultInstance(), Items.DIAMOND_AXE.getDefaultInstance(), Items.DIAMOND_HOE.getDefaultInstance(), Items.NETHERITE_SWORD.getDefaultInstance(), Items.NETHERITE_SHOVEL.getDefaultInstance(), Items.NETHERITE_PICKAXE.getDefaultInstance(), Items.NETHERITE_AXE.getDefaultInstance(), Items.NETHERITE_HOE.getDefaultInstance(), Items.LEATHER_HELMET.getDefaultInstance(), Items.LEATHER_CHESTPLATE.getDefaultInstance(), Items.LEATHER_LEGGINGS.getDefaultInstance(), Items.LEATHER_BOOTS.getDefaultInstance(), Items.CHAINMAIL_HELMET.getDefaultInstance(), Items.CHAINMAIL_CHESTPLATE.getDefaultInstance(), Items.CHAINMAIL_LEGGINGS.getDefaultInstance(), Items.CHAINMAIL_BOOTS.getDefaultInstance(), Items.GOLDEN_HELMET.getDefaultInstance(), Items.GOLDEN_CHESTPLATE.getDefaultInstance(), Items.GOLDEN_LEGGINGS.getDefaultInstance(), Items.GOLDEN_BOOTS.getDefaultInstance(), Items.IRON_HELMET.getDefaultInstance(), Items.IRON_CHESTPLATE.getDefaultInstance(), Items.IRON_LEGGINGS.getDefaultInstance(), Items.IRON_BOOTS.getDefaultInstance(), Items.DIAMOND_HELMET.getDefaultInstance(), Items.DIAMOND_CHESTPLATE.getDefaultInstance(), Items.DIAMOND_LEGGINGS.getDefaultInstance(), Items.DIAMOND_BOOTS.getDefaultInstance(), Items.NETHERITE_HELMET.getDefaultInstance(), Items.NETHERITE_CHESTPLATE.getDefaultInstance(), Items.NETHERITE_LEGGINGS.getDefaultInstance(), Items.NETHERITE_BOOTS.getDefaultInstance(), Items.TRIDENT.getDefaultInstance(), Items.WARPED_FUNGUS_ON_A_STICK.getDefaultInstance(), Items.BOW.getDefaultInstance(), Items.TURTLE_HELMET.getDefaultInstance(), Items.TOTEM_OF_UNDYING.getDefaultInstance(), Items.SHIELD.getDefaultInstance(), Items.GHAST_TEAR.getDefaultInstance(), Items.FERMENTED_SPIDER_EYE.getDefaultInstance(), Items.BLAZE_POWDER.getDefaultInstance(), Items.MAGMA_CREAM.getDefaultInstance(), Items.GLISTERING_MELON_SLICE.getDefaultInstance(), Items.GOLDEN_CARROT.getDefaultInstance(), Items.RABBIT_FOOT.getDefaultInstance(), Items.DRAGON_BREATH.getDefaultInstance(), Items.SPECTRAL_ARROW.getDefaultInstance(), Items.PHANTOM_MEMBRANE.getDefaultInstance(), Items.END_CRYSTAL.getDefaultInstance(), Items.CRIMSON_NYLIUM.getDefaultInstance(), Items.WARPED_NYLIUM.getDefaultInstance(), Items.CRIMSON_PLANKS.getDefaultInstance(), Items.WARPED_PLANKS.getDefaultInstance(), Items.GOLD_ORE.getDefaultInstance(), Items.IRON_ORE.getDefaultInstance(), Items.COAL_ORE.getDefaultInstance(), Items.NETHER_GOLD_ORE.getDefaultInstance(), Items.CRIMSON_STEM.getDefaultInstance(), Items.WARPED_STEM.getDefaultInstance(), Items.STRIPPED_CRIMSON_STEM.getDefaultInstance(), Items.STRIPPED_WARPED_STEM.getDefaultInstance(), Items.CRIMSON_HYPHAE.getDefaultInstance(), Items.WARPED_HYPHAE.getDefaultInstance(), Items.STRIPPED_CRIMSON_HYPHAE.getDefaultInstance(), Items.STRIPPED_WARPED_HYPHAE.getDefaultInstance(), Items.SPONGE.getDefaultInstance(), Items.WET_SPONGE.getDefaultInstance(), Items.LAPIS_ORE.getDefaultInstance(), Items.CRIMSON_SLAB.getDefaultInstance(), Items.WARPED_SLAB.getDefaultInstance(), Items.NETHER_BRICK_SLAB.getDefaultInstance(), Items.PURPUR_SLAB.getDefaultInstance(), Items.PRISMARINE_SLAB.getDefaultInstance(), Items.PRISMARINE_BRICK_SLAB.getDefaultInstance(), Items.DARK_PRISMARINE_SLAB.getDefaultInstance(), Items.PURPUR_BLOCK.getDefaultInstance(), Items.PURPUR_PILLAR.getDefaultInstance(), Items.PURPUR_STAIRS.getDefaultInstance(), Items.DIAMOND_ORE.getDefaultInstance(), Items.REDSTONE_ORE.getDefaultInstance(), Items.NETHERRACK.getDefaultInstance(), Items.SOUL_SAND.getDefaultInstance(), Items.SOUL_SOIL.getDefaultInstance(), Items.GLOWSTONE.getDefaultInstance(), Items.NETHER_BRICKS.getDefaultInstance(), Items.CRACKED_NETHER_BRICKS.getDefaultInstance(), Items.CHISELED_NETHER_BRICKS.getDefaultInstance(), Items.NETHER_BRICK_STAIRS.getDefaultInstance(), Items.END_STONE.getDefaultInstance(), Items.END_STONE_BRICKS.getDefaultInstance(), Items.EMERALD_ORE.getDefaultInstance(), Items.CRIMSON_STAIRS.getDefaultInstance(), Items.WARPED_STAIRS.getDefaultInstance(), Items.NETHER_QUARTZ_ORE.getDefaultInstance(), Items.PRISMARINE.getDefaultInstance(), Items.PRISMARINE_BRICKS.getDefaultInstance(), Items.DARK_PRISMARINE.getDefaultInstance(), Items.PRISMARINE_STAIRS.getDefaultInstance(), Items.PRISMARINE_BRICK_STAIRS.getDefaultInstance(), Items.DARK_PRISMARINE_STAIRS.getDefaultInstance(), Items.SEA_LANTERN.getDefaultInstance(), Items.NETHER_WART_BLOCK.getDefaultInstance(), Items.WARPED_WART_BLOCK.getDefaultInstance(), Items.RED_NETHER_BRICKS.getDefaultInstance(), Items.RED_NETHER_BRICK_STAIRS.getDefaultInstance(), Items.RED_NETHER_BRICK_SLAB.getDefaultInstance(), Items.NETHERITE_BLOCK.getDefaultInstance(), Items.ANCIENT_DEBRIS.getDefaultInstance(), Items.CRYING_OBSIDIAN.getDefaultInstance(), Items.GILDED_BLACKSTONE.getDefaultInstance(), Items.WITHER_ROSE.getDefaultInstance(), Items.CRIMSON_FUNGUS.getDefaultInstance(), Items.WARPED_FUNGUS.getDefaultInstance(), Items.CRIMSON_ROOTS.getDefaultInstance(), Items.WARPED_ROOTS.getDefaultInstance(), Items.NETHER_SPROUTS.getDefaultInstance(), Items.WEEPING_VINES.getDefaultInstance(), Items.TWISTING_VINES.getDefaultInstance(), Items.END_ROD.getDefaultInstance(), Items.CHORUS_PLANT.getDefaultInstance(), Items.CHORUS_FLOWER.getDefaultInstance(), Items.CHEST.getDefaultInstance(), Items.FURNACE.getDefaultInstance(), Items.CRIMSON_FENCE.getDefaultInstance(), Items.WARPED_FENCE.getDefaultInstance(), Items.SOUL_TORCH.getDefaultInstance(), Items.BROWN_MUSHROOM_BLOCK.getDefaultInstance(), Items.RED_MUSHROOM_BLOCK.getDefaultInstance(), Items.MUSHROOM_STEM.getDefaultInstance(), Items.NETHER_BRICK_FENCE.getDefaultInstance(), Items.ENCHANTING_TABLE.getDefaultInstance(), Items.END_PORTAL_FRAME.getDefaultInstance(), Items.ENDER_CHEST.getDefaultInstance(), Items.PRISMARINE_WALL.getDefaultInstance(), Items.NETHER_BRICK_WALL.getDefaultInstance(), Items.RED_NETHER_BRICK_WALL.getDefaultInstance(), Items.END_STONE_BRICK_WALL.getDefaultInstance(), Items.SHULKER_BOX.getDefaultInstance(), Items.WHITE_SHULKER_BOX.getDefaultInstance(), Items.ORANGE_SHULKER_BOX.getDefaultInstance(), Items.MAGENTA_SHULKER_BOX.getDefaultInstance(), Items.LIGHT_BLUE_SHULKER_BOX.getDefaultInstance(), Items.YELLOW_SHULKER_BOX.getDefaultInstance(), Items.LIME_SHULKER_BOX.getDefaultInstance(), Items.PINK_SHULKER_BOX.getDefaultInstance(), Items.GRAY_SHULKER_BOX.getDefaultInstance(), Items.LIGHT_GRAY_SHULKER_BOX.getDefaultInstance(), Items.CYAN_SHULKER_BOX.getDefaultInstance(), Items.PURPLE_SHULKER_BOX.getDefaultInstance(), Items.BLUE_SHULKER_BOX.getDefaultInstance(), Items.BROWN_SHULKER_BOX.getDefaultInstance(), Items.GREEN_SHULKER_BOX.getDefaultInstance(), Items.RED_SHULKER_BOX.getDefaultInstance(), Items.BLACK_SHULKER_BOX.getDefaultInstance(), Items.CRIMSON_SIGN.getDefaultInstance(), Items.WARPED_SIGN.getDefaultInstance(), Items.WITHER_SKELETON_SKULL.getDefaultInstance(), Items.ZOMBIE_HEAD.getDefaultInstance(), Items.CREEPER_HEAD.getDefaultInstance(), Items.DRAGON_HEAD.getDefaultInstance(), Items.COMPOSTER.getDefaultInstance(), Items.BARREL.getDefaultInstance(), Items.SMOKER.getDefaultInstance(), Items.BLAST_FURNACE.getDefaultInstance(), Items.SOUL_LANTERN.getDefaultInstance(), Items.SOUL_CAMPFIRE.getDefaultInstance(), Items.SHROOMLIGHT.getDefaultInstance(), Items.RESPAWN_ANCHOR.getDefaultInstance(), Items.DISPENSER.getDefaultInstance(), Items.STICKY_PISTON.getDefaultInstance(), Items.PISTON.getDefaultInstance(), Items.LEVER.getDefaultInstance(), Items.STONE_PRESSURE_PLATE.getDefaultInstance(), Items.OAK_PRESSURE_PLATE.getDefaultInstance(), Items.SPRUCE_PRESSURE_PLATE.getDefaultInstance(), Items.BIRCH_PRESSURE_PLATE.getDefaultInstance(), Items.JUNGLE_PRESSURE_PLATE.getDefaultInstance(), Items.ACACIA_PRESSURE_PLATE.getDefaultInstance(), Items.DARK_OAK_PRESSURE_PLATE.getDefaultInstance(), Items.CRIMSON_PRESSURE_PLATE.getDefaultInstance(), Items.WARPED_PRESSURE_PLATE.getDefaultInstance(), Items.POLISHED_BLACKSTONE_PRESSURE_PLATE.getDefaultInstance(), Items.REDSTONE_TORCH.getDefaultInstance(), Items.CRIMSON_TRAPDOOR.getDefaultInstance(), Items.WARPED_TRAPDOOR.getDefaultInstance(), Items.CRIMSON_FENCE_GATE.getDefaultInstance(), Items.WARPED_FENCE_GATE.getDefaultInstance(), Items.REDSTONE_LAMP.getDefaultInstance(), Items.TRIPWIRE_HOOK.getDefaultInstance(), Items.STONE_BUTTON.getDefaultInstance(), Items.OAK_BUTTON.getDefaultInstance(), Items.SPRUCE_BUTTON.getDefaultInstance(), Items.BIRCH_BUTTON.getDefaultInstance(), Items.JUNGLE_BUTTON.getDefaultInstance(), Items.ACACIA_BUTTON.getDefaultInstance(), Items.DARK_OAK_BUTTON.getDefaultInstance(), Items.CRIMSON_BUTTON.getDefaultInstance(), Items.WARPED_BUTTON.getDefaultInstance(), Items.POLISHED_BLACKSTONE_BUTTON.getDefaultInstance(), Items.TRAPPED_CHEST.getDefaultInstance(), Items.LIGHT_WEIGHTED_PRESSURE_PLATE.getDefaultInstance(), Items.HEAVY_WEIGHTED_PRESSURE_PLATE.getDefaultInstance(), Items.DAYLIGHT_DETECTOR.getDefaultInstance(), Items.REDSTONE_BLOCK.getDefaultInstance(), Items.HOPPER.getDefaultInstance(), Items.DROPPER.getDefaultInstance(), Items.IRON_TRAPDOOR.getDefaultInstance(), Items.OBSERVER.getDefaultInstance(), Items.IRON_DOOR.getDefaultInstance(), Items.CRIMSON_DOOR.getDefaultInstance(), Items.WARPED_DOOR.getDefaultInstance(), Items.REPEATER.getDefaultInstance(), Items.COMPARATOR.getDefaultInstance(), Items.REDSTONE.getDefaultInstance(), Items.TARGET.getDefaultInstance(), Items.POWERED_RAIL.getDefaultInstance(), Items.DETECTOR_RAIL.getDefaultInstance(), Items.ACTIVATOR_RAIL.getDefaultInstance(), Items.BEACON.getDefaultInstance(), Items.CONDUIT.getDefaultInstance(), Items.BREWING_STAND.getDefaultInstance(), Items.BLAZE_SPAWN_EGG.getDefaultInstance(), Items.CAVE_SPIDER_SPAWN_EGG.getDefaultInstance(), Items.CREEPER_SPAWN_EGG.getDefaultInstance(), Items.DROWNED_SPAWN_EGG.getDefaultInstance(), Items.ELDER_GUARDIAN_SPAWN_EGG.getDefaultInstance(), Items.ENDERMAN_SPAWN_EGG.getDefaultInstance(), Items.ENDERMITE_SPAWN_EGG.getDefaultInstance(), Items.EVOKER_SPAWN_EGG.getDefaultInstance(), Items.GHAST_SPAWN_EGG.getDefaultInstance(), Items.GUARDIAN_SPAWN_EGG.getDefaultInstance(), Items.HOGLIN_SPAWN_EGG.getDefaultInstance(), Items.HUSK_SPAWN_EGG.getDefaultInstance(), Items.MAGMA_CUBE_SPAWN_EGG.getDefaultInstance(), Items.MOOSHROOM_SPAWN_EGG.getDefaultInstance(), Items.PHANTOM_SPAWN_EGG.getDefaultInstance(), Items.PIGLIN_SPAWN_EGG.getDefaultInstance(), Items.PIGLIN_BRUTE_SPAWN_EGG.getDefaultInstance(), Items.PILLAGER_SPAWN_EGG.getDefaultInstance(), Items.RAVAGER_SPAWN_EGG.getDefaultInstance(), Items.SHULKER_SPAWN_EGG.getDefaultInstance(), Items.SILVERFISH_SPAWN_EGG.getDefaultInstance(), Items.SKELETON_SPAWN_EGG.getDefaultInstance(), Items.SKELETON_HORSE_SPAWN_EGG.getDefaultInstance(), Items.SLIME_SPAWN_EGG.getDefaultInstance(), Items.SPIDER_SPAWN_EGG.getDefaultInstance(), Items.STRAY_SPAWN_EGG.getDefaultInstance(), Items.STRIDER_SPAWN_EGG.getDefaultInstance(), Items.TRADER_LLAMA_SPAWN_EGG.getDefaultInstance(), Items.VEX_SPAWN_EGG.getDefaultInstance(), Items.VILLAGER_SPAWN_EGG.getDefaultInstance(), Items.VINDICATOR_SPAWN_EGG.getDefaultInstance(), Items.WANDERING_TRADER_SPAWN_EGG.getDefaultInstance(), Items.WITCH_SPAWN_EGG.getDefaultInstance(), Items.WITHER_SKELETON_SPAWN_EGG.getDefaultInstance(), Items.ZOMBIE_SPAWN_EGG.getDefaultInstance(), Items.ZOMBIE_HORSE_SPAWN_EGG.getDefaultInstance(), Items.ZOMBIE_VILLAGER_SPAWN_EGG.getDefaultInstance(), Items.ZOMBIFIED_PIGLIN_SPAWN_EGG.getDefaultInstance(), Items.INFESTED_CHISELED_STONE_BRICKS.getDefaultInstance(), Items.INFESTED_COBBLESTONE.getDefaultInstance(), Items.INFESTED_CRACKED_STONE_BRICKS.getDefaultInstance(), Items.INFESTED_MOSSY_STONE_BRICKS.getDefaultInstance(), Items.INFESTED_STONE.getDefaultInstance(), Items.INFESTED_STONE_BRICKS.getDefaultInstance(), Items.BASALT.getDefaultInstance(), Items.POLISHED_BASALT.getDefaultInstance(), Items.SPIDER_EYE.getDefaultInstance(), Items.CROSSBOW.getDefaultInstance(), Items.BOW.getDefaultInstance(), Items.STONECUTTER.getDefaultInstance()));
	}
}
