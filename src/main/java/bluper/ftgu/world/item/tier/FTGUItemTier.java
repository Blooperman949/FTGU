package bluper.ftgu.world.item.tier;

import bluper.ftgu.world.item.ChiselItem;
import bluper.ftgu.world.item.HammerItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;

public enum FTGUItemTier implements IItemTier {
	PRIMITIVE(0, 16, 2.0f, 0.0f, Ingredient.EMPTY);

	final int uses;
	final float speed;
	final float damageBonus;
	final int level;
	final Ingredient repairIngredient;

	FTGUItemTier(int level, int uses, float speed, float damageBonus, Ingredient repairIngredient) {
		this.level = level;
		this.uses = uses;
		this.speed = speed;
		this.damageBonus = damageBonus;
		this.repairIngredient = repairIngredient;
	}

	public SwordItem createSword(HandleTier handle) {
		return new SwordItem(this, 3, -2.4f * handle.getWeight(), new Properties().tab(ItemGroup.TAB_COMBAT).durability((int) (this.getUses() * handle.getDurability())));
	}

	public SwordItem createKnife(HandleTier handle) {
		return new SwordItem(this, 1, -2.0f * handle.getWeight(), new Properties().tab(ItemGroup.TAB_TOOLS).durability((int) (this.getUses() * handle.getDurability())));
	}

	public ShovelItem createShovel(HandleTier handle) {
		return new ShovelItem(this, 1.5f, -3.0f * handle.getWeight(), new Properties().tab(ItemGroup.TAB_TOOLS).durability((int) (this.getUses() * handle.getDurability())));
	}

	public PickaxeItem createPickaxe(HandleTier handle) {
		return new PickaxeItem(this, 1, -2.8f * handle.getWeight(), new Properties().tab(ItemGroup.TAB_TOOLS).durability(2 * (int) (this.getUses() * handle.getDurability())));
	}

	public AxeItem createAxe(HandleTier handle) {
		return new AxeItem(this, 6.0f, -3.2f * handle.getWeight(), new Properties().tab(ItemGroup.TAB_TOOLS).durability(2 * (int) (this.getUses() * handle.getDurability())));
	}

	public AxeItem createHatchet(HandleTier handle) {
		return new AxeItem(this, 1.0f, -2.4f * handle.getWeight(), new Properties().tab(ItemGroup.TAB_TOOLS).durability((int) (this.getUses() * handle.getDurability())));
	}

	public HoeItem createHoe(HandleTier handle) {
		return new HoeItem(this, 0, -3.0f * handle.getWeight(), new Properties().tab(ItemGroup.TAB_TOOLS).durability((int) (this.getUses() * handle.getDurability())));
	}

	public HammerItem createHammer(HandleTier handle) {
		return new HammerItem(this, 5.5f, -3.2f * handle.getWeight(), new Properties().tab(ItemGroup.TAB_TOOLS).durability(4 * (int) (this.getUses() * handle.getDurability())));
	}

	public ChiselItem createChisel(HandleTier handle) {
		return new ChiselItem(this, new Properties().tab(ItemGroup.TAB_TOOLS).durability(4 * (int) (this.getUses() * handle.getDurability())));
	}

	@Override
	public int getUses() {
		return uses;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return damageBonus;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient;
	}
}
