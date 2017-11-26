package openccsensors.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import openccsensors.OpenCCSensors;
import openccsensors.api.IItemMeta;
import openccsensors.client.model.ModelSensorCard;
import openccsensors.client.renderer.tileentity.TileEntityGaugeRenderer;
import openccsensors.client.renderer.tileentity.TileEntitySensorRenderer;
import openccsensors.common.CommonProxy;
import openccsensors.common.tileentity.TileEntityGauge;
import openccsensors.common.tileentity.TileEntitySensor;

import javax.annotation.Nonnull;
import java.io.File;

public class ClientProxy extends CommonProxy {
	private static final ResourceLocation SENSOR_DISH_RESOURCE = new ResourceLocation("openccsensors", "block/sensor_dish");
	public static final ModelResourceLocation SENSOR_DISH_MODEL = new ModelResourceLocation(new ResourceLocation("openccsensors", "sensor_dish"), "inventory");

	@Override
	public File getBase() {
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(this);

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
	public void onModelBakeEvent(ModelBakeEvent event) {
		event.getModelRegistry().putObject(SENSOR_DISH_MODEL, bakeModel(SENSOR_DISH_RESOURCE));
	}

	private IBakedModel bakeModel(ResourceLocation location) {
		return ModelLoaderRegistry
			.getModelOrMissing(location)
			.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
	}

	private void onModelRegister() {
		registerItemModel(OpenCCSensors.Blocks.basicSensorBlock, 0);
		registerItemModel(OpenCCSensors.Blocks.gaugeBlock, 0);
		registerItemModel(OpenCCSensors.Blocks.sensorBlock, 0);

		ModelLoaderRegistry.registerLoader(ModelSensorCard.INSTANCE);
		ModelBakery.registerItemVariants(OpenCCSensors.Items.sensorCard, ModelSensorCard.MODEL_LOCATION);
		ModelLoader.setCustomMeshDefinition(OpenCCSensors.Items.sensorCard, new ItemMeshDefinition() {
			@Nonnull
			@Override
			public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
				return ModelSensorCard.MODEL_LOCATION;
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
