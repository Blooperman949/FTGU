package bluper.ftgu.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

/**
 * Simple wrapper class for {@link ITag}{@code <}{@link Block}{@code >} where blocks can be interpreted as {@code Item}s.
 * Useful for JEI.
 * @author Bluperman949
 */
public class BlockItemTag implements ITag<Item> {
	private ITag<Block> tag;

	/**
	 * @param tag an {@link ITag}{@code <}{@link Block}{@code >} to be interpreted as an {@code Item} tag.
	 * @throws IllegalArgumentException tag contains a {@code Block} without a {@code BlockItem}.
	 */
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
