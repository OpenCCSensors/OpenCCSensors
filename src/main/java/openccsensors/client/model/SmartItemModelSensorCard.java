package openccsensors.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import openccsensors.OpenCCSensors;
import openccsensors.api.SensorCard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmartItemModelSensorCard implements IBakedModel, IResourceManagerReloadListener {
	private final Map<SensorCard, IBakedModel> cache = Maps.newHashMap();
	private final ItemOverrideList overrideList = new ItemOverrideList(new ArrayList<ItemOverride>()) {
		@Nonnull
		@Override
		public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entity) {
			return getModel(OpenCCSensors.Items.sensorCard.getSensorCard(stack));
		}
	};

	@Nonnull
	private IBakedModel getDefaultModel() {
		return getModel(OpenCCSensors.Items.sensorCard.getSensorCard(1));
	}

	private IBakedModel getModel(SensorCard card) {
		IBakedModel model = cache.get(card);
		if (model == null) cache.put(card, model = bakeModel(card));
		return model;
	}

	@Nonnull
	private static IBakedModel bakeModel(SensorCard card) {
		return new ItemLayerModel(ImmutableList.of(
			card.getSensor().getIcon(),
			card.getTier().getIcon()
		))
			.bake(ModelRotation.X0_Y0, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
	}

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return overrideList;
	}

	@Override
	public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
		cache.clear();
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		return getDefaultModel().getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return getDefaultModel().isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return getDefaultModel().isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return getDefaultModel().isBuiltInRenderer();
	}

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return getDefaultModel().getParticleTexture();
	}

	@Nonnull
	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms() {
		return getDefaultModel().getItemCameraTransforms();
	}
}
