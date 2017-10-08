package openccsensors.api;

import net.minecraft.util.ResourceLocation;

public interface ISensorTier {
	EnumItemRarity getRarity();

	double getMultiplier();

	String getName();

	ResourceLocation getIcon();
}
