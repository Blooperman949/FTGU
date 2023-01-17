package bluper.ftgu.data;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import bluper.ftgu.FTGU;
import bluper.ftgu.gui.FTGUCraftingInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.registries.ForgeRegistries;

public class DataUtils {
	public static Predicate<Block> createSurfaceTest(String s) {
		if (s.isEmpty())
			return (block) -> true;
		ResourceLocation rl;
		try {
			rl = new ResourceLocation(s);
			Block t = ForgeRegistries.BLOCKS.getValue(rl);
			return (block) -> block.is(t);
		} catch (ResourceLocationException e) {
			rl = new ResourceLocation(s.substring(1));
			IOptionalNamedTag<Block> t = BlockTags.createOptional(rl);
			return (block) -> block.is(t);
		}
	}

	private static final IOptionalNamedTag<Item> CONSUME = ItemTags.createOptional(new ResourceLocation(FTGU.MODID, "consume_when_crafting"));

	public static NonNullList<ItemStack> getRemainingItems(FTGUCraftingInventory inv) {
		NonNullList<ItemStack> remainingItems = NonNullList.withSize(9, ItemStack.EMPTY);
		for (int i = 0; i < remainingItems.size(); ++i) {
			ItemStack item = inv.getItem(i);
			if (item.hasContainerItem()) {
				remainingItems.set(i, item.getContainerItem());
			} else if (!item.getItem().is(CONSUME) && item.isDamageableItem()) {
				ItemStack damaged = item.copy();
				damaged.setDamageValue(item.getDamageValue() + 1);
				if (!(damaged.getDamageValue() > damaged.getMaxDamage()))
					remainingItems.set(i, damaged);
			}
		}
		return remainingItems;
	}

	@Nullable
	public static BlockState stateFromString(String str) {
		BlockStateParser parser;
		try {
			parser = new BlockStateParser(new StringReader(str), true).parse(true);
		} catch (CommandSyntaxException e) {
			return null;
		}
		return parser.getState();
	}
}
