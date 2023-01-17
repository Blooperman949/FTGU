package bluper.ftgu.world.tileentity.util;

import bluper.ftgu.world.tileentity.ChamberPortTileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class PortFluidHandler implements IFluidHandler {
	private ChamberPortTileEntity te;

	public PortFluidHandler(ChamberPortTileEntity te) {
		this.te = te;
	}

	private MultiFluidTank tank() {
		return te.getController().getTank();
	}

	private int index() {
		return tank().indexAtFillLevel((int) ((te.height() - 0.75f) * 1000) * te.getController().getChamber().area());
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return tank().getFluid(index());
	}

	@Override
	public int getTankCapacity(int tank) {
		return tank().getCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return true;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return tank().fill(resource, action);
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		return FluidStack.EMPTY;
	}
}
