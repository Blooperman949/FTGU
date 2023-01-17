package bluper.ftgu.gui;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;

/**
 * An extension of {@link CraftingInventory} that holds a {@code Block} crafting surface for FTGU's Surface Crafting.
 * @author Bluperman949
 */
public class FTGUCraftingInventory extends CraftingInventory {
	private final Block surface;

	public FTGUCraftingInventory(Container container, Block surface) {
		super(container, 3, 3);
		this.surface = surface;
	}

	public Block getSurface() {
		return surface;
	}
}
