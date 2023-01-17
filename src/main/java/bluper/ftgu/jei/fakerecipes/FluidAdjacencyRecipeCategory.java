package bluper.ftgu.jei.fakerecipes;

import bluper.ftgu.FTGU;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class FluidAdjacencyRecipeCategory implements IRecipeCategory<FluidAdjacencyRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(FTGU.MODID, "fluid_adjacency");
	private IDrawable icon;
	private IDrawable background;

	public FluidAdjacencyRecipeCategory(IGuiHelper guiHelper) {
		this.icon = guiHelper.createDrawableIngredient(new FluidStack(Fluids.WATER, 1000));
		this.background = guiHelper.createDrawable(new ResourceLocation(FTGU.MODID, "textures/gui/fluid_adjacency.png"), 0, 0, 42, 52);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends FluidAdjacencyRecipe> getRecipeClass() {
		return FluidAdjacencyRecipe.class;
	}

	@Override
	public String getTitle() {
		return "Fluid Adjacency";
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(FluidAdjacencyRecipe recipe, IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, recipe.blockIn);
		ingredients.setInput(VanillaTypes.FLUID, recipe.fluidIn);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.blockOut);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, FluidAdjacencyRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 0, 0);
		recipeLayout.getFluidStacks().init(1, true, 25, 1);
		guiItemStacks.init(2, false, 12, 34);
		recipeLayout.getItemStacks().set(ingredients);
		recipeLayout.getFluidStacks().set(ingredients);
	}

}
