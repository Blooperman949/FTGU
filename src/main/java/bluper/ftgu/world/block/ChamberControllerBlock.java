package bluper.ftgu.world.block;

import java.util.function.Supplier;

import bluper.ftgu.registry.registries.FTGUTiles;
import bluper.ftgu.world.chamber.ChamberType;
import bluper.ftgu.world.tileentity.ChamberControllerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.world.IBlockReader;

public class ChamberControllerBlock extends Block {
	private final Supplier<ChamberType> type;

	public ChamberControllerBlock(Properties properties, Supplier<ChamberType> type) {
		super(properties);
		FTGUTiles.VALID_CHAMBER_CONTROLLERS.add(this);
		this.type = type;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ChamberControllerTileEntity();
	}

	@Override
	public Item asItem() {
		return type.get().getMainBlock().asItem();
	}

	@Override
	public IFormattableTextComponent getName() {
		return type.get().getMainBlock().getName();
	}

	public ChamberType getChamberType() {
		return type.get();
	}
}
