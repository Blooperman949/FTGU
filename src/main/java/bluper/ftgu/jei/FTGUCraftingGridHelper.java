package bluper.ftgu.jei;

import java.util.List;

import bluper.ftgu.data.recipes.ShapedSurfaceRecipe;
import bluper.ftgu.data.recipes.ShapelessSurfaceRecipe;
import bluper.ftgu.gui.FTGUCraftingContainer;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.gui.CraftingGridHelper;

/**
 * The JEI {@code CraftingGridHelper} for FTGU Surface Crafting recipes.
 * @see ShapedSurfaceRecipe
 * @see ShapelessSurfaceRecipe
 * @see FTGUCraftingContainer
 * @author Bluperman949
 */
public class FTGUCraftingGridHelper extends CraftingGridHelper {
	public FTGUCraftingGridHelper() {
		super(1);
	}

	@Override
	public <T> void setInputs(IGuiIngredientGroup<T> ingredientGroup, List<List<T>> inputs) {
		int width, height;
		if (inputs.size() > 4) {
			width = height = 3;
		} else if (inputs.size() > 1) {
			width = height = 2;
		} else {
			width = height = 1;
		}

		setInputs(ingredientGroup, inputs, width, height);
	}

	@Override
	public <T> void setInputs(IGuiIngredientGroup<T> ingredientGroup, List<List<T>> inputs, int width, int height) {
		for (int i = 0; i < inputs.size() - 1; i++) {
			List<T> recipeItem = inputs.get(i);
			int index = getCraftingIndex(i, width, height);

			ingredientGroup.set(1 + index, recipeItem);
		}
	}

	private int getCraftingIndex(int i, int width, int height) {
		int index;
		if (width == 1) {
			if (height == 3) {
				index = (i * 3) + 1;
			} else if (height == 2) {
				index = (i * 3) + 1;
			} else {
				index = 4;
			}
		} else if (height == 1) {
			index = i + 3;
		} else if (width == 2) {
			index = i;
			if (i > 1) {
				index++;
				if (i > 3) {
					index++;
				}
			}
		} else if (height == 2) {
			index = i + 3;
		} else {
			index = i;
		}
		return index;
	}
}
