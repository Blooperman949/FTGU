package bluper.ftgu.gui;

import java.util.Optional;

import bluper.ftgu.data.DataUtils;
import bluper.ftgu.data.recipes.IFTGUCraftingRecipe;
import bluper.ftgu.registry.registries.FTGURecipeTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

public class FTGUCraftingResultSlot extends Slot {
	private final FTGUCraftingInventory craftSlots;
	private final PlayerEntity player;
	private int removeCount;

	public FTGUCraftingResultSlot(PlayerEntity player, FTGUCraftingInventory inventory, IInventory iInventory, int index, int x, int y) {
		super(iInventory, index, x, y);
		this.player = player;
		this.craftSlots = inventory;
	}

	public boolean mayPlace(ItemStack itemStack) {
		return false;
	}

	public ItemStack remove(int count) {
		if (this.hasItem())
			this.removeCount += Math.min(count, this.getItem().getCount());
		return super.remove(count);
	}

	protected void onQuickCraft(ItemStack item, int count) {
		this.removeCount += count;
		this.checkTakeAchievements(item);
	}

	protected void onSwapCraft(int count) {
		this.removeCount += count;
	}

	protected void checkTakeAchievements(ItemStack item) {
		if (this.removeCount > 0) {
			item.onCraftedBy(this.player.level, this.player, this.removeCount);
			BasicEventHooks.firePlayerCraftingEvent(this.player, item, this.craftSlots);
		}
		if (this.container instanceof IRecipeHolder)
			((IRecipeHolder) this.container).awardUsedRecipes(this.player);
		this.removeCount = 0;
	}

	private static NonNullList<ItemStack> getRemainingItems(FTGUCraftingInventory inv, World level) {
		RecipeManager rm = level.getRecipeManager();
		Optional<IFTGUCraftingRecipe> modOptional = rm.getRecipeFor(FTGURecipeTypes.FTGU_CRAFTING, inv, level);
		Optional<ICraftingRecipe> vanillaOptional = rm.getRecipeFor(IRecipeType.CRAFTING, inv, level);
		if (modOptional.isPresent() || vanillaOptional.isPresent()) {
			return DataUtils.getRemainingItems(inv);
		} else {
			NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

			for (int i = 0; i < remaining.size(); ++i) {
				remaining.set(i, inv.getItem(i));
			}

			return remaining;
		}
	}

	public ItemStack onTake(PlayerEntity player, ItemStack item) {
		this.checkTakeAchievements(item);
		ForgeHooks.setCraftingPlayer(player);
		NonNullList<ItemStack> recipeRemainingItems = getRemainingItems(this.craftSlots, player.level);
		ForgeHooks.setCraftingPlayer(null);
		for (int i = 0; i < recipeRemainingItems.size(); ++i) {
			ItemStack itemFromSlots = this.craftSlots.getItem(i);
			ItemStack remainingItem = recipeRemainingItems.get(i);
			if (!itemFromSlots.isEmpty()) {
				this.craftSlots.removeItem(i, 1);
				itemFromSlots = this.craftSlots.getItem(i);
			}
			if (!remainingItem.isEmpty())
				if (itemFromSlots.isEmpty())
					this.craftSlots.setItem(i, remainingItem);
				else if (ItemStack.isSame(itemFromSlots, remainingItem) && ItemStack.tagMatches(itemFromSlots, remainingItem)) {
					remainingItem.grow(itemFromSlots.getCount());
					this.craftSlots.setItem(i, remainingItem);
				} else if (!this.player.inventory.add(remainingItem))
					this.player.drop(remainingItem, false);
		}

		return item;
	}
}
