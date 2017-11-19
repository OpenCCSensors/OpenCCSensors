package openccsensors.client.renderer.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import openccsensors.client.ClientProxy;
import openccsensors.common.item.ItemSensorCard;
import openccsensors.common.tileentity.TileEntitySensor;
import org.lwjgl.opengl.GL11;

public class TileEntitySensorRenderer extends TileEntitySpecialRenderer<TileEntitySensor> {
	private ModelManager manager;

	public ModelManager getManager() {
		ModelManager manager = this.manager;
		if (manager == null) {
			manager = this.manager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();
		}

		return manager;
	}

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

			IBakedModel model = getManager().getModel(ClientProxy.SENSOR_DISH_MODEL);

			RenderHelper.disableStandardItemLighting();
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() ? GL11.GL_SMOOTH : GL11.GL_FLAT);

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			IBlockState state = sensor.getWorld().getBlockState(sensor.getPos());
			dispatcher.getBlockModelRenderer().renderModel(sensor.getWorld(), model, state, sensor.getPos(), buffer, true);
			tessellator.draw();

			RenderHelper.enableStandardItemLighting();
			GlStateManager.popMatrix();
		}

		{
			ItemStack sensorCardStack = sensor.getSensorCardStack();
			float placing = sensor.getFacing().getHorizontalAngle();

			GlStateManager.rotate(placing, 0, 1, 0);
			if (sensorCardStack != null && sensorCardStack.getItem() instanceof ItemSensorCard) {

				GlStateManager.scale(0.02f, 0.02f, 0.02f);
				GlStateManager.rotate(90.0F, 1, 0, 0);
				GlStateManager.translate(-8.0f, 4.0f, 12.0f);
				Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(sensorCardStack, 0, 0);
			}
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}
