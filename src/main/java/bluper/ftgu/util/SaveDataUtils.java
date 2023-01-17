package bluper.ftgu.util;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Some static helper methods for saving and loading {@code TileEntity} data.
 * @author Bluperman949
 */
public class SaveDataUtils {
	/**
	 * @param nbt an NBT object containing the "x", "y", and "z" values of a {@code BlockPos}
	 * @param world the {@link World} to search for a {@code TileEntity}
	 * @return the {@link TileEntity} at the given position. Returns {@code null} if it does not exist.
	 */
	@Nullable
	public static TileEntity tileFromNBTPos(CompoundNBT nbt, World world) {
		return world.getBlockEntity(blockPosFromNBT(nbt));
	}

	/**
	 * Stores a {@link TileEntity}'s position to new NBT object
	 * @param te the {@code TileEntity} whose position will be stored
	 * @return a new {@link CompoundNBT} containing the "x", "y", and "z" values of the given {@code TileEntity}'s {@code BlockPos}
	 */
	public static CompoundNBT tilePosToNBT(TileEntity te) {
		return blockPosToNBT(te.getBlockPos());
	}

	/**
	 * @param nbt a {@link CompoundNBT} containing the "x", "y", and "z" values of a {@link BlockPos}
	 * @return a new {@code BlockPos}. Missing NBT values are interpreted as 0.
	 */
	public static BlockPos blockPosFromNBT(CompoundNBT nbt) {
		return new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
	}

	/**
	 * @param pos a {@link BlockPos}
	 * @return a new {@link CompoundNBT} containing the "x", "y", and "z" values of the given {@code BlockPos}
	 */
	public static CompoundNBT blockPosToNBT(BlockPos pos) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("x", pos.getX());
		nbt.putInt("y", pos.getY());
		nbt.putInt("z", pos.getZ());
		return nbt;
	}
}
