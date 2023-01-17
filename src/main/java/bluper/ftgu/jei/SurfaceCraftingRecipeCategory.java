package bluper.ftgu.jei;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import bluper.ftgu.FTGU;
import bluper.ftgu.data.BlockItemTag;
import bluper.ftgu.data.recipes.IFTGUCraftingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.common.util.Size2i;
import net.minecraftforge.registries.ForgeRegistries;

public class SurfaceCraftingRecipeCategory implements IRecipeCategory<IFTGUCraftingRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(FTGU.MODID, "crafting");
	private IDrawable icon;
	private IDrawable background;
	private final ICraftingGridHelper craftingGridHelper = new FTGUCraftingGridHelper();

	public SurfaceCraftingRecipeCategory(IGuiHelper guiHelper) {
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(Items.CRAFTING_TABLE));
		this.background = guiHelper.createDrawable(new ResourceLocation(FTGU.MODID, "textures/gui/container/crafting_table.png"), 29, 16, 116, 54);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends IFTGUCraftingRecipe> getRecipeClass() {
		return IFTGUCraftingRecipe.class;
	}

	@Override
	public String getTitle() {
		return "Crafting on Surface";
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
	public void setIngredients(IFTGUCraftingRecipe recipe, IIngredients ingredients) {
		List<Ingredient> inputs = recipe.getIngredients();
		inputs.add(ingredientFromSurfaceTestLocation(recipe.getSurfaceTestLocation()));
		ingredients.setInputIngredients(inputs);
		inputs.remove(inputs.size() - 1);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}

	private Ingredient ingredientFromSurfaceTestLocation(String s) {
		ResourceLocation rl;
		try {
			rl = new ResourceLocation(s);
			Block t = ForgeRegistries.BLOCKS.getValue(rl);
			return Ingredient.of(t);
		} catch (ResourceLocationException e) {
			rl = new ResourceLocation(s.substring(1));
			BlockTags.createOptional(rl);
			return Ingredient.of(new BlockItemTag(BlockTags.createOptional(rl)));
		}
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IFTGUCraftingRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, false, 94, 18);
		for (int y = 0; y < 3; ++y)
			for (int x = 0; x < 3; ++x) {
				int index = 1 + x + (y * 3);
				guiItemStacks.init(index, true, x * 18, y * 18);
			}
		guiItemStacks.init(10, true, 63, 18);
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		Size2i size = recipe.getSize();
		if (size != null && size.width > 0 && size.height > 0)
			craftingGridHelper.setInputs(guiItemStacks, inputs, size.width, size.height);
		else {
			craftingGridHelper.setInputs(guiItemStacks, inputs);
			recipeLayout.setShapeless();
		}
		guiItemStacks.set(0, outputs.get(0));
		guiItemStacks.set(10, inputs.get(inputs.size() - 1));
	}

	@Override
	public void draw(IFTGUCraftingRecipe recipe, MatrixStack ms, double x, double y) {
	}
}
