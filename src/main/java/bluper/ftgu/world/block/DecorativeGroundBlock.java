package bluper.ftgu.world.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

public abstract class DecorativeGroundBlock extends FallingBlock {
	public DecorativeGroundBlock(Properties properties) {
		super(properties);
	}

	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.XZ;
	}

	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		Vector3d vector3d = state.getOffset(world, pos);
		return getShapeBeforeOffset(state, world, pos, ctx).move(vector3d.x, vector3d.y, vector3d.z);
	}

	protected abstract VoxelShape getShapeBeforeOffset(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx);
}
