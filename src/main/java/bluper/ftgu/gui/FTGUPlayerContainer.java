package bluper.ftgu.gui;

import com.mojang.datafixers.util.Pair;

import bluper.ftgu.FTGU;
import bluper.ftgu.registry.registries.FTGUContainerTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * FTGU's replacement for {@link PlayerContainer}. The 2x2 crafting grid has been removed.<br>
 * More features will come in upcoming versions.
 * @author Bluperman949
 */
public class FTGUPlayerContainer extends Container {
	public static final ResourceLocation EMPTY_SLOT_HAND_LEFT = new ResourceLocation(FTGU.MODID, "item/empty_slot_hand_left");
	public static final ResourceLocation EMPTY_SLOT_HAND_RIGHT = new ResourceLocation(FTGU.MODID, "item/empty_slot_hand_right");
	public static final ResourceLocation EMPTY_SLOT_EYES = new ResourceLocation(FTGU.MODID, "item/empty_slot_eyes");
	public static final ResourceLocation EMPTY_SLOT_NECK = new ResourceLocation(FTGU.MODID, "item/empty_slot_neck");
	public static final ResourceLocation EMPTY_SLOT_WAIST = new ResourceLocation(FTGU.MODID, "item/empty_slot_waist");
	private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[] { PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS, PlayerContainer.EMPTY_ARMOR_SLOT_LEGGINGS, PlayerContainer.EMPTY_ARMOR_SLOT_CHESTPLATE, PlayerContainer.EMPTY_ARMOR_SLOT_HELMET };
	private static final EquipmentSlotType[] SLOT_IDS = new EquipmentSlotType[] { EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET };
	public final boolean active;
	private final PlayerEntity owner;

	public FTGUPlayerContainer(PlayerInventory inventory, boolean active, PlayerEntity player) {
		super((ContainerType<?>) null, 0);
		this.active = active;
		this.owner = player;
		IInventory fakeInventory = new Inventory(5);
		for (int i = 0; i < 5; i++) {
			this.addSlot(new Slot(fakeInventory, i, -500, -500) {
				@Override
				public boolean mayPlace(ItemStack p_75214_1_) {
					return false;
				}
			});
		}
		for (int i = 0; i < 4; ++i) {
			final EquipmentSlotType type = SLOT_IDS[i];
			this.addSlot(new EquipmentSlot(inventory, 39 - i, 8, 8 + i * 18, type));
		}
		for (int j = 0; j < 3; ++j)
			for (int i = 0; i < 9; ++i)
				this.addSlot(new Slot(inventory, i + (j + 1) * 9, 8 + i * 18, 84 + j * 18));
		for (int i = 0; i < 9; ++i)
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
		this.addSlot(new Slot(inventory, 40, 77, 62) {
			@OnlyIn(Dist.CLIENT)
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS, PlayerContainer.EMPTY_ARMOR_SLOT_SHIELD);
			}
		});
	}

	private class EquipmentSlot extends Slot {
		private EquipmentSlotType type;

		public EquipmentSlot(IInventory inventory, int index, int x, int y, EquipmentSlotType type) {
			super(inventory, index, x, y);
			this.type = type;
		}

		public int getMaxStackSize() {
			return 1;
		}

		public boolean mayPlace(ItemStack p_75214_1_) {
			return p_75214_1_.canEquip(type, owner);
		}

		public boolean mayPickup(PlayerEntity p_82869_1_) {
			ItemStack itemstack = this.getItem();
			return !itemstack.isEmpty() && !p_82869_1_.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(p_82869_1_);
		}

		@OnlyIn(Dist.CLIENT)
		public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
			return Pair.of(PlayerContainer.BLOCK_ATLAS, FTGUPlayerContainer.TEXTURE_EMPTY_SLOTS[type.getIndex()]);
		}
	}

	@Override
	public ContainerType<?> getType() {
		return FTGUContainerTypes.MOD_PLAYER.get();
	}

	@Override
	public boolean stillValid(PlayerEntity p_75145_1_) {
		return true;
	}

	public ItemStack quickMoveStack(PlayerEntity player, int slotIndex) {
		ItemStack itemBefore = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack = slot.getItem();
			itemBefore = itemStack.copy();
			EquipmentSlotType type = MobEntity.getEquipmentSlotForItem(itemBefore);
			if (slotIndex == 0) {
				if (!this.moveItemStackTo(itemStack, 9, 45, true))
					return ItemStack.EMPTY;
				slot.onQuickCraft(itemStack, itemBefore);
			} else if (slotIndex >= 1 && slotIndex < 5) {
				if (!this.moveItemStackTo(itemStack, 9, 45, false))
					return ItemStack.EMPTY;
			} else if (slotIndex >= 5 && slotIndex < 9) {
				if (!this.moveItemStackTo(itemStack, 9, 45, false))
					return ItemStack.EMPTY;
			} else if (type.getType() == EquipmentSlotType.Group.ARMOR && !this.slots.get(8 - type.getIndex()).hasItem()) {
				int i = 8 - type.getIndex();
				if (!this.moveItemStackTo(itemStack, i, i + 1, false))
					return ItemStack.EMPTY;
			} else if (type == EquipmentSlotType.OFFHAND && !this.slots.get(45).hasItem()) {
				if (!this.moveItemStackTo(itemStack, 45, 46, false))
					return ItemStack.EMPTY;
			} else if (slotIndex >= 9 && slotIndex < 36) {
				if (!this.moveItemStackTo(itemStack, 36, 45, false))
					return ItemStack.EMPTY;
			} else if (slotIndex >= 36 && slotIndex < 45) {
				if (!this.moveItemStackTo(itemStack, 9, 36, false))
					return ItemStack.EMPTY;
			} else if (!this.moveItemStackTo(itemStack, 9, 45, false))
				return ItemStack.EMPTY;
			if (itemStack.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();

			if (itemStack.getCount() == itemBefore.getCount())
				return ItemStack.EMPTY;
			ItemStack itemstack2 = slot.onTake(player, itemStack);
			if (slotIndex == 0)
				player.drop(itemstack2, false);
		}
		return itemBefore;
	}
}
