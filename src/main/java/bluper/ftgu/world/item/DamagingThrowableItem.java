package bluper.ftgu.world.item;

import bluper.ftgu.world.entity.DamagingSnowballEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class DamagingThrowableItem extends Item {

	public DamagingThrowableItem(Properties properties) {
		super(properties);
	}

	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack item = player.getItemInHand(hand);
		world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!world.isClientSide) {
			SnowballEntity entity = new DamagingSnowballEntity(world, player, 3);
			entity.setItem(item);
			entity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
			world.addFreshEntity(entity);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.abilities.instabuild) {
			item.shrink(1);
		}

		return ActionResult.sidedSuccess(item, world.isClientSide());
	}
}
