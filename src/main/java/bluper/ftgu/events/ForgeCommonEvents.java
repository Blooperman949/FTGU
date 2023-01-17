package bluper.ftgu.events;

import java.util.Random;

import bluper.ftgu.FTGU;
import bluper.ftgu.data.assembly.AssemblyManager;
import bluper.ftgu.data.assembly.IAssembly;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = FTGU.MODID, bus = Bus.FORGE)
public class ForgeCommonEvents {
	private static Random rand = new Random();

	@SubscribeEvent
	static void onAddReloadListener(AddReloadListenerEvent event) {
		event.addListener(AssemblyManager.get());
	}

	@SubscribeEvent
	static void onItemUseEvent(PlayerInteractEvent.RightClickBlock event) {
		ItemStack is = event.getItemStack();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		IAssembly a = AssemblyManager.get().getAssembly(state, is.getItem());
		if (a != null) {
			PlayerEntity player = event.getPlayer();
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), a.getSound(), SoundCategory.BLOCKS, 2f, 0.5f + rand.nextFloat());
			world.setBlockAndUpdate(pos, a.getResult());
			Item pop = a.getPop();
			if (!player.isCreative()) {
				is.setCount(is.getCount() - 1);
				if (pop != null) world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, pop.getDefaultInstance()));
			}
			event.setCancellationResult(ActionResultType.SUCCESS);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	static void onBlockToolInteractEvent(BlockEvent.BlockToolInteractEvent event) {
		event.setCanceled(true);
	}
}
