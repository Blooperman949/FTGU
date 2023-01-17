package bluper.ftgu.util;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SaveDataUtils {
	@Nullable
	public static TileEntity tileFromNBTPos(CompoundNBT nbt, World world) {
		return world.getBlockEntity(blockPosFromNBT(nbt));
	}

	public static CompoundNBT tilePosToNBT(TileEntity te) {
		return blockPosToNBT(te.getBlockPos());
	}

	public static BlockPos blockPosFromNBT(CompoundNBT nbt) {
		return new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
	}

	public static CompoundNBT blockPosToNBT(BlockPos pos) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("x", pos.getX());
		nbt.putInt("y", pos.getY());
		nbt.putInt("z", pos.getZ());
		return nbt;
	}
}
