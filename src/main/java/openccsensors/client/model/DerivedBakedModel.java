package openccsensors.client.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import javax.annotation.Nonnull;

public abstract class DerivedBakedModel implements IBakedModel {
	protected abstract IBakedModel getBase();

	@Override
	public boolean isAmbientOcclusion() {
		return getBase().isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return getBase().isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return getBase().isBuiltInRenderer();
	}

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return getBase().getParticleTexture();
	}

	@Nonnull
	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms() {
		return getBase().getItemCameraTransforms();
	}

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return getBase().getOverrides();
	}
}
