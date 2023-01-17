package bluper.ftgu.registry.registries;

import bluper.ftgu.FTGU;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FTGUSounds {
	public static final DeferredRegister<SoundEvent> R = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FTGU.MODID);

	private static RegistryObject<SoundEvent> register(String name) {
		ResourceLocation rl = new ResourceLocation(FTGU.MODID, name);
		SoundEvent sound = new SoundEvent(rl);
		return R.register(name, () -> sound);
	}

	public static final RegistryObject<SoundEvent> CHISEL = register("chisel");
}