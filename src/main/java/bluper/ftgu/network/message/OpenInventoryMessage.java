package bluper.ftgu.network.message;

import java.util.function.Supplier;

import bluper.ftgu.gui.FTGUCraftingContainer;
import bluper.ftgu.registry.registries.FTGUContainerTypes;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class OpenInventoryMessage {
	public OpenInventoryMessage() {
	}

	public static void encode(OpenInventoryMessage msg, PacketBuffer buf) {
	}

	public static OpenInventoryMessage decode(PacketBuffer buf) {
		return new OpenInventoryMessage();
	}

	public static void handle(OpenInventoryMessage msg, Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			ctx.getSender().openMenu(new SimpleNamedContainerProvider((i, inventory, p) -> {
				PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
				return FTGUContainerTypes.MOD_PLAYER.get().create(i, inventory, buf);
			}, FTGUCraftingContainer.TITLE));
		});
		ctx.setPacketHandled(true);
	}
}
