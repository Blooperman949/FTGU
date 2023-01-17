package bluper.ftgu.world.tileentity;

import javax.annotation.Nullable;

import bluper.ftgu.registry.registries.FTGUTiles;
import bluper.ftgu.util.SaveDataUtils;
import bluper.ftgu.world.block.ChamberPortBlock;
import bluper.ftgu.world.chamber.Chamber;
import bluper.ftgu.world.chamber.ChamberType;
import bluper.ftgu.world.tileentity.util.PortFluidHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ChamberPortTileEntity extends FTGUTileEntity implements ITickableTileEntity {
	private ChamberType type;
	private ChamberControllerTileEntity controller = null;
	private long tickCounter = 0;
	private BlockPos eyes = BlockPos.ZERO;
	private int height;
	private PortFluidHandler fluidHandler = new PortFluidHandler(this);
	private LazyOptional<IFluidHandler> optional = LazyOptional.of(() -> fluidHandler);

	public ChamberPortTileEntity() {
		super(FTGUTiles.CHAMBER_PORT.get());
	}

	public ChamberType getChamberType() {
		return type;
	}

	public int height() {
		return height;
	}

	public BlockPos getEyes() {
		return eyes;
	}

	public ChamberControllerTileEntity getController() {
		return controller;
	}

	@Override
	public void tick() {
		if (!level.isClientSide) {
			tickCounter++;
			if (tickCounter >= 40) {
				tickCounter = 0;
				if (controller == null && type != null) {
					look(worldPosition.relative(getBlockState().getValue(ChamberPortBlock.FACING)));
					boolean valid = search(Direction.DOWN, Chamber.MAX_HEIGHT);
					if (!valid) return;
					valid = search(Direction.NORTH, Chamber.MAX_WIDTH);
					if (!valid) return;
					valid = search(Direction.WEST, Chamber.MAX_WIDTH);
					if (valid) {
						look(eyes.relative(Direction.DOWN));
						valid = type.validateWall(level.getBlockState(eyes), Direction.DOWN);
						if (!valid) return;
						tryGetController();
						if (controller == null) {
							level.setBlockAndUpdate(eyes, type.getController().defaultBlockState());
							tryGetController();
						}
						tryConnect();
					}
					this.setChanged();
				}
			}
		}
	}

	private void tryGetController() {
		controller = (ChamberControllerTileEntity) level.getBlockEntity(eyes);
	}

	private void tryConnect() {
		if (controller.height() >= height) {
			controller.connect(this);
		} else controller.disconnect(this);
	}

	private void look(BlockPos pos) {
		eyes = pos;
	}

	private boolean search(Direction dir, int n) {
		boolean hitWall = false;
		int i;
		for (i = 0; i < n; i++) {
			look(eyes.relative(dir));
			BlockState state = level.getBlockState(eyes);
			hitWall = type.validateWall(state, dir);
			if (hitWall) break;
		}
		look(eyes.relative(dir.getOpposite()));
		if (hitWall && dir == Direction.DOWN) height = i + 1;
		return hitWall;
	}

	@Override
	public void setRemoved() {
		if (controller != null) controller.disconnect(this);
		super.setRemoved();
	}

	public void disconnect() {
		controller = null;
		this.setChanged();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction dir) {
		if (controller != null && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && dir == getBlockState().getValue(ChamberPortBlock.FACING)) {
			return optional.cast();
		}
		return super.getCapability(cap, dir);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		return super.save(nbt);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
		if (eyes != null) nbt.put("eyes", SaveDataUtils.blockPosToNBT(eyes));
		return save(nbt);
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		CompoundNBT eyesTag = tag.getCompound("eyes");
		this.eyes = SaveDataUtils.blockPosFromNBT(eyesTag);
		if (controller == null) {
			tryGetController();
			if (controller != null) tryConnect();
		}
		load(getBlockState(), tag);
	}

	@Override
	public void onLoad() {
		this.type = ((ChamberPortBlock) getBlockState().getBlock()).getChamberType();
		super.onLoad();
	}
}
