package bluper.ftgu.data;

import java.util.function.Predicate;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import bluper.ftgu.FTGU;
import bluper.ftgu.gui.FTGUCraftingInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

/**
 * Some static methods to help with JSON data.
 * @author Bluperman949
 */
public class DataUtils {
	/**
	 * Creates a {@code Predicate<Block>} used by FTGU's Surface Crafting
	 * @param surface a block tag or block registry name formatted as a {@code ResourceLocation}
	 * @return a predicate for testing whether a {@code Block} matches the given surface
	 * @see ShapedSurfaceRecipe
	 * @see ShapelessSurfaceRecipe
	 * @see FTGUCraftingContainer
	 */
	public static Predicate<Block> createSurfaceTest(String surface) {
		if (surface.isEmpty())
			return (block) -> true;
		ResourceLocation rl;
		try {
			rl = new ResourceLocation(surface);
			Block t = ForgeRegistries.BLOCKS.getValue(rl);
			return (block) -> block.is(t);
		} catch (ResourceLocationException e) {
			rl = new ResourceLocation(surface.substring(1));
			IOptionalNamedTag<Block> t = BlockTags.createOptional(rl);
			return (block) -> block.is(t);
		}
	}

	private static final IOptionalNamedTag<Item> CONSUME = ItemTags.createOptional(new ResourceLocation(FTGU.MODID, "consume_when_crafting"));

	/**
	 * Used by FTGU's crafting GUI to get damaged tool items and empty buckets after a recipe is crafted.
	 * @see FTGUCraftingResultSlot
	 */
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

	/**
	 * Uses Minecraft's command parser to infer a {@code BlockState} from a {@code String}.<br>
	 * Example:<br>{@code "minecraft:oak_slab[type=bottom,waterlogged=false]"}
	 * @param str a block state {@code String}
	 * @return a parsed {@link BlockState}, air if {@code str} is invalid
	 */
	public static BlockState stateFromString(String str) {
		BlockStateParser parser;
		try {
			parser = new BlockStateParser(new StringReader(str), true).parse(true);
		} catch (CommandSyntaxException e) {
			return Blocks.AIR.defaultBlockState();
		}
		return parser.getState();
	}
}
