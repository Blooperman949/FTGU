package bluper.ftgu.registry;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class BlockDeferredRegister {
	protected DeferredRegister<Block> blocks;
	protected DeferredRegister<Item> items;

	public BlockDeferredRegister(DeferredRegister<Block> blocks, DeferredRegister<Item> items) {
		this.blocks = blocks;
		this.items = items;
	}

	public <B extends Block> RegistryObject<B> register(String name, Supplier<B> sup, ItemGroup tab) {
		RegistryObject<B> ret = register(name, sup);
		items.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(tab)));
		return ret;
	}

	public <B extends Block> RegistryObject<B> register(String name, Supplier<B> sup) {
		return blocks.register(name, sup);
	}

	public void register(IEventBus bus) {
		blocks.register(bus);
	}
}