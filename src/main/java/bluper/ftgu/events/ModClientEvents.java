package bluper.ftgu.events;

import bluper.ftgu.FTGU;
import bluper.ftgu.client.FTGUItemModelsProperties;
import bluper.ftgu.client.FTGUKeys;
import bluper.ftgu.client.render.tileentity.ChamberControllerRenderer;
import bluper.ftgu.client.render.tileentity.ChamberPortRenderer;
import bluper.ftgu.gui.FTGUCraftingScreen;
import bluper.ftgu.gui.FTGUInventoryScreen;
import bluper.ftgu.registry.registries.FTGUContainerTypes;
import bluper.ftgu.registry.registries.FTGUItems;
import bluper.ftgu.registry.registries.FTGUTiles;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = FTGU.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModClientEvents {
	@SubscribeEvent
	static void onFMLClientSetup(final FMLClientSetupEvent event) {
		// UI
		FTGUKeys.register(event);

		ScreenManager.register(FTGUContainerTypes.MOD_CRAFTING.get(), (container, inventory, title) -> new FTGUCraftingScreen(container, inventory));
		ScreenManager.register(FTGUContainerTypes.MOD_PLAYER.get(), (container, inventory, title) -> new FTGUInventoryScreen(container, inventory));

		// Models
		FTGUItemModelsProperties.makeBow(FTGUItems.PRIMITIVE_BOW.get());

		// TERs
		ClientRegistry.bindTileEntityRenderer(FTGUTiles.CHAMBER_CONTROLLER.get(), ChamberControllerRenderer::new);
		ClientRegistry.bindTileEntityRenderer(FTGUTiles.CHAMBER_PORT.get(), ChamberPortRenderer::new);
	}
}
