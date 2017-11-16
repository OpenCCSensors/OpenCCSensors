package openccsensors.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import openccsensors.common.tileentity.TileEntityGauge;

public class TileEntityGaugeRenderer extends TileEntitySpecialRenderer<TileEntityGauge> {
	@Override
	public void renderTileEntityAt(TileEntityGauge gauge, double x, double y, double z, float partialTick, int destroyStage) {
		GlStateManager.pushMatrix();
		float facing = gauge.getFacing().getOpposite().getHorizontalAngle();
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		GlStateManager.rotate(-facing, 0.0F, 1.0F, 0.0F);

		GlStateManager.translate(0.0F, 0.1F, -0.37F); // -0.5 + 2/16 and rounded a little to prevent z fighting. 
		GlStateManager.scale(0.02F, 0.02F, 0.02F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.depthMask(false);

		FontRenderer fontRenderer = getFontRenderer();
		if (fontRenderer != null) {
			String stringPercentage = gauge.getPercentage() + "%";
			fontRenderer.drawString(stringPercentage, -fontRenderer.getStringWidth(stringPercentage) / 2, 0, 0xFFFFFF);
		}

		GlStateManager.depthMask(true);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.popMatrix();
	}

}
