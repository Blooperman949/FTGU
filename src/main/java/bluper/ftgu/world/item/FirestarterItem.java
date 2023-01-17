package bluper.ftgu.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FirestarterItem extends FlintAndSteelItem {
	private final float chance;

	public FirestarterItem(Properties properties, float chance) {
		super(properties);
		this.chance = chance;
	}

	public ActionResultType useOn(ItemUseContext ctx) {
		PlayerEntity player = ctx.getPlayer();
		World world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = world.getBlockState(pos);
		boolean f = world.isClientSide() ? false : random.nextFloat() < chance;
		if (CampfireBlock.canLight(state)) {
			playSound(world, player, pos);
			if (f)
				world.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
			if (player != null)
				ctx.getItemInHand().hurtAndBreak(1, player, (p) -> {
					p.broadcastBreakEvent(ctx.getHand());
				});
			return ActionResultType.sidedSuccess(world.isClientSide());
		} else {
			BlockPos facePos = pos.relative(ctx.getClickedFace());
			if (AbstractFireBlock.canBePlacedAt(world, facePos, ctx.getHorizontalDirection())) {
				playSound(world, player, pos);
				if (f)
					world.setBlock(facePos, AbstractFireBlock.getState(world, facePos), 11);
				ItemStack itemstack = ctx.getItemInHand();
				if (player instanceof ServerPlayerEntity) {
					if (f)
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, facePos, itemstack);
					itemstack.hurtAndBreak(1, player, (p) -> {
						p.broadcastBreakEvent(ctx.getHand());
					});
				}
				return ActionResultType.sidedSuccess(world.isClientSide());
			} else
				return ActionResultType.FAIL;
		}
	}

	private void playSound(World world, PlayerEntity player, BlockPos pos) {
		world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
	}
}
