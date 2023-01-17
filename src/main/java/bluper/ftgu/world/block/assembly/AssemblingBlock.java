package bluper.ftgu.world.block.assembly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;

public class AssemblingBlock extends FrameBlock {
	public static final IntegerProperty STEP = IntegerProperty.create("step", 1, 4);
	private final Item frame;

	public AssemblingBlock(Properties properties, Item frame) {
		super(properties);
		this.frame = frame;
	}

	@Override
	public Item asItem() {
		return frame;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(STEP);
	}
}
