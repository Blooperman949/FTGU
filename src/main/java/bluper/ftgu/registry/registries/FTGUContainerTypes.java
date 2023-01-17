package bluper.ftgu.registry.registries;

import bluper.ftgu.FTGU;
import bluper.ftgu.gui.FTGUCraftingContainer;
import bluper.ftgu.gui.FTGUPlayerContainer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FTGUContainerTypes {
	public static final DeferredRegister<ContainerType<?>> R = DeferredRegister.create(ForgeRegistries.CONTAINERS, FTGU.MODID);

	static final IOptionalNamedTag<Block> CRAFTING_SURFACES = BlockTags.createOptional(new ResourceLocation(FTGU.MODID, "crafting_surfaces"));

	public static final RegistryObject<ContainerType<FTGUCraftingContainer>> MOD_CRAFTING = R.register("crafting", () -> IForgeContainerType.create((i, inventory, buf) -> {
		World world = inventory.player.getCommandSenderWorld();
		Minecraft mc = Minecraft.getInstance();
		BlockPos pos;
		boolean up;
		if (world.isClientSide) {
			BlockRayTraceResult hr = (BlockRayTraceResult) mc.hitResult;
			pos = hr.getBlockPos();
			up = hr.getDirection() == Direction.UP;
		} else {
			pos = buf.readBlockPos();
			up = buf.readBoolean();
		}
		Block surface = world.getBlockState(pos).getBlock();
		return new FTGUCraftingContainer(i, inventory, IWorldPosCallable.create(world, pos), surface.is(CRAFTING_SURFACES) && up ? surface : Blocks.AIR);
	}));
	public static final RegistryObject<ContainerType<FTGUPlayerContainer>> MOD_PLAYER = R.register("player", () -> IForgeContainerType.create((i, inventory, buf) -> {
		return new FTGUPlayerContainer(inventory, true, inventory.player);
	}));
}