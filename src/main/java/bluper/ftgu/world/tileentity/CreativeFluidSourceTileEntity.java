package bluper.ftgu.world.tileentity;

import bluper.ftgu.registry.registries.FTGUTiles;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CreativeFluidSourceTileEntity extends TileEntity implements ITickableTileEntity {
	private FluidStack fluid;

	public CreativeFluidSourceTileEntity() {
		super(FTGUTiles.CREATIVE_FLUID_SOURCE.get());
	}

	public void setFluid(Fluid fluid) {
		this.fluid = new FluidStack(fluid, 1000);
	}

	@Override
	public void tick() {
		if (fluid != null) for (Direction dir : Direction.values()) {
			TileEntity te = level.getBlockEntity(worldPosition.relative(dir));
			if (te == null) continue;
			te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir).map((cap) -> {
				return cap.fill(fluid, FluidAction.EXECUTE);
			});
		}
	}
}
