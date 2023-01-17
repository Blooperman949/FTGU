package bluper.ftgu.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A temperature stored in degrees Kelvin.
 * Contains some constants for reference.
 * @author Bluperman949
 */
public class Temperature {
	public static final Temperature WATER_BOIL = new Temperature(373.1f);
	public static final Temperature WATER_FREEZE = new Temperature(273f);

	private float k;

	/**
	 * @param k a temperature in degrees Kelvin
	 */
	public Temperature(float k) {
		this.k = k;
	}

	/**
	 * @return the {@code Temperature}'s value in degrees Kelvin
	 */
	public float k() {
		return k;
	}

	/**
	 * @return the {@code Temperature}'s value in degrees Celsius
	 */
	public float c() {
		return k - 273.15f;
	}

	/**
	 * @return the {@code Temperature}'s value in degrees Fahrenheit
	 */
	public float f() {
		return c() * (9f / 5) + 32;
	}

	/**
	 * @param system the desired system of measurement as a {@link Temperature.MeasurementSystem}
	 * @return the {@code Temperature}'s value in the given system of measurement
	 */
	public float get(MeasurementSystem system) {
		switch (system) {
		case CELSIUS:
			return c();
		case FAHRENHEIT:
			return f();
		default:
			return k;
		}
	}

	/**
	 * Enum indicators for temperature measurement systems.
	 */
	public static enum MeasurementSystem {
		KELVIN,
		CELSIUS,
		FAHRENHEIT
	}

	/**
	 * @param world a {@link World}
	 * @param pos a {@link BlockPos}
	 * @return an estimate of the temperature at the given {@code BlockPos} in the given {@code World}
	 */
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
