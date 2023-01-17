package bluper.ftgu.world.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class WaterSensitiveBlock extends FallingBlock {
	private final BlockState wet;

	public WaterSensitiveBlock(Properties properties, BlockState wet) {
		super(properties);
		this.wet = wet;
	}

	public BlockState getWet() {
		return wet;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		IBlockReader iblockreader = ctx.getLevel();
		BlockPos blockpos = ctx.getClickedPos();
		BlockState blockstate = iblockreader.getBlockState(blockpos);
		return shouldSolidify(iblockreader, blockpos, blockstate) ? this.wet : super.getStateForPlacement(ctx);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState state2, IWorld world, BlockPos pos, BlockPos pos2) {
		return touchesLiquid(world, pos) ? this.wet : super.updateShape(state, dir, state2, world, pos, pos2);
	}

	private static boolean shouldSolidify(IBlockReader world, BlockPos pos, BlockState state) {
		return canSolidify(state) || touchesLiquid(world, pos);
	}

	private static boolean touchesLiquid(IBlockReader world, BlockPos pos) {
		boolean flag = false;
		BlockPos.Mutable blockpos$mutable = pos.mutable();

		for (Direction direction : Direction.values()) {
			BlockState blockstate = world.getBlockState(blockpos$mutable);
			if (direction != Direction.DOWN || canSolidify(blockstate)) {
				blockpos$mutable.setWithOffset(pos, direction);
				blockstate = world.getBlockState(blockpos$mutable);
				if (canSolidify(blockstate) && !blockstate.isFaceSturdy(world, pos, direction.getOpposite())) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	private static boolean canSolidify(BlockState state) {
		return state.getBlock() == Blocks.WATER;
	}
}
