package bluper.ftgu.world.tileentity.util;

import java.util.List;

import org.apache.logging.log4j.util.TriConsumer;

import com.google.common.collect.Lists;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class MultiFluidTank implements IFluidTank {
	private List<FluidStack> fluids = Lists.newLinkedList();
	private int capacity;
	private int contained = 0;

	public MultiFluidTank(int capacity) {
		this.capacity = capacity;
	}

	public int indexAtFillLevel(int fill) {
		if (fluids.isEmpty()) return 0;
		int i;
		for (i = 0; fill > 0; i++) {
			fill -= fluids.get(i).getAmount();
		}
		return i + 1;
	}

	public FluidStack getFluid(int tank) {
		try {
			return fluids.get(tank);
		} catch (IndexOutOfBoundsException e) {
			return FluidStack.EMPTY;
		}
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (contained >= capacity || resource.isEmpty()) return 0;
		int amount = Math.min(capacity - contained, resource.getAmount());
		if (action.execute()) {
			;
			int tank = fluidIndex(resource, action);
			fluids.get(tank).grow(amount);
			contained += amount;
		}
		return amount;
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (fluids.isEmpty()) return FluidStack.EMPTY;
		int index = fluidIndex(resource, action);
		if (index == -1) return FluidStack.EMPTY;
		FluidStack f = fluids.get(index);
		int amount = Math.min(resource.getAmount(), f.getAmount());
		if (action.execute()) {
			f.shrink(amount);
			if (f.isEmpty()) fluids.remove(index);
			contained -= amount;
		}
		FluidStack ret = f.copy();
		ret.shrink(amount);
		return ret;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		return drain(maxDrain, action, 0);
	}

	public FluidStack drain(int maxDrain, FluidAction action, int index) {
		if (fluids.isEmpty()) return FluidStack.EMPTY;
		FluidStack f = fluids.get(index);
		int amount = Math.min(maxDrain, f.getAmount());
		if (action.execute()) {
			f.shrink(amount);
			if (f.isEmpty()) fluids.remove(index);
			contained -= amount;
		}
		FluidStack ret = f.copy();
		ret.shrink(amount);
		return ret;
	}

	protected int fluidIndex(FluidStack f, FluidAction action) {
		for (int i = 0; i < fluids.size(); i++)
			if (getFluid(i).isFluidEqual(f)) return i;
		return addFluid(f, action);
	}

	protected int addFluid(FluidStack f, FluidAction action) {
		int i;
		int density = f.getFluid().getAttributes().getDensity();
		for (i = 0; density < getFluid(i).getFluid().getAttributes().getDensity(); i++)
			;
		if (action.execute()) fluids.add(i, new FluidStack(f.getFluid(), 0));
		return i;
	}

	public CompoundNBT save() {
		CompoundNBT nbt = new CompoundNBT();
		CompoundNBT fnbt = new CompoundNBT();
		for (int i = 0; i < fluids.size(); i++) {
			fnbt.put("" + i, fluids.get(i).writeToNBT(new CompoundNBT()));
		}
		nbt.put("fluids", fnbt);
		nbt.putInt("capacity", capacity);
		return nbt;
	}

	public void load(CompoundNBT nbt) {
		CompoundNBT fnbt = nbt.getCompound("fluids");
		this.capacity = nbt.getInt("capacity");
		fluids.clear();
		for (int i = 0; fnbt.contains("" + i); i++) {
			FluidStack fs = FluidStack.loadFluidStackFromNBT(fnbt.getCompound("" + i));
			if (!fs.isEmpty()) fluids.add(fs);
		}
		contained = 0;
		for (FluidStack f : fluids)
			contained += f.getAmount();
	}

	@Override
	public FluidStack getFluid() {
		return FluidStack.EMPTY;
	}

	@Override
	public int getFluidAmount() {
		return contained;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public boolean isFluidValid(FluidStack stack) {
		return true;
	}

	public void forEachFluid(TriConsumer<FluidStack, Integer, Integer> con) {
		int lvl = 0;
		for (int i = 0; i < fluids.size(); i++) {
			FluidStack f = fluids.get(i);
			con.accept(f, lvl, i);
			lvl += f.getAmount();
		}
	}
}
