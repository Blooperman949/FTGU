package bluper.ftgu.registry.registries;

import java.util.ArrayList;

import bluper.ftgu.FTGU;
import bluper.ftgu.world.tileentity.ChamberControllerTileEntity;
import bluper.ftgu.world.tileentity.ChamberPortTileEntity;
import bluper.ftgu.world.tileentity.CreativeFluidSourceTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FTGUTiles {
	public static final DeferredRegister<TileEntityType<?>> R = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FTGU.MODID);

	public static final ArrayList<Block> VALID_CHAMBER_CONTROLLERS = new ArrayList<Block>();
	public static final ArrayList<Block> VALID_CHAMBER_PORTS = new ArrayList<Block>();

	public static final RegistryObject<TileEntityType<ChamberControllerTileEntity>> CHAMBER_CONTROLLER = R.register("chamber_controller", () -> TileEntityType.Builder.of(ChamberControllerTileEntity::new, listToBlockArray(VALID_CHAMBER_CONTROLLERS.toArray())).build(null));
	public static final RegistryObject<TileEntityType<ChamberPortTileEntity>> CHAMBER_PORT = R.register("chamber_port", () -> TileEntityType.Builder.of(ChamberPortTileEntity::new, listToBlockArray(VALID_CHAMBER_PORTS.toArray())).build(null));
	public static final RegistryObject<TileEntityType<CreativeFluidSourceTileEntity>> CREATIVE_FLUID_SOURCE = R.register("creative_fluid_source", () -> TileEntityType.Builder.of(CreativeFluidSourceTileEntity::new, FTGUBlocks.CREATIVE_FLUID_SOURCE.get()).build(null));

	public static Block[] listToBlockArray(Object[] list) {
		Block[] ret = new Block[list.length];
		for (int i = 0; i < list.length; i++) {
			ret[i] = (Block) list[i];
		}
		return ret;
	}
}