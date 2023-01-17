package bluper.ftgu.world.item;

import com.google.common.collect.ImmutableSet;

import bluper.ftgu.registry.FTGUToolType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ToolItem;

public class HammerItem extends ToolItem {
	public HammerItem(IItemTier tier, float damageBoost, float speed, Properties properties) {
		super(damageBoost, speed, tier, ImmutableSet.of(), properties.addToolType(FTGUToolType.HAMMER, tier.getLevel()));
	}
}
