package bluper.ftgu.client.render.debug;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class FTGUDebug {
	public static boolean debugModeOn = false;
	private static Minecraft mc = Minecraft.getInstance();

	public static void renderLine(MatrixStack ms, IVertexBuilder vb, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a) {
		Matrix4f matrix4f = ms.last().pose();
		float fx1 = (float) x1;
		float fy1 = (float) y1;
		float fz1 = (float) z1;
		float fx2 = (float) x2;
		float fy2 = (float) y2;
		float fz2 = (float) z2;
		vb.vertex(matrix4f, fx1, fy1, fz1).color(r, g, b, a).endVertex();
		vb.vertex(matrix4f, fx2, fy2, fz2).color(r, g, b, a).endVertex();
	}

	public static void sendDebugNotif(String message) {
		mc.gui.getChat().addMessage((new StringTextComponent("")).append((new TranslationTextComponent("debug.prefix")).withStyle(new TextFormatting[]{TextFormatting.YELLOW, TextFormatting.BOLD})).append(" ").append(new TranslationTextComponent(message)));
	}

	public static void toggleDebugMode() {
		debugModeOn = !debugModeOn;
	}
}
