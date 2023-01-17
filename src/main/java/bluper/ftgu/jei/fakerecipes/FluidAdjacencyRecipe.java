package bluper.ftgu.jei.fakerecipes;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * A simple class that holds the data for a FTGU-JEI Fluid Adjacency fake recipe, such as concrete powder and water. Not data-driven, purely informational.
 * @author Bluperman949
 */
public class FluidAdjacencyRecipe {
	public ItemStack blockIn;
	public FluidStack fluidIn;
	public ItemStack blockOut;

	public FluidAdjacencyRecipe(Block blockIn, Fluid fluidIn, Block blockOut) {
		this.blockIn = new ItemStack(blockIn);
		this.fluidIn = new FluidStack(fluidIn, 1000);
		this.blockOut = new ItemStack(blockOut);
	}
}
