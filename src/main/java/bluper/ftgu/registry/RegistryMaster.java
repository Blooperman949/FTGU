package bluper.ftgu.registry;

import bluper.ftgu.registry.registries.Chambers;
import bluper.ftgu.registry.registries.FTGUBlocks;
import bluper.ftgu.registry.registries.FTGUContainerTypes;
import bluper.ftgu.registry.registries.FTGUEntities;
import bluper.ftgu.registry.registries.FTGUFluids;
import bluper.ftgu.registry.registries.FTGUItems;
import bluper.ftgu.registry.registries.FTGURecipeTypes;
import bluper.ftgu.registry.registries.FTGUSounds;
import bluper.ftgu.registry.registries.FTGUTiles;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;

public class RegistryMaster {
	public static void registerAll(IEventBus bus) {
		Chambers.R.register(bus);
		FTGUBlocks.R.register(bus);
		FTGUBlocks.OVR.register(bus);
		FTGUContainerTypes.R.register(bus);
		FTGUEntities.R.register(bus);
		FTGUFluids.R.register(bus);
		FTGUItems.R.register(bus);
		FTGUItems.OVR.register(bus);
		FTGURecipeTypes.R.register(bus);
		FTGUSounds.R.register(bus);
		FTGUTiles.R.register(bus);
		ForgeMod.enableMilkFluid();
	}
}
