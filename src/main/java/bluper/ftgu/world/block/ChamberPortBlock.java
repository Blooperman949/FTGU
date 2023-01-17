package bluper.ftgu.world.block;

import java.util.function.Supplier;

import bluper.ftgu.registry.registries.FTGUTiles;
import bluper.ftgu.util.VoxelShapeUtils;
import bluper.ftgu.world.chamber.ChamberType;
import bluper.ftgu.world.tileentity.ChamberPortTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class ChamberPortBlock extends Block {
	private Supplier<ChamberType> chamberType;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	protected static final VoxelShape SHAPE_Z = VoxelShapes.join(box(0, 0, 0, 16, 16, 16), box(4, 4, 0, 12, 12, 16), IBooleanFunction.ONLY_FIRST);
	protected static final VoxelShape SHAPE_X = VoxelShapeUtils.rotateShape(Direction.SOUTH, Direction.EAST, SHAPE_Z);

	public ChamberPortBlock(Properties properties, Supplier<ChamberType> chamberType) {
		super(properties);
		this.chamberType = chamberType;
		FTGUTiles.VALID_CHAMBER_PORTS.add(this);
	}

	public ChamberType getChamberType() {
		return chamberType.get();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ChamberPortTileEntity();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return state.getValue(FACING).getAxis() == Axis.X ? SHAPE_X : SHAPE_Z;
	}
}
