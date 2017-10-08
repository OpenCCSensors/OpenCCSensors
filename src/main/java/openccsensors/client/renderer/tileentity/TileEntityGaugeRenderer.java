package openccsensors.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import openccsensors.client.model.ModelGauge;
import openccsensors.common.tileentity.TileEntityGauge;
import org.lwjgl.opengl.GL11;

public class TileEntityGaugeRenderer extends TileEntitySpecialRenderer<TileEntityGauge> {

	private ModelGauge modelGauge = new ModelGauge();

	@Override
	public void renderTileEntityAt(TileEntityGauge gauge, double x, double y, double z, float partialTick, int destroyStage) {
		GL11.glPushMatrix();
		float facing;
		switch (gauge.getFacing()) {
			default:
			case SOUTH:
				facing = 0.0f;
				break;
			case NORTH:
				facing = 180.0f;
				break;
			case WEST:
				facing = 90.0f;
				break;
			case EAST:
				facing = -90.0f;
				break;
		}

		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		GL11.glRotatef(-facing, 0.0F, 1.0F, 0.0F);
		this.bindTexture(new ResourceLocation("openccsensors", "textures/models/gauge.png"));
		GL11.glPushMatrix();
		this.modelGauge.render();
		GL11.glPopMatrix();

		FontRenderer fontRenderer = this.getFontRenderer();
		GL11.glTranslatef(0.0F, 0.1F, -0.43F);
		GL11.glScalef(0.02F, 0.02F, 0.02F);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glDepthMask(false);

		if (fontRenderer != null) {
			String stringPercentage = gauge.getPercentage() + "%";
			fontRenderer.drawString(stringPercentage,
				-fontRenderer.getStringWidth(stringPercentage) / 2, 0,
				16777215);
		}

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glPopMatrix();
	}

}
