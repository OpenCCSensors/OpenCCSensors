package openccsensors.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import openccsensors.OpenCCSensors;
import openccsensors.api.IItemMeta;
import openccsensors.api.SensorCard;
import openccsensors.client.model.SmartItemModelSensorCard;
import openccsensors.client.renderer.tileentity.TileEntityGaugeRenderer;
import openccsensors.client.renderer.tileentity.TileEntitySensorRenderer;
import openccsensors.common.CommonProxy;
import openccsensors.common.tileentity.TileEntityGauge;
import openccsensors.common.tileentity.TileEntitySensor;

import javax.annotation.Nonnull;
import java.io.File;

public class ClientProxy extends CommonProxy {
	private final SmartItemModelSensorCard sensorModel = new SmartItemModelSensorCard();
	private final ResourceLocation sensorName = new ResourceLocation("openccsensors:sensor_card");
	private final ModelResourceLocation sensorModelName = new ModelResourceLocation(sensorName, "inventory");

	@Override
	public File getBase() {
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(this);

		IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		if (manager instanceof SimpleReloadableResourceManager) {
			((SimpleReloadableResourceManager) manager).registerReloadListener(sensorModel);
		}

		onModelRegister();
	}

	@Override
	public void init() {
		super.init();

		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntitySensor.class,
			new TileEntitySensorRenderer()
		);
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntityGauge.class,
			new TileEntityGaugeRenderer()
		);
	}

	@SubscribeEvent
	public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
		for (SensorCard card : OpenCCSensors.Items.sensorCard.getSensorCards()) {
			event.getMap().registerSprite(card.getSensor().getIcon());
			event.getMap().registerSprite(card.getTier().getIcon());
		}
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		event.getModelRegistry().putObject(sensorModelName, sensorModel);
	}

	private void onModelRegister() {
		registerItemModel(OpenCCSensors.Blocks.basicSensorBlock, 0);
		registerItemModel(OpenCCSensors.Blocks.gaugeBlock, 0);
		registerItemModel(OpenCCSensors.Blocks.sensorBlock, 0);

		ModelBakery.registerItemVariants(OpenCCSensors.Items.sensorCard, sensorName);
		ModelLoader.setCustomMeshDefinition(OpenCCSensors.Items.sensorCard, new ItemMeshDefinition() {
			@Nonnull
			@Override
			public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
				return sensorModelName;
			}
		});

		for (IItemMeta meta : OpenCCSensors.Items.genericItem.getMetas()) {
			ModelBakery.registerItemVariants(OpenCCSensors.Items.genericItem, meta.getModelName());
			ModelLoader.setCustomModelResourceLocation(OpenCCSensors.Items.genericItem, meta.getId(), new ModelResourceLocation(meta.getModelName(), "inventory"));
		}
	}

	private void registerItemModel(Block block, int damage) {
		registerItemModel(Item.getItemFromBlock(block), damage);
	}

	private void registerItemModel(Item item, int damage) {
		ModelBakery.registerItemVariants(item, item.getRegistryName());
		ModelLoader.setCustomModelResourceLocation(item, damage, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
