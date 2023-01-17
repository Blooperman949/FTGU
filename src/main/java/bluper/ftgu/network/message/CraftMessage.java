package bluper.ftgu.network.message;

import java.util.function.Supplier;

import bluper.ftgu.gui.FTGUCraftingContainer;
import bluper.ftgu.registry.registries.FTGUContainerTypes;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.fml.network.NetworkEvent;

public class CraftMessage {
	private BlockRayTraceResult rt;

	public CraftMessage(BlockRayTraceResult rt) {
		this.rt = rt;
	}

	public static void encode(CraftMessage msg, PacketBuffer buf) {
		buf.writeBlockHitResult(msg.rt);
	}

	public static CraftMessage decode(PacketBuffer buf) {
		return new CraftMessage(buf.readBlockHitResult());
	}

	public static void handle(CraftMessage msg, Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			ctx.getSender().openMenu(new SimpleNamedContainerProvider((i, inventory, p) -> {
				BlockPos pos = msg.rt.getBlockPos();
				Direction dir = msg.rt.getDirection();
				PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
				buf.writeBlockPos(pos);
				buf.writeBoolean(dir == Direction.UP);
				return FTGUContainerTypes.MOD_CRAFTING.get().create(i, inventory, buf);
			}, FTGUCraftingContainer.TITLE));
		});
		ctx.setPacketHandled(true);
	}
}
