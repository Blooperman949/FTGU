package bluper.ftgu.registry.registries;

import java.util.function.Supplier;

import bluper.ftgu.FTGU;
import bluper.ftgu.registry.BlockDeferredRegister;
import bluper.ftgu.registry.FTGUToolType;
import bluper.ftgu.world.block.BrickChamberPortBlock;
import bluper.ftgu.world.block.ChamberControllerBlock;
import bluper.ftgu.world.block.CreativeFluidSourceBlock;
import bluper.ftgu.world.block.DryingBrickBlock;
import bluper.ftgu.world.block.LargeRockBlock;
import bluper.ftgu.world.block.SimpleStairsBlock;
import bluper.ftgu.world.block.SingleBrickBlock;
import bluper.ftgu.world.block.SmallRockBlock;
import bluper.ftgu.world.block.WaterSensitiveBlock;
import bluper.ftgu.world.block.assembly.AssemblingBlock;
import bluper.ftgu.world.block.assembly.FrameBlock;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FTGUBlocks {
	public static final BlockDeferredRegister R = new BlockDeferredRegister(DeferredRegister.create(ForgeRegistries.BLOCKS, FTGU.MODID), FTGUItems.R);
	public static final BlockDeferredRegister OVR = new BlockDeferredRegister(DeferredRegister.create(ForgeRegistries.BLOCKS, "minecraft"), FTGUItems.OVR);

	private static <B extends Block> RegistryObject<B> override(String name, Supplier<B> sup) {
		return OVR.register(name, sup, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)).asItem().getItemCategory());
	}

	static final Properties CHARCOAL_PROPERTIES = Properties.of(Material.DIRT, DyeColor.BLACK).requiresCorrectToolForDrops().strength(1.0f).harvestTool(ToolType.SHOVEL).sound(SoundType.BASALT);
	static final Material WEAK_MATERIAL = new Material.Builder(MaterialColor.STONE).nonSolid().noCollider().build();

	// Natural
	public static final RegistryObject<Block> MUD = R.register("mud", () -> new Block(Properties.copy(Blocks.DIRT).sound(SoundType.SLIME_BLOCK).requiresCorrectToolForDrops().harvestTool(ToolType.SHOVEL)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<WaterSensitiveBlock> DIRT = override("dirt", () -> new WaterSensitiveBlock(Properties.copy(Blocks.DIRT).requiresCorrectToolForDrops().harvestTool(ToolType.SHOVEL), MUD.get().defaultBlockState()));
	public static final RegistryObject<Block> CLAY = override("clay", () -> new Block(Properties.copy(Blocks.CLAY).requiresCorrectToolForDrops().harvestTool(ToolType.SHOVEL)));
	public static final RegistryObject<Block> RED_CLAY = R.register("red_clay", () -> new Block(Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().harvestTool(ToolType.SHOVEL).strength(0.6f).sound(SoundType.GRAVEL)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<SmallRockBlock> ROCK_ON_GROUND = R.register("rock_on_ground", () -> new SmallRockBlock(Properties.of(Material.STONE).instabreak()), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<LargeRockBlock> LARGE_ROCK_ON_GROUND = R.register("large_rock_on_ground", () -> new LargeRockBlock(Properties.of(Material.STONE).strength(1.0f)), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<Block> CHARCOAL_BLOCK = R.register("charcoal_block", () -> new FallingBlock(CHARCOAL_PROPERTIES), ItemGroup.TAB_BUILDING_BLOCKS);

	// Bricks
	public static final RegistryObject<Block> ADOBE_BRICKS = R.register("adobe_bricks", () -> new Block(Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().harvestTool(FTGUToolType.HAMMER).strength(2.0f, 6.0f)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<BrickChamberPortBlock> ADOBE_BRICK_PORT = R.register("adobe_brick_port", () -> new BrickChamberPortBlock(Properties.copy(ADOBE_BRICKS.get()), Chambers.ADOBE_BRICK), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<ChamberControllerBlock> ADOBE_BRICK_CONTROLLER = R.register("adobe_brick_controller", () -> new ChamberControllerBlock(Properties.copy(ADOBE_BRICKS.get()), Chambers.ADOBE_BRICK));
	public static final RegistryObject<Block> ADOBE_BRICK_SLAB = R.register("adobe_brick_slab", () -> new SlabBlock(Properties.copy(ADOBE_BRICKS.get())), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<SimpleStairsBlock> ADOBE_BRICK_STAIRS = R.register("adobe_brick_stairs", () -> new SimpleStairsBlock(ADOBE_BRICKS.get()), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> BRICKS = override("bricks", () -> new Block(Properties.copy(Blocks.BRICKS).requiresCorrectToolForDrops().harvestTool(FTGUToolType.HAMMER)));
	public static final RegistryObject<Block> WHITE_BRICKS = R.register("white_bricks", () -> new Block(Properties.of(Material.STONE, DyeColor.WHITE).requiresCorrectToolForDrops().harvestTool(FTGUToolType.HAMMER).strength(2.0f, 6.0f)), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> WHITE_BRICK_SLAB = R.register("white_brick_slab", () -> new SlabBlock(Properties.copy(WHITE_BRICKS.get())), ItemGroup.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<SimpleStairsBlock> WHITE_BRICK_STAIRS = R.register("white_brick_stairs", () -> new SimpleStairsBlock(WHITE_BRICKS.get()), ItemGroup.TAB_BUILDING_BLOCKS);

	// Creative
	public static final RegistryObject<CreativeFluidSourceBlock> CREATIVE_FLUID_SOURCE = R.register("creative_fluid_source", () -> new CreativeFluidSourceBlock(Properties.copy(Blocks.STONE)), ItemGroup.TAB_DECORATIONS);

	// Assembly
	public static final RegistryObject<FrameBlock> PRIMITIVE_FRAME = R.register("primitive_frame", () -> new FrameBlock(Properties.of(WEAK_MATERIAL, MaterialColor.COLOR_BROWN).sound(SoundType.SCAFFOLDING).noCollission()), ItemGroup.TAB_DECORATIONS);
	public static final RegistryObject<SingleBrickBlock> DRIED_ADOBE_BRICK = R.register("dried_adobe_brick", () -> new SingleBrickBlock(Properties.of(WEAK_MATERIAL, MaterialColor.TERRACOTTA_LIGHT_GRAY).instabreak()) {
		@Override
		public Item asItem() {
			return FTGUItems.ADOBE_BRICK.get();
		}
	});
	public static final RegistryObject<DryingBrickBlock> WET_COB_BRICK = R.register("wet_cob_brick", () -> new DryingBrickBlock(Properties.of(WEAK_MATERIAL, MaterialColor.DIRT).instabreak().sound(SoundType.GRAVEL), DRIED_ADOBE_BRICK.get().defaultBlockState()), ItemGroup.TAB_MATERIALS);
	public static final RegistryObject<AssemblingBlock> ASSEMBLING_ADOBE_BRICKS = R.register("assembling_adobe_bricks", () -> new AssemblingBlock(Properties.copy(ADOBE_BRICKS.get()).noCollission(), PRIMITIVE_FRAME.get().asItem()));
}