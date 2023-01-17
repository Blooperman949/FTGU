package bluper.ftgu.data.recipes;

import bluper.ftgu.gui.FTGUCraftingInventory;
import bluper.ftgu.registry.registries.FTGURecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.common.util.Size2i;

/**
 * extension of {@link IRecipe} for the {@link FTGUCraftingInventory}.
 * @author Bluperman949
 */
public interface IFTGUCraftingRecipe extends IRecipe<FTGUCraftingInventory> {
	@Override
	default IRecipeType<?> getType() {
		return FTGURecipeTypes.FTGU_CRAFTING;
	}

	@Override
	default boolean canCraftInDimensions(int x, int y) {
		return x >= 3 && y >= 3;
	}

	String getSurfaceTestLocation();

	default Size2i getSize() {
		return null;
	}

	ItemStack getOutput();
}
