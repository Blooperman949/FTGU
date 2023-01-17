package bluper.ftgu.registry.registries;

import bluper.ftgu.FTGU;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FTGUEntities {
	public static final DeferredRegister<EntityType<?>> R = DeferredRegister.create(ForgeRegistries.ENTITIES, FTGU.MODID);
}