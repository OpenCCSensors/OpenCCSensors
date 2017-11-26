package openccsensors.client.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.IPerspectiveAwareModel.MapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import openccsensors.OpenCCSensors;
import openccsensors.api.SensorCard;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelSensorCard implements IModel, ICustomModelLoader {
	public static final ModelSensorCard INSTANCE = new ModelSensorCard();

	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(new ResourceLocation("openccsensors", "sensor_card"), "inventory");

	private ModelSensorCard() {
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		Set<ResourceLocation> locations = new HashSet<ResourceLocation>();
		for (SensorCard card : OpenCCSensors.Items.sensorCard.getSensorCards()) {
			locations.add(card.getSensor().getIcon());
			locations.add(card.getTier().getIcon());
		}
		return locations;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		return bake(null, state, format, textureGetter);
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	private static IBakedModel bake(SensorCard card, IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		if (card == null) card = OpenCCSensors.Items.sensorCard.getSensorCard(0);

		IBakedModel model = new ItemLayerModel(ImmutableList.of(
			card.getSensor().getIcon(),
			card.getTier().getIcon()
		))
			.bake(state, format, textureGetter);

		ImmutableMap<TransformType, TRSRTransformation> transformation = MapWrapper.getTransforms(state);
		return new Baked(model, transformation, format, Maps.<SensorCard, IBakedModel>newHashMap());
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals("openccsensors") && modelLocation.getResourcePath().contains("sensor_card_dynamic");
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return this;
	}

	@Override
	public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
	}

	private static class Baked extends DerivedBakedModel implements IPerspectiveAwareModel {
		private final IBakedModel base;
		final ImmutableMap<TransformType, TRSRTransformation> transforms;
		final VertexFormat format;
		final Map<SensorCard, IBakedModel> cache;

		Baked(IBakedModel base, ImmutableMap<TransformType, TRSRTransformation> transforms, VertexFormat format, Map<SensorCard, IBakedModel> cache) {
			this.base = base;
			this.transforms = transforms;
			this.format = format;
			this.cache = cache;
		}

		@Override
		protected IBakedModel getBase() {
			return base;
		}

		@Nonnull
		@Override
		public ItemOverrideList getOverrides() {
			return Overrides.INSTANCE;
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
			return MapWrapper.handlePerspective(this, transforms, cameraTransformType);
		}
	}

	private static class Overrides extends ItemOverrideList {
		static final ItemOverrideList INSTANCE = new Overrides();

		private Overrides() {
			super(ImmutableList.<ItemOverride>of());
		}

		@Nonnull
		@Override
		public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
			if (!(originalModel instanceof Baked)) return originalModel;
			if (stack.getItem() != OpenCCSensors.Items.sensorCard) return originalModel;

			SensorCard card = OpenCCSensors.Items.sensorCard.getSensorCard(stack);
			if (card == null) return originalModel;

			Baked parent = (Baked) originalModel;
			IBakedModel cached = parent.cache.get(card);
			if (cached != null) return cached;

			cached = bake(card, new SimpleModelState(parent.transforms), parent.format, ModelLoader.defaultTextureGetter());
			parent.cache.put(card, cached);
			return cached;
		}
	}
}
