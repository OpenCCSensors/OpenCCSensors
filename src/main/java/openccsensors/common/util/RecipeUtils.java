package openccsensors.common.util;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import openccsensors.OpenCCSensors;
import openccsensors.api.SensorCard;

import java.lang.reflect.Field;
import java.util.Map.Entry;

public class RecipeUtils {

	public static void addTier1Recipe(ItemStack input, ItemStack output) {
		GameRegistry.addRecipe(new ShapedOreRecipe(
			output,
			new Object[]{
				"rpr",
				"rrr",
				"aaa",
				'r', "dustRedstone",
				'a', "paper",
				'p', input
			}
		));

	}

	public static void addTier2Recipe(ItemStack input, ItemStack output) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(
			output,
			input,
			OpenCCSensors.Items.rangeExtensionAntenna.newItemStack(1)
		));
	}

	public static void addTier3Recipe(ItemStack input, ItemStack output) {
		GameRegistry.addRecipe(new ShapedOreRecipe(
			output,
			new Object[]{
				"aca",
				" m ",
				'a', OpenCCSensors.Items.rangeExtensionAntenna.newItemStack(1),
				'c', input,
				'm', OpenCCSensors.Items.signalAmplifier.newItemStack(1)
			}
		));
	}

	public static void addTier4Recipe(ItemStack input, ItemStack output) {
		GameRegistry.addRecipe(new ShapedOreRecipe(
			output,
			new Object[]{
				" a ",
				"aca",
				"mpm",
				'a', OpenCCSensors.Items.rangeExtensionAntenna.newItemStack(1),
				'c', input,
				'm', OpenCCSensors.Items.signalAmplifier.newItemStack(1),
				'p', OpenCCSensors.Items.advancedAmplifier.newItemStack(1)
			}
		));
	}

	public static void addSensorRecipe() {
		GameRegistry.addRecipe(new ShapedOreRecipe(
			new ItemStack(OpenCCSensors.Blocks.sensorBlock),
			new Object[]{
				"ooo",
				"ror",
				"sss",
				'o', "obsidian",
				'r', "dustRedstone",
				's', "stone",
			}
		));
	}

	public static void addGaugeRecipe() {
		GameRegistry.addRecipe(new ShapedOreRecipe(
			new ItemStack(OpenCCSensors.Blocks.gaugeBlock),
			new Object[]{
				"grm",
				'g', "paneGlass",
				'r', "dustRedstone",
				'm', new ItemStack(getMonitor(), 1, 2)
			}
		));
	}

	private static Block getMonitor() {
		Block monitor = null;
		try {
			Class cc = Class.forName("dan200.computercraft.ComputerCraft$Blocks");
			if (cc != null) {
				Field peripheralField = cc.getDeclaredField("peripheral");
				if (peripheralField != null) {
					monitor = (Block) peripheralField.get(cc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return monitor;
	}

	public static void addProxSensorBlockRecipe() {

		Entry<Integer, SensorCard> entry = OpenCCSensors.Items.sensorCard.getEntryForSensorAndTier(
			OpenCCSensors.Sensors.proximitySensor,
			OpenCCSensors.Tiers.tier4
		);
		ItemStack proxCard = new ItemStack(OpenCCSensors.Items.sensorCard, 1, entry.getKey());

		GameRegistry.addRecipe(new ShapedOreRecipe(
			new ItemStack(OpenCCSensors.Blocks.basicSensorBlock),
			new Object[]{
				"   ",
				"cpc",
				"rir",
				'c', Items.COMPARATOR,
				'p', proxCard,
				'r', "dustRedstone",
				'i', "blockIron",
			}
		));
	}
}
