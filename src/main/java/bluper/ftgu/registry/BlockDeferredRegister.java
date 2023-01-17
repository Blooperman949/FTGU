package bluper.ftgu.registry;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * A wrapper for two <i>existing</i> {@link DeferredRegister}s that facilitates
 * the registry of {@link BlockItem}s.
 */
public class BlockDeferredRegister {
	protected DeferredRegister<Block> blocks;
	protected DeferredRegister<Item> items;

	/**
	 * Wrap two {@link DeferredRegister}s with the same modid.
	 * @param blocks an existing block register
	 * @param items an existing item register
	 */
	public BlockDeferredRegister(DeferredRegister<Block> blocks, DeferredRegister<Item> items) {
		this.blocks = blocks;
		this.items = items;
	}

	/**
	 * Register a {@code Block} and a {@code BlockItem} at the same time
	 * @param name registry name to be passed to each internal {@code DeferredRegister}
	 * @param sup block supplier to be passed to each internal {@code DeferredRegister}
	 * @param tab the {@link ItemGroup} for the {@code BlockItem}
	 * @return a {@link RegistryObject} for the {@code Block} registered
	 */
	public <B extends Block> RegistryObject<B> register(String name, Supplier<B> sup, ItemGroup tab) {
		RegistryObject<B> ret = register(name, sup);
		items.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(tab)));
		return ret;
	}

	/**
	 * Register a {@code Block}
	 * @param name registry name to be passed to each internal {@code DeferredRegister}
	 * @param sup block supplier to be passed to each internal {@code DeferredRegister}
	 * @return a {@link RegistryObject} for the {@code Block} registered
	 */
	public <B extends Block> RegistryObject<B> register(String name, Supplier<B> sup) {
		return blocks.register(name, sup);
	}

	/**
	 * Call {@code register(IEventBus)} only on the internal block register.
	 */
	public void register(IEventBus bus) {
		blocks.register(bus);
	}
}