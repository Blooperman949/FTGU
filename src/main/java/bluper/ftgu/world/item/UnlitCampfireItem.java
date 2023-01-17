package bluper.ftgu.world.item;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;

public class UnlitCampfireItem extends BlockItem {
	public UnlitCampfireItem(Properties properties) {
		super(Blocks.CAMPFIRE, properties);
	}

	@Override
	@Nullable
	protected BlockState getPlacementState(BlockItemUseContext ctx) {
		BlockState state = this.getBlock().getStateForPlacement(ctx).setValue(CampfireBlock.LIT, false);
		return state != null && this.canPlace(ctx, state) ? state : null;
	}

	@Override
	public void registerBlocks(Map<Block, Item> p_195946_1_, Item p_195946_2_) {
	}
}
