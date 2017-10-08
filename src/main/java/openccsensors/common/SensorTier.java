package openccsensors.common;

import net.minecraft.util.ResourceLocation;
import openccsensors.api.EnumItemRarity;
import openccsensors.api.ISensorTier;

public class SensorTier implements ISensorTier {
	private final String name;
	private final EnumItemRarity rarity;
	private final int multiplier;
	private final ResourceLocation iconName;

	public SensorTier(String name, EnumItemRarity rarity, int multiplier, ResourceLocation iconName) {
		this.name = name;
		this.rarity = rarity;
		this.multiplier = multiplier;
		this.iconName = iconName;
	}

	@Override
	public EnumItemRarity getRarity() {
		return rarity;
	}

	@Override
	public double getMultiplier() {
		return multiplier;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ResourceLocation getIcon() {
		return iconName;
	}
}
