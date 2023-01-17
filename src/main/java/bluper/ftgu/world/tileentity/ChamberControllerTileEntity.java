package bluper.ftgu.world.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import bluper.ftgu.registry.registries.FTGUTiles;
import bluper.ftgu.util.SaveDataUtils;
import bluper.ftgu.world.block.ChamberControllerBlock;
import bluper.ftgu.world.chamber.Chamber;
import bluper.ftgu.world.chamber.ChamberType;
import bluper.ftgu.world.tileentity.util.MultiFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;

public class ChamberControllerTileEntity extends FTGUTileEntity implements ITickableTileEntity {
	protected ChamberType type;
	protected long tickTimer = 0;
	public final List<ChamberPortTileEntity> ports = Lists.newLinkedList();
	protected Chamber chamber;
	protected MultiFluidTank tank = new MultiFluidTank(0);

	public ChamberControllerTileEntity() {
		super(FTGUTiles.CHAMBER_CONTROLLER.get());
	}

	public ChamberType getChamberType() {
		return type;
	}

	public int height() {
		return chamber == null ? -1 : chamber.height();
	}

	public Chamber getChamber() {
		return chamber;
	}

	public MultiFluidTank getTank() {
		return tank;
	}

	@Override
	public void tick() {
		if (!level.isClientSide) {
			tickTimer++;
			if (tickTimer >= 60) {
				tickTimer = 0;
				reValidate();
			}
		}
	}

	private void reValidate() {
		Chamber.Validator validator = new Chamber.Validator(type, level, worldPosition.above());
		if (!validator.run()) {
			this.removeIfTypeNotNull();
			return;
		}
		Chamber newChamber = validator.makeChamber();
		if (newChamber.height() == 0) this.removeIfTypeNotNull();
		if (!newChamber.equals(chamber)) {
			chamber = newChamber;
			tank.setCapacity(chamber.fluidVolume());
			disconnectAll();
		}
	}

	public void connect(ChamberPortTileEntity port) {
		if (!ports.contains(port)) {
			ports.add(port);
			this.setChanged();
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		tank.load(nbt.getCompound("tank"));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("tank", tank.save());
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
		CompoundNBT pNbt = new CompoundNBT();
		for (int i = 0; i < ports.size(); i++)
			nbt.put("" + i, SaveDataUtils.tilePosToNBT(ports.get(i)));
		nbt.put("ports", pNbt);
		if (chamber != null) nbt.put("chamber", chamber.save());
		return save(nbt);
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
		ports.clear();
		CompoundNBT pNbt = nbt.getCompound("ports");
		for (int i = 0; pNbt.contains("" + i); i++)
			ports.add((ChamberPortTileEntity) SaveDataUtils.tileFromNBTPos(pNbt.getCompound(i + ""), level));
		Chamber c = Chamber.load(nbt.getCompound("chamber"));
		if (c != null) chamber = c;
		load(getBlockState(), nbt);
	}

	public void disconnect(ChamberPortTileEntity port) {
		port.disconnect();
		ports.remove(port);
		this.setChanged();
	}

	private void disconnectAll() {
		ports.forEach((p) -> p.disconnect());
		ports.clear();
		this.setChanged();
	}

	@Override
	public void setRemoved() {
		disconnectAll();
		super.setRemoved();
	}

	public void removeIfTypeNotNull() {
		if (type != null) {
			this.setRemoved();
			level.setBlockAndUpdate(worldPosition, type.getMainBlock().defaultBlockState());
		}
	}

	@Override
	public void onLoad() {
		this.type = ((ChamberControllerBlock) getBlockState().getBlock()).getChamberType();
		super.onLoad();
	}
}
