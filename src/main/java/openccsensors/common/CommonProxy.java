package openccsensors.common;

import java.io.File;
import java.io.IOException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.client.Minecraft;
import openccsensors.OpenCCSensors;
import openccsensors.common.block.BlockBasicSensor;
import openccsensors.common.block.BlockGauge;
import openccsensors.common.block.BlockSensor;
import openccsensors.common.item.ItemGeneric;
import openccsensors.common.item.ItemSensorCard;
import openccsensors.common.item.meta.ItemMetaAdvancedAmplifier;
import openccsensors.common.item.meta.ItemMetaRangeExtensionAntenna;
import openccsensors.common.item.meta.ItemMetaSignalAmplifier;
import openccsensors.common.sensor.*;
import openccsensors.common.tileentity.TileEntityGauge;
import openccsensors.common.turtle.TurtleUpgradeSensor;
import openccsensors.common.util.RecipeUtils;
import openccsensors.common.util.ResourceExtractingUtils;

public class CommonProxy {

	public void init() {

		initSensors();
		initBlocks();
		initItems();

		OpenCCSensors.turtleUpgradeSensor = new TurtleUpgradeSensor();

		if (OpenCCSensors.Config.turtlePeripheralEnabled) {
			ComputerCraftAPI.registerTurtleUpgrade(OpenCCSensors.turtleUpgradeSensor);
		}

		ComputerCraftAPI.registerPeripheralProvider(OpenCCSensors.Blocks.sensorBlock);

		NetworkRegistry.INSTANCE.registerGuiHandler(OpenCCSensors.instance, new GuiHandler());

		TileEntityGauge.addGaugeSensor(OpenCCSensors.Sensors.machineSensor);
		TileEntityGauge.addGaugeSensor(OpenCCSensors.Sensors.powerSensor);
		TileEntityGauge.addGaugeSensor(OpenCCSensors.Sensors.inventorySensor);
		TileEntityGauge.addGaugeSensor(OpenCCSensors.Sensors.tankSensor);

		RecipeUtils.addGaugeRecipe();
		RecipeUtils.addSensorRecipe();
		RecipeUtils.addProxSensorBlockRecipe();

		setupLuaFiles();
	}

	private void initSensors() {
		OpenCCSensors.Sensors.proximitySensor = new ProximitySensor();
		OpenCCSensors.Sensors.droppedItemSensor = new DroppedItemSensor();
		OpenCCSensors.Sensors.signSensor = new SignSensor();
		OpenCCSensors.Sensors.minecartSensor = new MinecartSensor();
		OpenCCSensors.Sensors.sonicSensor = new SonicSensor();
		OpenCCSensors.Sensors.tankSensor = new TankSensor();
		OpenCCSensors.Sensors.inventorySensor = new InventorySensor();
		OpenCCSensors.Sensors.worldSensor = new WorldSensor();
		OpenCCSensors.Sensors.powerSensor = new PowerSensor();
		OpenCCSensors.Sensors.machineSensor = new MachineSensor();
		OpenCCSensors.Sensors.magicSensor = new MagicSensor();
		OpenCCSensors.Sensors.cropSensor = new CropSensor();
	}

	private void initBlocks() {
		OpenCCSensors.Blocks.sensorBlock = new BlockSensor();
		OpenCCSensors.Blocks.gaugeBlock = new BlockGauge();
		OpenCCSensors.Blocks.basicSensorBlock = new BlockBasicSensor();
	}

	private void initItems() {

		// metas
		OpenCCSensors.Items.genericItem = new ItemGeneric();
		OpenCCSensors.Items.rangeExtensionAntenna = new ItemMetaRangeExtensionAntenna(1);
		OpenCCSensors.Items.signalAmplifier = new ItemMetaSignalAmplifier(2);
		OpenCCSensors.Items.advancedAmplifier = new ItemMetaAdvancedAmplifier(3);

		OpenCCSensors.Items.sensorCard = new ItemSensorCard();
		OpenCCSensors.Items.sensorCard.registerSensors();

		GameRegistry.registerItem(OpenCCSensors.Items.genericItem, "genericItem", "ocs");
		GameRegistry.registerItem(OpenCCSensors.Items.sensorCard, "sensorCard", "ocs");

	}


	public void registerRenderInformation() {

	}

	public File getBase() {
		if (FMLLaunchHandler.side().isClient()) {
			return Minecraft.getMinecraft().mcDataDir;
		} else {
			return new File(".");
		}
	}

	private boolean setupLuaFiles() {
		ModContainer container = FMLCommonHandler.instance().findContainerFor(OpenCCSensors.instance);
		File modFile = container.getSource();
		File baseFile = getBase();
		if (modFile.isDirectory()) {
			File srcFile = new File(modFile, OpenCCSensors.LUA_PATH);
			File destFile = new File(baseFile, OpenCCSensors.EXTRACTED_LUA_PATH);
			if (destFile.exists()) {
				return false;
			}
			try {
				ResourceExtractingUtils.copy(srcFile, destFile);
			} catch (IOException e) {
			}
		} else {
			File destFile = new File(OpenCCSensors.proxy.getBase(), OpenCCSensors.EXTRACTED_LUA_PATH);
			if (destFile.exists()) {
				return false;
			}
			ResourceExtractingUtils.extractZipToLocation(modFile, OpenCCSensors.LUA_PATH, OpenCCSensors.EXTRACTED_LUA_PATH);
		}
		return true;
	}

}
