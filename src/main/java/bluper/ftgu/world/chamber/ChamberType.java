package bluper.ftgu.world.chamber;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

import bluper.ftgu.util.Temperature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ChamberType extends ForgeRegistryEntry<ChamberType> {
	private BiPredicate<BlockState, Direction> wallValidator;
	private Supplier<? extends Block> mainBlock;
	private Supplier<? extends Block> controller;
	private Temperature minTemp;
	private Temperature maxTemp;

	public ChamberType(BiPredicate<BlockState, Direction> wallValidator, Supplier<? extends Block> mainBlock, Supplier<? extends Block> controller, float minTemp, float maxTemp) {
		this.wallValidator = wallValidator;
		this.mainBlock = mainBlock;
		this.controller = controller;
		this.minTemp = new Temperature(minTemp);
		this.maxTemp = new Temperature(maxTemp);
	}

	public boolean validateWall(BlockState state, Direction dir) {
		if (state.is(mainBlock.get())) return true;
		else if (dir == Direction.DOWN) return state.is(controller.get());
		return wallValidator.test(state, dir);
	}

	public Block getMainBlock() {
		return mainBlock.get();
	}

	public Block getController() {
		return controller.get();
	}

	public Temperature getMinTemp() {
		return minTemp;
	}

	public Temperature getMaxTemp() {
		return maxTemp;
	}
}
