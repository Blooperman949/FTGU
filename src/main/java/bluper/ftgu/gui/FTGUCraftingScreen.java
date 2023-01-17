package bluper.ftgu.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import bluper.ftgu.FTGU;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;

public class FTGUCraftingScreen extends ContainerScreen<FTGUCraftingContainer> {
	private ResourceLocation background;

	public FTGUCraftingScreen(FTGUCraftingContainer container, PlayerInventory inventory) {
		super(container, inventory, FTGUCraftingContainer.TITLE);
		this.background = new ResourceLocation(container.getSurface().is(Blocks.AIR) ? "minecraft" : FTGU.MODID, "textures/gui/container/crafting_table.png");
	}

	@Override
	public void render(MatrixStack ms, int x, int y, float f) {
		this.renderBackground(ms);
		this.renderBg(ms, f, x, y);
		super.render(ms, x, y, f);
		this.renderTooltip(ms, x, y);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(background);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
}
