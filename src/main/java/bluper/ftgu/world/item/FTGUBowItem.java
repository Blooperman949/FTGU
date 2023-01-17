package bluper.ftgu.world.item;

import bluper.ftgu.world.item.tier.HandleTier;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class FTGUBowItem extends BowItem {
	private int drawTime;
	private float powerFactor;

	public FTGUBowItem(HandleTier tier, int baseDurability) {
		super(new Properties().tab(ItemGroup.TAB_COMBAT).durability((int) (tier.getDurability() * baseDurability)));
		int base = 20;
		float stat = tier.getDurability();
		this.drawTime = (int) (4 * base - 3 * stat * base);
		this.powerFactor = stat;
	}

	@Override
	public void releaseUsing(ItemStack itemStack, World world, LivingEntity shooter, int time) {
		if (shooter instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) shooter;
			boolean infinity = player.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, itemStack) > 0;
			ItemStack arrowStack = player.getProjectile(itemStack);
			int t = this.getUseDuration(itemStack) - time;
			t = ForgeEventFactory.onArrowLoose(itemStack, world, player, t, !arrowStack.isEmpty() || infinity);
			if (t < 0)
				return;
			if (!arrowStack.isEmpty() || infinity) {
				if (arrowStack.isEmpty())
					arrowStack = new ItemStack(Items.ARROW);
				float f = getModPowerForTime(t);
				if (f > 0.05f) {
					boolean hasArrow = player.abilities.instabuild || (arrowStack.getItem() instanceof ArrowItem && ((ArrowItem) arrowStack.getItem()).isInfinite(arrowStack, itemStack, player));
					if (!world.isClientSide) {
						ArrowItem arrow = (ArrowItem) (arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
						AbstractArrowEntity arrowEntity = arrow.createArrow(world, arrowStack, player);
						arrowEntity = customArrow(arrowEntity);
						arrowEntity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 3.0f * f * powerFactor, 1.0F);
						if (f == 1.0F)
							arrowEntity.setCritArrow(true);
						int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
						if (j > 0)
							arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (double) j * 0.5D + 0.5D);
						int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
						if (k > 0)
							arrowEntity.setKnockback(k);
						if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) > 0)
							arrowEntity.setSecondsOnFire(100);
						itemStack.hurtAndBreak(1, player, (p_220009_1_) -> {
							p_220009_1_.broadcastBreakEvent(player.getUsedItemHand());
						});
						if (hasArrow || player.abilities.instabuild && (arrowStack.getItem() == Items.SPECTRAL_ARROW || arrowStack.getItem() == Items.TIPPED_ARROW))
							arrowEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
						world.addFreshEntity(arrowEntity);
					}
					world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!hasArrow && !player.abilities.instabuild) {
						arrowStack.shrink(1);
						if (arrowStack.isEmpty())
							player.inventory.removeItem(arrowStack);
					}
					player.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}

	public float getDrawTime() {
		return drawTime;
	}

	private float getModPowerForTime(int time) {
		float f = (float) time / drawTime;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F)
			f = 1.0F;
		return f;
	}
}
