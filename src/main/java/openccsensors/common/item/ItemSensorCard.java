package openccsensors.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import openccsensors.OpenCCSensors;
import openccsensors.api.*;
import openccsensors.common.SensorTier;
import openccsensors.common.util.OCSLog;
import openccsensors.common.util.RecipeUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ItemSensorCard extends Item implements ISensorCardRegistry {
	private final HashMap<Integer, SensorCard> cards = new HashMap<Integer, SensorCard>();

	public ItemSensorCard() {
		super();
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(OpenCCSensors.tabOpenCCSensors);
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		SensorCard card = getSensorCard(itemStack);
		if (card == null) {
			return "";
		}
		return String.format("item.openccsensors.%s", card.getSensor().getName());
	}

	public void registerSensors() {
		SensorTier tier1 = new SensorTier("Mk. I", EnumItemRarity.COMMON, 2, new ResourceLocation("openccsensors", "tier1"));
		SensorTier tier2 = new SensorTier("Mk. II", EnumItemRarity.UNCOMMON, 4, new ResourceLocation("openccsensors", "tier2"));
		SensorTier tier3 = new SensorTier("Mk. III", EnumItemRarity.RARE, 6, new ResourceLocation("openccsensors", "tier3"));
		SensorTier tier4 = new SensorTier("Mk. IV", EnumItemRarity.EPIC, 8, new ResourceLocation("openccsensors", "tier4"));

		OpenCCSensors.Tiers.tier1 = tier1;
		OpenCCSensors.Tiers.tier2 = tier2;
		OpenCCSensors.Tiers.tier3 = tier3;
		OpenCCSensors.Tiers.tier4 = tier4;

		addSensorCard(1, new SensorCard(OpenCCSensors.Sensors.proximitySensor, tier1));
		addSensorCard(2, new SensorCard(OpenCCSensors.Sensors.proximitySensor, tier2));
		addSensorCard(3, new SensorCard(OpenCCSensors.Sensors.proximitySensor, tier3));
		addSensorCard(4, new SensorCard(OpenCCSensors.Sensors.proximitySensor, tier4));

		addSensorCard(5, new SensorCard(OpenCCSensors.Sensors.droppedItemSensor, tier1));
		addSensorCard(6, new SensorCard(OpenCCSensors.Sensors.droppedItemSensor, tier2));
		addSensorCard(7, new SensorCard(OpenCCSensors.Sensors.droppedItemSensor, tier3));
		addSensorCard(8, new SensorCard(OpenCCSensors.Sensors.droppedItemSensor, tier4));

		/*addSensorCard(9, new SensorCard(OpenCCSensors.Sensors.signSensor, tier1));
		addSensorCard(10, new SensorCard(OpenCCSensors.Sensors.signSensor, tier2));
		addSensorCard(11, new SensorCard(OpenCCSensors.Sensors.signSensor, tier3));
		addSensorCard(12, new SensorCard(OpenCCSensors.Sensors.signSensor, tier4));
		addIconsForLoading(OpenCCSensors.Sensors.signSensor);
		*/
		addSensorCard(13, new SensorCard(OpenCCSensors.Sensors.minecartSensor, tier1));
		addSensorCard(14, new SensorCard(OpenCCSensors.Sensors.minecartSensor, tier2));
		addSensorCard(15, new SensorCard(OpenCCSensors.Sensors.minecartSensor, tier3));
		addSensorCard(16, new SensorCard(OpenCCSensors.Sensors.minecartSensor, tier4));

		addSensorCard(17, new SensorCard(OpenCCSensors.Sensors.sonicSensor, tier1));
		addSensorCard(18, new SensorCard(OpenCCSensors.Sensors.sonicSensor, tier2));
		addSensorCard(19, new SensorCard(OpenCCSensors.Sensors.sonicSensor, tier3));
		addSensorCard(20, new SensorCard(OpenCCSensors.Sensors.sonicSensor, tier4));

		addSensorCard(21, new SensorCard(OpenCCSensors.Sensors.tankSensor, tier1));
		addSensorCard(22, new SensorCard(OpenCCSensors.Sensors.tankSensor, tier2));
		addSensorCard(23, new SensorCard(OpenCCSensors.Sensors.tankSensor, tier3));
		addSensorCard(24, new SensorCard(OpenCCSensors.Sensors.tankSensor, tier4));

		addSensorCard(25, new SensorCard(OpenCCSensors.Sensors.inventorySensor, tier1));
		addSensorCard(26, new SensorCard(OpenCCSensors.Sensors.inventorySensor, tier2));
		addSensorCard(27, new SensorCard(OpenCCSensors.Sensors.inventorySensor, tier3));
		addSensorCard(28, new SensorCard(OpenCCSensors.Sensors.inventorySensor, tier4));

		addSensorCard(29, new SensorCard(OpenCCSensors.Sensors.worldSensor, tier1));

		addSensorCard(30, new SensorCard(OpenCCSensors.Sensors.powerSensor, tier1));
		addSensorCard(31, new SensorCard(OpenCCSensors.Sensors.powerSensor, tier2));
		addSensorCard(32, new SensorCard(OpenCCSensors.Sensors.powerSensor, tier3));
		addSensorCard(33, new SensorCard(OpenCCSensors.Sensors.powerSensor, tier4));

		addSensorCard(34, new SensorCard(OpenCCSensors.Sensors.machineSensor, tier1));
		addSensorCard(35, new SensorCard(OpenCCSensors.Sensors.machineSensor, tier2));
		addSensorCard(36, new SensorCard(OpenCCSensors.Sensors.machineSensor, tier3));
		addSensorCard(37, new SensorCard(OpenCCSensors.Sensors.machineSensor, tier4));

		addSensorCard(38, new SensorCard(OpenCCSensors.Sensors.cropSensor, tier1));
		addSensorCard(39, new SensorCard(OpenCCSensors.Sensors.cropSensor, tier2));
		addSensorCard(40, new SensorCard(OpenCCSensors.Sensors.cropSensor, tier3));
		addSensorCard(41, new SensorCard(OpenCCSensors.Sensors.cropSensor, tier4));

		addSensorCard(42, new SensorCard(OpenCCSensors.Sensors.magicSensor, tier1));
		addSensorCard(43, new SensorCard(OpenCCSensors.Sensors.magicSensor, tier2));
		addSensorCard(44, new SensorCard(OpenCCSensors.Sensors.magicSensor, tier3));
		addSensorCard(45, new SensorCard(OpenCCSensors.Sensors.magicSensor, tier4));

		OCSLog.info("Added %d Sensor Cards", cards.size());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
		SensorCard card = getSensorCard(itemStack);
		if (card != null) {
			ISensorTier tier = card.getTier();
			list.add(tier.getName());
		}
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	@Override
	public EnumRarity getRarity(ItemStack itemStack) {
		SensorCard card = getSensorCard(itemStack);
		if (card != null) {
			ISensorTier tier = card.getTier();
			switch (tier.getRarity()) {
				case COMMON:
					return EnumRarity.COMMON;
				case UNCOMMON:
					return EnumRarity.UNCOMMON;
				case RARE:
					return EnumRarity.RARE;
				case EPIC:
					return EnumRarity.EPIC;
			}
		}
		return EnumRarity.COMMON;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> subItems) {
		for (Entry<Integer, SensorCard> entry : cards.entrySet()) {
			subItems.add(new ItemStack(item, 1, entry.getKey()));
		}
	}

	public SensorCard getSensorCard(ItemStack stack) {
		if (!cards.containsKey(stack.getItemDamage())) {
			stack.setItemDamage(cards.keySet().iterator().next());
		}

		return getSensorCard(stack.getItemDamage());
	}

	public SensorCard getSensorCard(int id) {
		if (!cards.containsKey(id)) {
			return cards.values().iterator().next();
		}
		return cards.get(id);
	}

	public Entry<Integer, SensorCard> getEntryForSensorAndTier(ISensor sensor, SensorTier tier) {
		for (Entry<Integer, SensorCard> entry : cards.entrySet()) {
			if (entry.getValue().getSensor() == sensor && entry.getValue().getTier() == tier) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void addSensorCard(int id, SensorCard sensorCard) {
		cards.put(id, sensorCard);
		Entry<Integer, SensorCard> previousTier;
		if (sensorCard.getTier() == OpenCCSensors.Tiers.tier1) {
			RecipeUtils.addTier1Recipe(
				sensorCard.getSensor().getUniqueRecipeItem(),
				new ItemStack(OpenCCSensors.Items.sensorCard, 1, id)
			);
		} else if (sensorCard.getTier() == OpenCCSensors.Tiers.tier2) {
			previousTier = getEntryForSensorAndTier(sensorCard.getSensor(), OpenCCSensors.Tiers.tier1);
			RecipeUtils.addTier2Recipe(
				new ItemStack(OpenCCSensors.Items.sensorCard, 1, previousTier.getKey()),
				new ItemStack(OpenCCSensors.Items.sensorCard, 1, id)
			);
		} else if (sensorCard.getTier() == OpenCCSensors.Tiers.tier3) {
			previousTier = getEntryForSensorAndTier(sensorCard.getSensor(), OpenCCSensors.Tiers.tier2);
			RecipeUtils.addTier3Recipe(
				new ItemStack(OpenCCSensors.Items.sensorCard, 1, previousTier.getKey()),
				new ItemStack(OpenCCSensors.Items.sensorCard, 1, id)
			);
		} else if (sensorCard.getTier() == OpenCCSensors.Tiers.tier4) {
			previousTier = getEntryForSensorAndTier(sensorCard.getSensor(), OpenCCSensors.Tiers.tier3);
			RecipeUtils.addTier4Recipe(
				new ItemStack(OpenCCSensors.Items.sensorCard, 1, previousTier.getKey()),
				new ItemStack(OpenCCSensors.Items.sensorCard, 1, id)
			);
		}
	}

}
