package bluper.ftgu.events;

import bluper.ftgu.FTGU;
import bluper.ftgu.client.FTGUKeys;
import bluper.ftgu.client.render.debug.FTGUDebug;
import bluper.ftgu.network.FTGUNetwork;
import bluper.ftgu.network.message.CraftMessage;
import bluper.ftgu.network.message.OpenInventoryMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = FTGU.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ForgeClientEvents {
	@SubscribeEvent
	static void onKeyInput(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;
		onInput(mc, event.getKey(), event.getAction());
	}

	@SubscribeEvent
	static void onMouseInput(InputEvent.MouseInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;
		onInput(mc, event.getButton(), event.getAction());
	}

	private static int lastKey;
	private static void onInput(Minecraft mc, int key, int action) {
		if (FTGUKeys.craftKey.isDown() && mc.screen == null) {
			mc.player.swing(Hand.MAIN_HAND, true);
			BlockRayTraceResult rt = (BlockRayTraceResult) mc.hitResult;
			FTGUNetwork.CHANNEL.sendToServer(new CraftMessage(rt));
		}
		if (lastKey != 89 && InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292) && key == 89) {
			FTGUDebug.toggleDebugMode();
			FTGUDebug.sendDebugNotif("gui.ftgu.debug" + (FTGUDebug.debugModeOn ? "On" : "Off"));
		}
		lastKey = key;
	}

	@SubscribeEvent
	static void onGUIOpen(final GuiOpenEvent event) {
		Screen gui = event.getGui();
		if (gui instanceof InventoryScreen && !Minecraft.getInstance().gameMode.hasInfiniteItems()) {
			event.setCanceled(true);
			FTGUNetwork.CHANNEL.sendToServer(new OpenInventoryMessage());
		}
	}

//	private static IRecipeType<?>[] r = { IRecipeType.CRAFTING, IRecipeType.BLASTING, IRecipeType.SMELTING, IRecipeType.SMITHING, IRecipeType.SMOKING, IRecipeType.STONECUTTING };
//	private static final String E = "gunk";

	@SubscribeEvent
	static void onDatapackSync(final OnDatapackSyncEvent event) {
		ServerPlayerEntity player = event.getPlayer();
		if (player != null) player.awardRecipes(player.getCommandSenderWorld().getRecipeManager().getRecipes());
//		player.getCommandSenderWorld().getRecipeManager().getRecipes().forEach(i -> {
//			if (i.getId().getNamespace().equals("minecraft")) {
//				for (IRecipeType<?> j : r)
//					if (i.getType().equals(j)) {
//						try {
//							FileWriter w = new FileWriter("C:/Users/chris/eclipse-workspace/FTGU/src/generated/resources/data/minecraft/recipes/" + i.getId().getPath() + ".json");
//							w.write(E);
//							FTGU.LOG.debug("writing blank recipe to " + i.getId());
//							w.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//			}
//		});
	}
}
