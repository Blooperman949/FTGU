package bluper.ftgu.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bluper.ftgu.client.render.debug.FTGUDebug;
import bluper.ftgu.world.tileentity.ChamberPortTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;

public class ChamberPortRenderer extends TileEntityRenderer<ChamberPortTileEntity> {

	public ChamberPortRenderer(TileEntityRendererDispatcher disp) {
		super(disp);
	}

	@Override
	public void render(ChamberPortTileEntity te, float partialTicks, MatrixStack ms, IRenderTypeBuffer buf, int light, int overlay) {
		BlockPos e = te.getEyes();
		if (FTGUDebug.debugModeOn && te.getController() == null && e != null) {
			BlockPos p = te.getBlockPos();
			IVertexBuilder vb = buf.getBuffer(RenderType.lines());
			ms.pushPose();
			ms.translate(-p.getX(), -p.getY(), -p.getZ());
			FTGUDebug.renderLine(ms, vb, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5, e.getX() + 0.5, e.getY() + 0.5, e.getZ() + 0.5, 1, 0, 0, 1);
			WorldRenderer.renderLineBox(ms, vb, e.getX(), e.getY(), e.getZ(), e.getX() + 1, e.getY() + 1, e.getZ() + 1, 1, 0, 0, 1);
			ms.popPose();
		}
	}
}
