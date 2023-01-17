package bluper.ftgu.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class FluidRenderer {
	private static final AtlasTexture BLOCKS = Minecraft.getInstance().getModelManager().getAtlas(PlayerContainer.BLOCK_ATLAS);

	public static void renderBox(MatrixStack ms, IVertexBuilder vb, FluidStack fluid, float x, float y, float z, float dx, float dy, float dz, int light, int faces) {
		FluidAttributes attr = fluid.getFluid().getAttributes();
		TextureAtlasSprite sprite = BLOCKS.getSprite(attr.getStillTexture());
		int color = attr.getColor();
		int a = color >> 24 & 0xFF;
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;
		int lum = attr.getLuminosity();
		int lu = light >> 0x10 & 0xFFFF;
		int lv = Math.max(light & 0xFFFF, lum << 4);
		ms.pushPose();
		ms.scale(0.999f, 0.999f, 0.999f);
		ms.translate(0.0005f, 0.0005f, 0.0005f);
		Matrix4f m4f = ms.last().pose();
		float u1 = sprite.getU0();
		float v1 = sprite.getV0();
		float u2 = sprite.getU1();
		float v2 = sprite.getV1();
		// shape format: two bits for each axis, 0bZZXXYY, 0b101010 -, 0b10101 +.
		if ((faces & 1) != 0) faceY(vb, m4f, x, dx, y, z, dz, u1, v1, u2, v2, r, g, b, a, lu, lv);
		if ((faces & 2) != 0) faceY(vb, m4f, x, dx, y + dy, z, dz, u1, v1, u2, v2, r, g, b, a, lu, lv);
		if ((faces & 4) != 0) faceX(vb, m4f, x, y, dy, z, dz, u1, v1, u2, v2, r, g, b, a, lu, lv);
		if ((faces & 8) != 0) faceX(vb, m4f, x + dx, y, dy, z, dz, u1, v1, u2, v2, r, g, b, a, lu, lv);
		if ((faces & 16) != 0) faceZ(vb, m4f, x, dx, y, dy, z, u1, v1, u2, v2, r, g, b, a, lu, lv);
		if ((faces & 32) != 0) faceZ(vb, m4f, x, dx, y, dy, z + dz, u1, v1, u2, v2, r, g, b, a, lu, lv);
		ms.popPose();
	}

	private static void faceX(IVertexBuilder vb, Matrix4f m4f, float x, float y, float dy, float z, float dz, float u1, float v1, float u2, float v2, int r, int g, int b, int a, int lu, int lv) {
		for (int iz = 0; iz < dz; iz++)
			for (int iy = 0; iy < dy; iy++)
				quadX(vb, m4f, 
						x,
						y + iy,
						y + Math.min(iy + 1, dy),
						z + iz,
						z + Math.min(iz + 1, dz),
						dz - iz >= 1 ? u1 : u1 + (u2 - u1) * (1 - (dz % 1)),
						dy - iy >= 1 ? v1 : v1 + (v2 - v1) * (1 - (dy % 1)),
						u2, v2,
						r, g, b, a, lu, lv);
	}

	private static void faceY(IVertexBuilder vb, Matrix4f m4f, float x, float dx, float y, float z, float dz, float u1, float v1, float u2, float v2, int r, int g, int b, int a, int lu, int lv) {
		for (int ix = 0; ix < dx; ix++)
			for (int iz = 0; iz < dz; iz++)
				quadY(vb, m4f,
						x + ix,
						x + Math.min(ix + 1, dx),
						y,
						z + iz,
						z + Math.min(iz + 1, dz),
						dx - ix >= 1 ? u1 : u1 + (u2 - u1) * (1 - (dx % 1)),
						dz - iz >= 1 ? v1 : v1 + (v2 - v1) * (1 - (dz % 1)),
						u2, v2,
						r, g, b, a, lu, lv);
	}

	private static void faceZ(IVertexBuilder vb, Matrix4f m4f, float x, float dx, float y, float dy, float z, float u1, float v1, float u2, float v2, int r, int g, int b, int a, int lu, int lv) {
		for (int ix = 0; ix < dx; ix++)
			for (int iy = 0; iy < dy; iy++)
				quadZ(vb, m4f,
						x + ix,
						x + Math.min(ix + 1, dx),
						y + iy,
						y + Math.min(iy + 1, dy),
						z,
						dx - ix >= 1 ? u1 : u1 + (u2 - u1) * (1 - (dx % 1)),
						dy - iy >= 1 ? v1 : v1 + (v2 - v1) * (1 - (dy % 1)),
						u2, v2,
						r, g, b, a, lu, lv);
	}

	private static void quadX(IVertexBuilder vb, Matrix4f m4f, float x, float y1, float y2, float z1, float z2, float u1, float v1, float u2, float v2, int r, int g, int b, int a, int lu, int lv) {
		vertex(vb, m4f, x, y1, z1, u1, v2, r, g, b, a, lu, lv);
		vertex(vb, m4f, x, y2, z1, u1, v1, r, g, b, a, lu, lv);
		vertex(vb, m4f, x, y2, z2, u2, v1, r, g, b, a, lu, lv);
		vertex(vb, m4f, x, y1, z2, u2, v2, r, g, b, a, lu, lv);
	}

	private static void quadY(IVertexBuilder vb, Matrix4f m4f, float x1, float x2, float y, float z1, float z2, float u1, float v1, float u2, float v2, int r, int g, int b, int a, int lu, int lv) {
		vertex(vb, m4f, x1, y, z1, u1, v1, r, g, b, a, lu, lv);
		vertex(vb, m4f, x2, y, z1, u2, v1, r, g, b, a, lu, lv);
		vertex(vb, m4f, x2, y, z2, u2, v2, r, g, b, a, lu, lv);
		vertex(vb, m4f, x1, y, z2, u1, v2, r, g, b, a, lu, lv);
	}

	private static void quadZ(IVertexBuilder vb, Matrix4f m4f, float x1, float x2, float y1, float y2, float z, float u1, float v1, float u2, float v2, int r, int g, int b, int a, int lu, int lv) {
		vertex(vb, m4f, x1, y1, z, u1, v1, r, g, b, a, lu, lv);
		vertex(vb, m4f, x2, y1, z, u2, v1, r, g, b, a, lu, lv);
		vertex(vb, m4f, x2, y2, z, u2, v2, r, g, b, a, lu, lv);
		vertex(vb, m4f, x1, y2, z, u1, v2, r, g, b, a, lu, lv);
	}

	private static void vertex(IVertexBuilder vb, Matrix4f m4f, float x, float y, float z, float u, float v, int r, int g, int b, int a, int lu, int lv) {
		vb.vertex(m4f, x, y, z).color(r, g, b, a).uv(u, v).uv2(lu, lv).endVertex();
	}
}
