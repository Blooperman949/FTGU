package bluper.ftgu.world.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class DryingBrickBlock extends SingleBrickBlock {
	private final BlockState dried;

	public DryingBrickBlock(Properties properties, BlockState dried) {
		super(properties.randomTicks());
		this.dried = dried;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (world.isDay() && world.canSeeSky(pos)) {
			float humidity = (world.getBiome(pos).getDownfall() + 1) / 2;
			if (rand.nextFloat() > humidity)
				world.setBlockAndUpdate(pos, dried);
		}
	}
}
