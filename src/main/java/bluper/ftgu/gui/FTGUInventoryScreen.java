package bluper.ftgu.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;

public class FTGUInventoryScreen extends DisplayEffectsScreen<FTGUPlayerContainer> {
	private float xMouse;
	private float yMouse;
	private boolean widthTooNarrow;
	private boolean buttonClicked;

	public FTGUInventoryScreen(FTGUPlayerContainer container, PlayerInventory inventory) {
		super(container, inventory, new TranslationTextComponent("container.inventory"));
		this.passEvents = true;
		this.titleLabelX = 97;
	}

	public void tick() {
		if (this.minecraft.gameMode.hasInfiniteItems())
			this.minecraft.setScreen(new CreativeScreen(this.minecraft.player));
	}

	protected void init() {
		if (this.minecraft.gameMode.hasInfiniteItems())
			this.minecraft.setScreen(new CreativeScreen(this.minecraft.player));
		else {
			super.init();
			this.widthTooNarrow = this.width < 379;
		}
	}

	protected void renderLabels(MatrixStack ms, int mx, int my) {
	}

	public void render(MatrixStack ms, int mx, int my, float f) {
		this.renderBackground(ms);
		if (this.widthTooNarrow) {
			this.renderBg(ms, f, mx, my);
		} else {
			super.render(ms, mx, my, f);
		}

		this.renderTooltip(ms, mx, my);
		this.xMouse = (float) mx;
		this.yMouse = (float) my;
	}

	@SuppressWarnings("deprecation")
	protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(INVENTORY_LOCATION);
		int i = this.leftPos;
		int j = this.topPos;
		this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight);
		renderEntityInInventory(i + 51, j + 75, 30, (float) (i + 51) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, this.minecraft.player);
	}

	@SuppressWarnings("deprecation")
	public static void renderEntityInInventory(int x, int y, int s, float yRotation, float xRotation, LivingEntity player) {
		float yr = (float) Math.atan((double) (yRotation / 40.0F));
		float xr = (float) Math.atan((double) (xRotation / 40.0F));
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float) x, (float) y, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack ms = new MatrixStack();
		ms.translate(0.0D, 0.0D, 1000.0D);
		ms.scale((float) s, (float) s, (float) s);
		Quaternion zQuat = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion xQuat = Vector3f.XP.rotationDegrees(xr * 20.0F);
		zQuat.mul(xQuat);
		ms.mulPose(zQuat);
		float f2 = player.yBodyRot;
		float f3 = player.yRot;
		float f4 = player.xRot;
		float f5 = player.yHeadRotO;
		float f6 = player.yHeadRot;
		player.yBodyRot = 180.0F + yr * 20.0F;
		player.yRot = 180.0F + yr * 40.0F;
		player.xRot = -xr * 20.0F;
		player.yHeadRot = player.yRot;
		player.yHeadRotO = player.yRot;
		EntityRendererManager rm = Minecraft.getInstance().getEntityRenderDispatcher();
		xQuat.conj();
		rm.overrideCameraOrientation(xQuat);
		rm.setRenderShadow(false);
		IRenderTypeBuffer.Impl buf = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> {
			rm.render(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, ms, buf, 15728880);
		});
		buf.endBatch();
		rm.setRenderShadow(true);
		player.yBodyRot = f2;
		player.yRot = f3;
		player.xRot = f4;
		player.yHeadRotO = f5;
		player.yHeadRot = f6;
		RenderSystem.popMatrix();
	}

	protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_, double p_195359_7_) {
		return (!this.widthTooNarrow) && super.isHovering(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
	}

	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		return this.widthTooNarrow ? false : super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}

	public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
		if (this.buttonClicked) {
			this.buttonClicked = false;
			return true;
		} else
			return super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
	}

	protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_) {
		return p_195361_1_ < (double) p_195361_5_ || p_195361_3_ < (double) p_195361_6_ || p_195361_1_ >= (double) (p_195361_5_ + this.imageWidth) || p_195361_3_ >= (double) (p_195361_6_ + this.imageHeight);
	}
}
