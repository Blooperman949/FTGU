package bluper.ftgu.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Temperature {
	public static final Temperature WATER_BOIL = new Temperature(373.1f);
	public static final Temperature WATER_FREEZE = new Temperature(273f);

	private float k;

	public Temperature(float k) {
		this.k = k;
	}

	public float k() {
		return k;
	}

	public float c() {
		return k - 273.15f;
	}

	public float f() {
		return c() * (9f / 5) + 32;
	}

	public float get(Type t) {
		switch (t) {
		case C:
			return c();
		case F:
			return f();
		default:
			return k;
		}
	}

	public static enum Type {
		K,
		C,
		F
	}

	public static Temperature worldTemp(World world, BlockPos pos) {
		float temp = world.getBiome(pos).getTemperature(pos);
		temp = temp * 39 + 277;
		if (world.isRainingAt(pos))
			temp -= 5;
		long time = world.dayTime();
		float dayTemp = ((time - 6000) % 12000) / 1200;
		if (time > 6000 && time < 18000)
			dayTemp = -dayTemp + 10;
		return new Temperature(temp + dayTemp);
	}
}
