package bluper.ftgu.world.block.assembly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class FrameBlock extends Block {
	private static final VoxelShape SHAPE = Block.box(0.0001, 0.0001, 0.0001, 15.9999, 15.9999, 15.9999);

	public FrameBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		BlockPos below = pos.below();
		BlockState belowState = world.getBlockState(below);
		return isFaceFull(belowState.getShape(world, below), Direction.UP);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState state2, IWorld world, BlockPos pos, BlockPos pos2) {
		return dir == Direction.DOWN && !this.canSurvive(state, world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, state2, world, pos, pos2);
	}
}
