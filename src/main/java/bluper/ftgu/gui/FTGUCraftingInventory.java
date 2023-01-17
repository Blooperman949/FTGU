package bluper.ftgu.gui;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;

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
