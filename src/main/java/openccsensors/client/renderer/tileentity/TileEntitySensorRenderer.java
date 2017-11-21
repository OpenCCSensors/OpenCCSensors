package openccsensors.client.renderer.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import openccsensors.client.ClientProxy;
import openccsensors.common.item.ItemSensorCard;
import openccsensors.common.tileentity.TileEntitySensor;
import org.lwjgl.opengl.GL11;

public class TileEntitySensorRenderer extends TileEntitySpecialRenderer<TileEntitySensor> {
	@Override
	public void renderTileEntityAt(TileEntitySensor sensor, double x, double y, double z, float partialTick, int destroyStage) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);

		{
			int rotation = (int) sensor.getRotation();

			GlStateManager.pushMatrix();
			GlStateManager.rotate((float) (rotation % 360), 0.0f, 1.0f, 0.0f);
			GlStateManager.translate(-0.5f, -0.5f, -0.5f);
			GlStateManager.translate(-sensor.getPos().getX(), -sensor.getPos().getY(), -sensor.getPos().getZ());

			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			IBakedModel model = dispatcher.getBlockModelShapes().getModelManager().getModel(ClientProxy.SENSOR_DISH_MODEL);

			RenderHelper.disableStandardItemLighting();
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() ? GL11.GL_SMOOTH : GL11.GL_FLAT);

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

			IBlockState state = sensor.getWorld().getBlockState(sensor.getPos());
			dispatcher.getBlockModelRenderer().renderModel(sensor.getWorld(), model, state, sensor.getPos(), buffer, true);
			tessellator.draw();

			RenderHelper.enableStandardItemLighting();
			GlStateManager.popMatrix();
		}

		{
			ItemStack stack = sensor.getSensorCardStack();

			if (stack != null && stack.getItem() instanceof ItemSensorCard) {
				GlStateManager.enableRescaleNormal();
				GlStateManager.alphaFunc(516, 0.1F);
				GlStateManager.enableBlend();
				RenderHelper.enableStandardItemLighting();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

				float placing = sensor.getFacing().getHorizontalAngle();
				GlStateManager.rotate(placing, 0, 1, 0);
				GlStateManager.translate(
					0.0f,
					// Translate to bottom, up 4 pixels and then half a item pixel
					-0.5f + (4 / 16.0f) + (0.2f * 0.5f / 16.0f),
					// Translate to edge, back 2 pixels and then 7.5 item pixels
					-0.5f + (2 / 16.0f) + (0.2f * 7.5f / 16.0f)
				);
				GlStateManager.scale(0.2f, 0.2f, 0.2f);
				GlStateManager.rotate(90.0f, 1, 0, 0);

				RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
				IBakedModel model = renderItem.getItemModelWithOverrides(stack, getWorld(), null);
				renderItem.renderItem(stack, model);

				GlStateManager.disableRescaleNormal();
				GlStateManager.disableBlend();
			}
		}

		GlStateManager.popMatrix();
	}
}
