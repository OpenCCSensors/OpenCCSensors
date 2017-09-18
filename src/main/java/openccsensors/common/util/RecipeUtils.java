package openccsensors.common.util;

import java.lang.reflect.Field;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import openccsensors.OpenCCSensors;
import openccsensors.api.SensorCard;

public class RecipeUtils {

	public static void addTier1Recipe(ItemStack input, ItemStack output) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
			output,
			new Object[]{
				"rpr",
				"rrr",
				"aaa",
				'r', new ItemStack((Item) Item.itemRegistry.getObject("redstone")),
				'a', new ItemStack((Item) Item.itemRegistry.getObject("paper")),
				'p', input
			}
		));

	}

	public static void addTier2Recipe(ItemStack input, ItemStack output) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(
			output,
			input,
			OpenCCSensors.Items.rangeExtensionAntenna.newItemStack(1)
		));
	}

	public static void addTier3Recipe(ItemStack input, ItemStack output) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
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
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
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
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
			new ItemStack(OpenCCSensors.Blocks.sensorBlock),
			new Object[]{
				"ooo",
				"ror",
				"sss",
				'o', new ItemStack((Block) Block.blockRegistry.getObject("obsidian")),
				'r', new ItemStack((Item) Item.itemRegistry.getObject("redstone")),
				's', new ItemStack((Block) Block.blockRegistry.getObject("stone"))
			}
		));
	}

	public static void addGaugeRecipe() {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
			new ItemStack(OpenCCSensors.Blocks.gaugeBlock),
			new Object[]{
				"grm",
				'g', new ItemStack((Block) Block.blockRegistry.getObject("glass_pane")),
				'r', new ItemStack((Item) Item.itemRegistry.getObject("redstone")),
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

		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
			new ItemStack(OpenCCSensors.Blocks.basicSensorBlock),
			new Object[]{
				"   ",
				"cpc",
				"rir",
				'c', new ItemStack((Item) Item.itemRegistry.getObject("comparator")),
				'p', proxCard,
				'r', new ItemStack((Item) Item.itemRegistry.getObject("redstone")),
				'i', new ItemStack((Block) Block.blockRegistry.getObject("iron_block")),
			}
		));
	}
}
