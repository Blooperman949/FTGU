package bluper.ftgu.client.render;

import java.util.OptionalDouble;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.Util;

@SuppressWarnings({ "deprecation", "resource" })
public class RenderConstants {
	public static final int GL_POINTS = 0x0;
	public static final int GL_LINES = 0x1;
	public static final int GL_LINE_LOOP = 0x2;
	public static final int GL_LINE_STRIP = 0x3;
	public static final int GL_TRIANGLES = 0x4;
	public static final int GL_TRIANGLE_STRIP = 0x5;
	public static final int GL_TRIANGLE_FAN = 0x6;
	public static final int GL_QUADS = 0x7;

	protected static final RenderState.TransparencyState NO_TRANSPARENCY = new RenderState.TransparencyState("no_transparency", () -> {
		RenderSystem.disableBlend();
	}, () -> {
	});
	protected static final RenderState.TransparencyState ADDITIVE_TRANSPARENCY = new RenderState.TransparencyState("additive_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderState.TransparencyState LIGHTNING_TRANSPARENCY = new RenderState.TransparencyState("lightning_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderState.TransparencyState GLINT_TRANSPARENCY = new RenderState.TransparencyState("glint_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderState.TransparencyState CRUMBLING_TRANSPARENCY = new RenderState.TransparencyState("crumbling_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderState.TransparencyState TRANSLUCENT_TRANSPARENCY = new RenderState.TransparencyState("translucent_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderState.AlphaState NO_ALPHA = new RenderState.AlphaState(0.0F);
	protected static final RenderState.AlphaState DEFAULT_ALPHA = new RenderState.AlphaState(0.003921569F);
	protected static final RenderState.AlphaState MIDWAY_ALPHA = new RenderState.AlphaState(0.5F);
	protected static final RenderState.ShadeModelState FLAT_SHADE = new RenderState.ShadeModelState(false);
	protected static final RenderState.ShadeModelState SMOOTH_SHADE = new RenderState.ShadeModelState(true);
	protected static final RenderState.TextureState BLOCK_SHEET_MIPPED = new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS, false, true);
	protected static final RenderState.TextureState BLOCK_SHEET = new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS, false, false);
	protected static final RenderState.TextureState NO_TEXTURE = new RenderState.TextureState();
	protected static final RenderState.TexturingState DEFAULT_TEXTURING = new RenderState.TexturingState("default_texturing", () -> {
	}, () -> {
	});
	protected static final RenderState.TexturingState OUTLINE_TEXTURING = new RenderState.TexturingState("outline_texturing", () -> {
		RenderSystem.setupOutline();
	}, () -> {
		RenderSystem.teardownOutline();
	});
	protected static final RenderState.TexturingState GLINT_TEXTURING = new RenderState.TexturingState("glint_texturing", () -> {
		setupGlintTexturing(8.0F);
	}, () -> {
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
	});
	protected static final RenderState.TexturingState ENTITY_GLINT_TEXTURING = new RenderState.TexturingState("entity_glint_texturing", () -> {
		setupGlintTexturing(0.16F);
	}, () -> {
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
	});
	protected static final RenderState.LightmapState LIGHTMAP = new RenderState.LightmapState(true);
	protected static final RenderState.LightmapState NO_LIGHTMAP = new RenderState.LightmapState(false);
	protected static final RenderState.OverlayState OVERLAY = new RenderState.OverlayState(true);
	protected static final RenderState.OverlayState NO_OVERLAY = new RenderState.OverlayState(false);
	protected static final RenderState.DiffuseLightingState DIFFUSE_LIGHTING = new RenderState.DiffuseLightingState(true);
	protected static final RenderState.DiffuseLightingState NO_DIFFUSE_LIGHTING = new RenderState.DiffuseLightingState(false);
	protected static final RenderState.CullState CULL = new RenderState.CullState(true);
	protected static final RenderState.CullState NO_CULL = new RenderState.CullState(false);
	protected static final RenderState.DepthTestState NO_DEPTH_TEST = new RenderState.DepthTestState("always", 519);
	protected static final RenderState.DepthTestState EQUAL_DEPTH_TEST = new RenderState.DepthTestState("==", 514);
	protected static final RenderState.DepthTestState LEQUAL_DEPTH_TEST = new RenderState.DepthTestState("<=", 515);
	protected static final RenderState.WriteMaskState COLOR_DEPTH_WRITE = new RenderState.WriteMaskState(true, true);
	protected static final RenderState.WriteMaskState COLOR_WRITE = new RenderState.WriteMaskState(true, false);
	protected static final RenderState.WriteMaskState DEPTH_WRITE = new RenderState.WriteMaskState(false, true);
	protected static final RenderState.LayerState NO_LAYERING = new RenderState.LayerState("no_layering", () -> {
	}, () -> {
	});
	protected static final RenderState.LayerState POLYGON_OFFSET_LAYERING = new RenderState.LayerState("polygon_offset_layering", () -> {
		RenderSystem.polygonOffset(-1.0F, -10.0F);
		RenderSystem.enablePolygonOffset();
	}, () -> {
		RenderSystem.polygonOffset(0.0F, 0.0F);
		RenderSystem.disablePolygonOffset();
	});
	protected static final RenderState.LayerState VIEW_OFFSET_Z_LAYERING = new RenderState.LayerState("view_offset_z_layering", () -> {
		RenderSystem.pushMatrix();
		RenderSystem.scalef(0.99975586F, 0.99975586F, 0.99975586F);
	}, RenderSystem::popMatrix);
	protected static final RenderState.FogState NO_FOG = new RenderState.FogState("no_fog", () -> {
	}, () -> {
	});
	protected static final RenderState.FogState FOG = new RenderState.FogState("fog", () -> {
		FogRenderer.levelFogColor();
		RenderSystem.enableFog();
	}, () -> {
		RenderSystem.disableFog();
	});
	protected static final RenderState.FogState BLACK_FOG = new RenderState.FogState("black_fog", () -> {
		RenderSystem.fog(2918, 0.0F, 0.0F, 0.0F, 1.0F);
		RenderSystem.enableFog();
	}, () -> {
		FogRenderer.levelFogColor();
		RenderSystem.disableFog();
	});
	protected static final RenderState.TargetState MAIN_TARGET = new RenderState.TargetState("main_target", () -> {
	}, () -> {
	});
	protected static final RenderState.TargetState OUTLINE_TARGET = new RenderState.TargetState("outline_target", () -> {
		Minecraft.getInstance().levelRenderer.entityTarget().bindWrite(false);
	}, () -> {
		Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
	});
	protected static final RenderState.TargetState TRANSLUCENT_TARGET = new RenderState.TargetState("translucent_target", () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().levelRenderer.getTranslucentTarget().bindWrite(false);
		}

	}, () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
		}

	});
	protected static final RenderState.TargetState PARTICLES_TARGET = new RenderState.TargetState("particles_target", () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().levelRenderer.getParticlesTarget().bindWrite(false);
		}

	}, () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
		}

	});
	protected static final RenderState.TargetState WEATHER_TARGET = new RenderState.TargetState("weather_target", () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().levelRenderer.getWeatherTarget().bindWrite(false);
		}

	}, () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
		}

	});
	protected static final RenderState.TargetState CLOUDS_TARGET = new RenderState.TargetState("clouds_target", () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().levelRenderer.getCloudsTarget().bindWrite(false);
		}

	}, () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
		}

	});
	protected static final RenderState.TargetState ITEM_ENTITY_TARGET = new RenderState.TargetState("item_entity_target", () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().levelRenderer.getItemEntityTarget().bindWrite(false);
		}

	}, () -> {
		if (Minecraft.useShaderTransparency()) {
			Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
		}

	});
	protected static final RenderState.LineState DEFAULT_LINE = new RenderState.LineState(OptionalDouble.of(1.0D));

	private static void setupGlintTexturing(float p_228548_0_) {
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		long i = Util.getMillis() * 8L;
		float f = (float) (i % 110000L) / 110000.0F;
		float f1 = (float) (i % 30000L) / 30000.0F;
		RenderSystem.translatef(-f, f1, 0.0F);
		RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
		RenderSystem.scalef(p_228548_0_, p_228548_0_, p_228548_0_);
		RenderSystem.matrixMode(5888);
	}
}
