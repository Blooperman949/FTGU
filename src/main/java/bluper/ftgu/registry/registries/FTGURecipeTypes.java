package bluper.ftgu.registry.registries;

import bluper.ftgu.FTGU;
import bluper.ftgu.data.recipes.IFTGUCraftingRecipe;
import bluper.ftgu.data.recipes.ShapedSurfaceRecipe;
import bluper.ftgu.data.recipes.ShapelessSurfaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FTGURecipeTypes {
	public static final DeferredRegister<IRecipeSerializer<?>> R = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FTGU.MODID);

	public static final RegistryObject<ShapedSurfaceRecipe.Serializer> SHAPED_SURFACE_SERIALIZER = R.register("crafting_shaped", ShapedSurfaceRecipe.Serializer::new);
	public static final RegistryObject<ShapelessSurfaceRecipe.Serializer> SHAPELESS_SURFACE_SERIALIZER = R.register("crafting_shapeless", ShapelessSurfaceRecipe.Serializer::new);

	public static final ResourceLocation FTGU_CRAFTING_ID = new ResourceLocation(FTGU.MODID, "crafting");
	public static final IRecipeType<IFTGUCraftingRecipe> FTGU_CRAFTING = new IRecipeType<IFTGUCraftingRecipe>() {
		@Override
		public String toString() {
			return FTGU_CRAFTING_ID.toString();
		}
	};
}