package openccsensors.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class DerivedBakedModel implements IBakedModel {
	protected abstract IBakedModel getBase();

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		return getBase().getQuads(state, side, rand);
	}

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
