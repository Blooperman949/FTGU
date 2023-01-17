package bluper.ftgu.client;

import bluper.ftgu.world.item.FTGUBowItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;

public class FTGUItemModelsProperties {
	public static void makeBow(FTGUBowItem item) {
		ItemModelsProperties.register(item, new ResourceLocation("pull"), (itemStack, world, entity) -> {
			if (entity == null)
				return 0.0F;
			else
				return entity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration() - entity.getUseItemRemainingTicks()) / item.getDrawTime();
		});
		ItemModelsProperties.register(item, new ResourceLocation("pulling"), (itemStack, world, entity) -> {
			return entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F;
		});
	}
}
