package bluper.ftgu.registry.registries;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

import bluper.ftgu.FTGU;
import bluper.ftgu.world.block.ChamberPortBlock;
import bluper.ftgu.world.chamber.ChamberType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class Chambers {
	public static final DeferredRegister<ChamberType> R = DeferredRegister.create(ChamberType.class, FTGU.MODID);
	public static final Supplier<IForgeRegistry<ChamberType>> REGISTRY = R.makeRegistry("spells", RegistryBuilder::new);

	public static final RegistryObject<ChamberType> ADOBE_BRICK = R.register("adobe_brick", () -> new ChamberType(new SimpleWallValidator(FTGUBlocks.ADOBE_BRICK_PORT.get()), FTGUBlocks.ADOBE_BRICKS, FTGUBlocks.ADOBE_BRICK_CONTROLLER, 270f, 2000f));

	private static final class SimpleWallValidator implements BiPredicate<BlockState, Direction> {
		private ChamberPortBlock port;

		public SimpleWallValidator(ChamberPortBlock port) {
			this.port = port;
		}

		@Override
		public boolean test(BlockState state, Direction dir) {
			Block block = state.getBlock();
			return block.is(port) ? state.getValue(ChamberPortBlock.FACING) == dir.getOpposite() : false;
		}
	}
}
