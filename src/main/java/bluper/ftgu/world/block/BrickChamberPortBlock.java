package bluper.ftgu.world.block;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import bluper.ftgu.util.VoxelShapeUtils;
import bluper.ftgu.world.chamber.ChamberType;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BrickChamberPortBlock extends ChamberPortBlock {
	protected static final VoxelShape SHAPE = VoxelShapes.join(ChamberPortBlock.SHAPE_Z, box(4, 0, 4, 12, 4, 16), IBooleanFunction.ONLY_FIRST);
	// I have no fucking clue how or why this works, but it does (barely). Using `rotateShape()` is a game of trial and error.
	protected static final Map<Direction, VoxelShape> SHAPES_BY_ROTATION = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, VoxelShapeUtils.rotateShape(Direction.SOUTH, Direction.NORTH, SHAPE), 
			Direction.SOUTH, SHAPE, 
			Direction.EAST, VoxelShapeUtils.rotateShape(Direction.NORTH, Direction.EAST, SHAPE), 
			Direction.WEST, VoxelShapeUtils.rotateShape(Direction.SOUTH, Direction.EAST, SHAPE)
			));

	public BrickChamberPortBlock(Properties properties, Supplier<ChamberType> chamberType) {
		super(properties, chamberType);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPES_BY_ROTATION.get(state.getValue(FACING));
	}
}
