package bluper.ftgu.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

public class BlockItemTag implements ITag<Item> {
	private ITag<Block> tag;

	public BlockItemTag(ITag<Block> tag) {
		this.tag = tag;
		tag.getValues().forEach(b -> {
			if (b.asItem() == null)
				throw new IllegalArgumentException("Tag contains a Block without a BlockItem");
		});
	}

	@Override
	public boolean contains(Item item) {
		if (item instanceof BlockItem)
			return ((BlockItem) item).getBlock().is(tag);
		return false;
	}

	public boolean contains(Block block) {
		return block.is(tag);
	}

	@Override
	public List<Item> getValues() {
		List<Item> list = new ArrayList<Item>();
		tag.getValues().forEach(block -> list.add(block.asItem()));
		return list;
	}

	public List<Block> getBlockValues() {
		return tag.getValues();
	}
}
