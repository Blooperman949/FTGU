package bluper.ftgu.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class DamagingSnowballEntity extends SnowballEntity {
	private final float damage;

	public DamagingSnowballEntity(World world, LivingEntity thrower, float damage) {
		super(world, thrower);
		this.damage = damage;
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult rt) {
		Entity entity = rt.getEntity();
		entity.hurt(DamageSource.thrown(this, this.getOwner()), damage);
	}
}
