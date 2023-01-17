package bluper.ftgu.network;

import bluper.ftgu.FTGU;
import bluper.ftgu.network.message.CraftMessage;
import bluper.ftgu.network.message.OpenInventoryMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class FTGUNetwork {
	public static final String VERSION = "0.1.0";

	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(FTGU.MODID, "network"), () -> VERSION, (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));

	public static void init() {
		CHANNEL.registerMessage(0, CraftMessage.class, CraftMessage::encode, CraftMessage::decode, CraftMessage::handle);
		CHANNEL.registerMessage(1, OpenInventoryMessage.class, OpenInventoryMessage::encode, OpenInventoryMessage::decode, OpenInventoryMessage::handle);
	}
}
