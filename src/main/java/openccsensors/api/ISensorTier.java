package openccsensors.api;

import net.minecraft.util.IIcon;

public interface ISensorTier {
	EnumItemRarity getRarity();

	double getMultiplier();

	String getName();

	IIcon getIcon();
}
