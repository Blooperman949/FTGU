package bluper.ftgu.gui;

import java.util.Optional;

import bluper.ftgu.data.recipes.IFTGUCraftingRecipe;
import bluper.ftgu.registry.registries.FTGUContainerTypes;
import bluper.ftgu.registry.registries.FTGURecipeTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * A container similar to {@link WorkbenchContainer} used for FTGU's
 * crafting.<br>
 * Interprets vanilla recipes and Surface Crafting recipes. Damages tools
 * instead of consuming them.
 * 
 * @author Bluperman949
 */
public class FTGUCraftingContainer extends Container {
	public static final ITextComponent TITLE = new TranslationTextComponent("container.crafting");
	private final FTGUCraftingInventory craftSlots;
	private final CraftResultInventory resultSlots = new CraftResultInventory();
	private final IWorldPosCallable access;
	private final PlayerEntity player;
	private final Block surface;

	public FTGUCraftingContainer(int id, PlayerInventory inventory, IWorldPosCallable access, Block surface) {
		super(FTGUContainerTypes.MOD_CRAFTING.get(), id);
		this.surface = surface;
		this.access = access;
		this.player = inventory.player;
		this.craftSlots = new FTGUCraftingInventory(this, surface);
		this.addSlot(new FTGUCraftingResultSlot(inventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 3; ++j)
				this.addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		for (int i = 0; i < 9; ++i)
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
		this.addSlot(new Slot(new Inventory(1), 0, 93, 35) { // hacky way to display the crafting surface
			@Override
			public boolean mayPickup(PlayerEntity p) {
				return false;
			}

			@Override
			public boolean mayPlace(ItemStack i) {
				return false;
			}
		});
		ItemStack surfaceItem = new ItemStack(surface.asItem());
		surfaceItem.setHoverName((new TranslationTextComponent("gui.ftgu.craftingOn").append(surfaceItem.getHoverName())).setStyle(Style.EMPTY.withItalic(false)));
		this.setItem(46, surfaceItem);
	}

	public Block getSurface() {
		return surface;
	}

	protected static void slotChangedCraftingGrid(int slot, World world, PlayerEntity player, FTGUCraftingInventory inv, CraftResultInventory resultInv) {
		if (!world.isClientSide) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			ItemStack result = ItemStack.EMPTY;
			Optional<IFTGUCraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(FTGURecipeTypes.FTGU_CRAFTING, inv, world);
			if (optional.isPresent()) {
				IFTGUCraftingRecipe iRecipe = optional.get();
				if (resultInv.setRecipeUsed(world, serverPlayer, iRecipe)) {
					result = iRecipe.assemble(inv);
				}
			} else {
				Optional<ICraftingRecipe> vanillaOptional = world.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, inv, world);
				if (vanillaOptional.isPresent()) {
					ICraftingRecipe iRecipe = vanillaOptional.get();
					if (resultInv.setRecipeUsed(world, serverPlayer, iRecipe)) {
						result = iRecipe.assemble(inv);
					}
				}
			}

			resultInv.setItem(0, result);
			serverPlayer.connection.send(new SSetSlotPacket(slot, 0, result));
		}
	}

	public void slotsChanged(IInventory inventory) {
		this.access.execute((world, pos) -> {
			slotChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots);
		});
	}

	@Override
	public boolean stillValid(PlayerEntity p_75145_1_) {
		return true;
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slotIndex) {
		ItemStack itemBefore = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack itemInSlot = slot.getItem();
			itemBefore = itemInSlot.copy();
			if (slotIndex == 0) {
				this.access.execute((world, pos) -> itemInSlot.getItem().onCraftedBy(itemInSlot, world, player));
				if (!this.moveItemStackTo(itemInSlot, 10, 46, true)) return ItemStack.EMPTY;
				slot.onQuickCraft(itemInSlot, itemBefore);
			} else if (slotIndex >= 10 && slotIndex < 46) {
				if (!this.moveItemStackTo(itemInSlot, 1, 10, false)) if (slotIndex < 37) {
					if (!this.moveItemStackTo(itemInSlot, 37, 46, false)) return ItemStack.EMPTY;
				} else if (!this.moveItemStackTo(itemInSlot, 10, 37, false)) return ItemStack.EMPTY;
			} else if (!this.moveItemStackTo(itemInSlot, 10, 46, false)) return ItemStack.EMPTY;
			if (itemInSlot.isEmpty()) slot.set(ItemStack.EMPTY);
			else slot.setChanged();
			if (itemInSlot.getCount() == itemBefore.getCount()) return ItemStack.EMPTY;
			ItemStack itemToDrop = slot.onTake(player, itemInSlot);
			if (slotIndex == 0) player.drop(itemToDrop, false);
		}
		return itemBefore;
	}

	@Override
	public void removed(PlayerEntity player) {
		super.removed(player);
		this.access.execute((world, pos) -> {
			this.clearContainer(player, world, this.craftSlots);
		});
	}
}