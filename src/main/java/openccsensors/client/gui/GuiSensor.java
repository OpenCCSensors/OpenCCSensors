package openccsensors.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import openccsensors.common.container.ContainerSensor;
import openccsensors.common.util.TranslateUtils;
import org.lwjgl.opengl.GL11;

public class GuiSensor extends GuiContainer {

	public GuiSensor(InventoryPlayer inventory, TileEntity sensor) {
		super(new ContainerSensor(inventory, sensor));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("openccsensors", "textures/gui/sensor.png"));
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String sensorGuiName = TranslateUtils.translateToLocal("gui.openccsensors.sensor");

		mc.fontRendererObj.drawString(
			sensorGuiName,
			this.xSize / 2
				- (mc.fontRendererObj.getStringWidth(sensorGuiName) / 2),
			6,
			4210752
		);
		mc.fontRendererObj.drawString(
			TranslateUtils.translateToLocal("container.inventory"),
			8,
			this.ySize - 96 + 2,
			4210752
		);
	}

}
