package bluper.ftgu.world.chamber;

import javax.annotation.Nullable;

import bluper.ftgu.util.SaveDataUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Chamber {
	public static final int MAX_WIDTH = 15;
	public static final int MAX_HEIGHT = 31;

	public final BlockPos pos1;
	public final BlockPos pos2;
	private boolean hasRoof;

	public Chamber(BlockPos pos1, BlockPos pos2, boolean hasRoof) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.hasRoof = hasRoof;
	}

	public boolean hasRoof() {
		return hasRoof;
	}

	public int blockVolume() {
		return height() * area();
	}

	public int area() {
		return widthX() * widthZ();
	}

	public int fluidVolume() {
		return blockVolume() * 1000;
	}

	@Override
	public String toString() {
		return "Chamber:{hasRoof=" + hasRoof() + ",pos1=(" + pos1.toShortString() + "),pos2=(" + pos2.toShortString() + ")}";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Chamber)) return false;
		Chamber c = (Chamber) obj;
		return (c.hasRoof == hasRoof && c.pos1.equals(pos1) && c.pos2.equals(pos2));
	}

	public int height() {
		return pos2.getY() - pos1.getY() + 1;
	}

	public int widthX() {
		return pos2.getX() - pos1.getX() + 1;
	}

	public int widthZ() {
		return pos2.getZ() - pos1.getZ() + 1;
	}

	public static class Validator {
		private final ChamberType type;
		private final World world;
		private int maxXWidth = MAX_WIDTH;
		private int maxHeight = MAX_HEIGHT;
		private int maxZWidth = MAX_WIDTH;
		private boolean valid = true;
		private final BlockPos pos;
		private boolean hasRun = false;
		private boolean hasRoof = true;

		public Validator(ChamberType chamberType, World world, BlockPos pos) {
			this.type = chamberType;
			this.world = world;
			this.pos = pos;
		}

		public boolean run() {
			if (type == null) return false;
			hasRun = true;
			validateFloor();
			if (!valid) return false;
			validateUpwards();
			if (valid) validateRoof();
			return valid;
		}

		public boolean hasRun() {
			return hasRun;
		}

		public boolean valid() {
			return hasRun ? valid : false;
		}

		public Chamber makeChamber() {
			return new Chamber(pos, pos.offset(maxXWidth - 1, maxHeight - 1, maxZWidth - 1), hasRoof);
		}

		private void validateFloor() {
			for (int x = 0; x < maxXWidth; x++) {
				for (int z = 0; z < maxZWidth; z++) {
					BlockPos oPos = pos.offset(x, 0, z);
					valid = type.validateWall(world.getBlockState(oPos.below()), Direction.DOWN);
					if (!valid) return;
					if (type.validateWall(world.getBlockState(oPos.south()), Direction.SOUTH)) maxZWidth = z + 1;
					if (type.validateWall(world.getBlockState(oPos.east()), Direction.EAST)) maxXWidth = x + 1;
				}
			}
		}

		private void validateUpwards() {
			for (int x = 0; x < maxXWidth; x++) {
				for (int z = 0; z < maxZWidth; z++) {
					for (int y = 0; y < maxHeight; y++) {
						BlockPos oPos = pos.offset(x, y, z);
						if ((type.validateWall(world.getBlockState(oPos), Direction.UP) || ((x == 0 && !type.validateWall(world.getBlockState(oPos.west()), Direction.WEST)) || (x == maxXWidth - 1 && !type.validateWall(world.getBlockState(oPos.east()), Direction.EAST)) || (z == 0 && !type.validateWall(world.getBlockState(oPos.north()), Direction.NORTH)) || (z == maxZWidth - 1 && !type.validateWall(world.getBlockState(oPos.south()), Direction.SOUTH))))) maxHeight = y;
					}
				}
			}
		}

		private void validateRoof() {
			for (int x = 0; x < maxXWidth; x++) {
				for (int z = 0; z < maxZWidth; z++) {
					BlockPos oPos = pos.offset(x, maxHeight, z);
					hasRoof = type.validateWall(world.getBlockState(oPos), Direction.UP);
					if (!hasRoof) return;
				}
			}
		}
	}

	public CompoundNBT save() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("pos1", SaveDataUtils.blockPosToNBT(pos1));
		nbt.put("pos2", SaveDataUtils.blockPosToNBT(pos2));
		nbt.putBoolean("hasRoof", hasRoof);
		return nbt;
	}

	@Nullable
	public static Chamber load(CompoundNBT nbt) {
		BlockPos pos1c = SaveDataUtils.blockPosFromNBT(nbt.getCompound("pos1"));
		BlockPos pos2c = SaveDataUtils.blockPosFromNBT(nbt.getCompound("pos2"));
		if (!pos1c.equals(BlockPos.ZERO) && !pos2c.equals(BlockPos.ZERO)) {
			return new Chamber(pos1c, pos2c, nbt.getBoolean("hasRoof"));
		}
		else return null;
	}
}
