package bluper.ftgu.world.item;

import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import bluper.ftgu.registry.registries.FTGUSounds;
import bluper.ftgu.world.block.ChamberPortBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class ChiselItem extends ToolItem {
	private static final Mode[] MODES = Mode.values();
	private static final String NBT_MODE = "mode";
	private static final Random RAND = new Random();

	public ChiselItem(IItemTier tier, Properties properties) {
		super(1, 1, tier, ImmutableSet.of(), properties);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack is = player.getItemInHand(hand);
		if (player.isCrouching() && !world.isClientSide())
			nextMode(is, (ServerPlayerEntity) player);
		return ActionResult.pass(is);
	}

	private static void nextMode(ItemStack is, ServerPlayerEntity player) {
		byte mode = is.getTag().getByte(NBT_MODE);
		mode++;
		if (mode >= MODES.length)
			mode = 0;
		is.getTag().putByte(NBT_MODE, mode);
		player.connection.send(new STitlePacket(STitlePacket.Type.ACTIONBAR, MODES[mode].name));
	}

	@Override
	public ActionResultType useOn(ItemUseContext ctx) {
		PlayerEntity player = ctx.getPlayer();
		World world = ctx.getLevel();
		ItemStack is = ctx.getItemInHand();
		if (!player.isCrouching()) {
			Hand oppHand = ctx.getHand() == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
			if (player.getItemInHand(oppHand).getItem() instanceof HammerItem) {
				byte mode = is.getTag().getByte(NBT_MODE);
				BlockPos pos = ctx.getClickedPos();
				Block b = world.getBlockState(pos).getBlock();
				Block c = tryChisel(b, mode);
				if (c != Blocks.AIR) {
					BlockState state = c.defaultBlockState();
					boolean bottom = ctx.getClickedFace() != Direction.UP && ctx.getClickLocation().y % 1d < 0.5;
					if (MODES[mode] == Mode.SLAB && bottom)
						state = state.setValue(SlabBlock.TYPE, SlabType.TOP);
					else if (MODES[mode] == Mode.STAIRS) {
						state = state.setValue(StairsBlock.FACING, player.getDirection());
						if (bottom)
							state = state.setValue(StairsBlock.HALF, Half.TOP);
					}
					else if (MODES[mode] == Mode.PORT) state = state.setValue(ChamberPortBlock.FACING, player.getDirection());
					world.setBlockAndUpdate(pos, state);
					for (Direction d : Direction.values())
						world.neighborChanged(pos.relative(d), c, pos);
					is.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(ctx.getHand()));
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), FTGUSounds.CHISEL.get(), SoundCategory.BLOCKS, 1f, 0.8f + RAND.nextFloat() / 5);
					return ActionResultType.SUCCESS;
				}
			}
		} else if (!world.isClientSide())
			nextMode(is, (ServerPlayerEntity) player);
		return ActionResultType.FAIL;
	}

	@Nullable
	public static Block tryChisel(Block b, int mode) {
		ResourceLocation rl = b.getRegistryName();
		String s = rl.getPath();
		if (s.endsWith("bricks") && MODES[mode] != Mode.CHISELED)
			s = s.substring(0, s.length() - 1);
		Block c = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(rl.getNamespace(), MODES[mode].nameMaker.apply(s)));
		return c;
	}

	public static enum Mode {
		SLAB((s) -> s + "_slab", new TranslationTextComponent("gui.ftgu.chiselMode.slab")),
		STAIRS((s) -> s + "_stairs", new TranslationTextComponent("gui.ftgu.chiselMode.stairs")),
		WALL((s) -> s + "_wall", new TranslationTextComponent("gui.ftgu.chiselMode.wall")),
		CHISELED((s) -> "chiseled_" + s, new TranslationTextComponent("gui.ftgu.chiselMode.chiseled")),
		PORT((s) -> s + "_port", new TranslationTextComponent("gui.ftgu.chiselMode.port"));

		public final Function<String, String> nameMaker;
		public final ITextComponent name;

		Mode(Function<String, String> nameMaker, ITextComponent name) {
			this.nameMaker = nameMaker;
			this.name = name;
		}
	}
}
