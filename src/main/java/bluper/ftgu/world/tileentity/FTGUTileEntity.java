package bluper.ftgu.world.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class FTGUTileEntity extends TileEntity {
	public FTGUTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		BlockState state = level.getBlockState(this.getBlockPos());
		this.level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
	}
}
