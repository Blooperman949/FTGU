package bluper.ftgu.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;

public class SimpleStairsBlock extends StairsBlock {
	public SimpleStairsBlock(Block block) {
		super(() -> block.defaultBlockState(), Properties.copy(block));
	}
}
