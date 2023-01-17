package bluper.ftgu.client;

import java.awt.event.KeyEvent;

import bluper.ftgu.FTGU;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class FTGUKeys {
	public static KeyBinding craftKey;

	public static void register(final FMLClientSetupEvent event) {
		craftKey = FTGUKeys.createBinding("craft", KeyEvent.VK_Z);
		ClientRegistry.registerKeyBinding(craftKey);
	}

	private static KeyBinding createBinding(String name, int key) {
		return new KeyBinding("key." + FTGU.MODID + "." + name, key, "key.category." + FTGU.MODID);
	}
}
