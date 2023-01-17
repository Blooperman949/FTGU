package bluper.ftgu.world.block;

import bluper.ftgu.world.tileentity.CreativeFluidSourceTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CreativeFluidSourceBlock extends Block {
	public CreativeFluidSourceBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CreativeFluidSourceTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rt) {
		Item item = player.getItemInHand(hand).getItem();
		if (item instanceof BucketItem) {
			((CreativeFluidSourceTileEntity) world.getBlockEntity(pos)).setFluid(((BucketItem) item).getFluid());
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}
}
