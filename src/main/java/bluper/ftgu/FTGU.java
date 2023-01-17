package bluper.ftgu;

import bluper.ftgu.network.FTGUNetwork;
import bluper.ftgu.registry.RegistryMaster;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FTGU.MODID)
public class FTGU {
	public static final String MODID = "ftgu";

	public FTGU() {
		FTGUConfig.register();
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::onFMLCommonSetup);
		RegistryMaster.registerAll(bus);
	}

	void onFMLCommonSetup(FMLCommonSetupEvent event) {
		FTGUNetwork.init();
	}
}
