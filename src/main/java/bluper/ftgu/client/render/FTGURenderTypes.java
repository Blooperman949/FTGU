package bluper.ftgu.client.render;

import bluper.ftgu.FTGU;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class FTGURenderTypes {
	public static final RenderType FLUID = RenderType.create(FTGU.MODID + ":fluid",
			DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP,
			RenderConstants.GL_QUADS, 256, true, false,
			RenderType.State.builder()
			.setShadeModelState(RenderConstants.SMOOTH_SHADE)
			.setLightmapState(RenderConstants.LIGHTMAP)
			.setTextureState(RenderConstants.BLOCK_SHEET_MIPPED)
			.setTransparencyState(RenderConstants.TRANSLUCENT_TRANSPARENCY)
			.setCullState(RenderConstants.NO_CULL)
			.createCompositeState(false));
}
