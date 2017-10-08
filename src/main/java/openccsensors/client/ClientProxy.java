package openccsensors.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import openccsensors.client.renderer.tileentity.TileEntityGaugeRenderer;
import openccsensors.client.renderer.tileentity.TileEntitySensorRenderer;
import openccsensors.common.CommonProxy;
import openccsensors.common.tileentity.TileEntityGauge;
import openccsensors.common.tileentity.TileEntitySensor;

import java.io.File;

public class ClientProxy extends CommonProxy {

	@Override
	public File getBase() {
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public void registerRenderInformation() {
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntitySensor.class,
			new TileEntitySensorRenderer()
		);
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntityGauge.class,
			new TileEntityGaugeRenderer()
		);
	}
}
