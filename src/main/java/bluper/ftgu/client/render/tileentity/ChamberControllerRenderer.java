package bluper.ftgu.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bluper.ftgu.client.render.FTGURenderTypes;
import bluper.ftgu.client.render.FluidRenderer;
import bluper.ftgu.client.render.debug.FTGUDebug;
import bluper.ftgu.world.chamber.Chamber;
import bluper.ftgu.world.tileentity.ChamberControllerTileEntity;
import bluper.ftgu.world.tileentity.ChamberPortTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;

public class ChamberControllerRenderer extends TileEntityRenderer<ChamberControllerTileEntity> {

	public ChamberControllerRenderer(TileEntityRendererDispatcher disp) {
		super(disp);
	}

	@Override
	public void render(ChamberControllerTileEntity te, float partialTicks, MatrixStack ms, IRenderTypeBuffer buf, int light, int overlay) {
		Chamber chamber = te.getChamber();
		if (chamber != null) {
			BlockPos pos1 = chamber.pos1;
			BlockPos pos2 = chamber.pos2;
			float dx = pos2.getX() - pos1.getX() + 1;
			float dz = pos2.getZ() - pos1.getZ() + 1;
			IVertexBuilder vbl = buf.getBuffer(RenderType.lines());
			BlockPos cp = te.getBlockPos();
			if (FTGUDebug.debugModeOn) {
				float h = pos2.getY() - pos1.getY() + 1;
				WorldRenderer.renderLineBox(ms, vbl, 0, 1, 0, dx, h + 1, dz, 1, 1, 1, 1);
				boolean roof = te.getChamber().hasRoof();
				WorldRenderer.renderLineBox(ms, vbl, 0, h + 1, 0, dx, h + 2, dz, roof ? 0 : 1, roof ? 1 : 0, 0, 1);
				for (ChamberPortTileEntity p : te.ports) {
					BlockPos pp = p.getBlockPos();
					FTGUDebug.renderLine(ms, vbl, 0.5, 1, 0.5, pp.getX() - cp.getX() + 0.5, pp.getY() - cp.getY() + 0.5, pp.getZ() - cp.getZ() + 0.5, 0, 0, 1, 1);
				}
			}
			IVertexBuilder vb = buf.getBuffer(FTGURenderTypes.FLUID);
			int area = te.getChamber().area();
			int lightAbove = WorldRenderer.getLightColor(te.getLevel(), cp.above());
			te.getTank().forEachFluid((fs, lvl, i) -> {
				FluidRenderer.renderBox(ms, vb, fs.getFluid(), 0, (lvl / 1000f) / area + 1, 0, dx, (fs.getAmount() / 1000f) / area, dz, lightAbove, true, true, i == 0);
			});
		}
	}

	@Override
	public boolean shouldRenderOffScreen(ChamberControllerTileEntity te) {
		return true;
	}
}
