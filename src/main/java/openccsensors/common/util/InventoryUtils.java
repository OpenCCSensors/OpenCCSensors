package openccsensors.common.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class InventoryUtils {

	public static final String FACTORIZATION_BARREL_CLASS = "factorization.common.TileEntityBarrel";
	public static int[] mapColors = new int[]{
		32768, // black
		32,    // lime
		16,    // yellow
		256,   // light gray
		16384, // red
		2048,  // blue
		128,   // gray
		8192,  // green
		1,     // white
		512,   // cyan
		4096,  // brown
		128,   // gray
		2048,  // blue
		4096   // brown
	};

	public static Map<String, Object> itemstackToMap(ItemStack itemstack) {
		if (itemstack == null) {
			return null;
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("Name", getNameForItemStack(itemstack));
			map.put("RawName", getRawNameForStack(itemstack));
			map.put("Size", itemstack.stackSize);
			map.put("DamageValue", itemstack.getItemDamage());
			map.put("MaxStack", itemstack.getMaxStackSize());
			Item item = itemstack.getItem();
			if (item != null) {
				if (item instanceof ItemEnchantedBook) {
					map.put("Enchantments", getBookEnchantments(itemstack));
				}
			}

			return map;
		}

	}

	protected static Map<Integer, Object> getBookEnchantments(ItemStack stack) {
		HashMap<Integer, Object> response = new HashMap<Integer, Object>();

		ItemEnchantedBook book = (ItemEnchantedBook) stack.getItem();
		NBTTagList nbttaglist = book.getEnchantments(stack);
		int offset = 1;
		if (nbttaglist != null) {
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				short short1 = (nbttaglist.getCompoundTagAt(i)).getShort("id");
				short short2 = (nbttaglist.getCompoundTagAt(i)).getShort("lvl");

				Enchantment enchantment = Enchantment.getEnchantmentByID(short1);
				if (enchantment != null) {
					response.put(offset, enchantment.getTranslatedName(short2));
					offset++;
				}
			}
		}
		return response;
	}

	public static Map<Integer, Object> invToMap(IInventory inventory) {
		HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		if (inventory.getClass().getName().equals(FACTORIZATION_BARREL_CLASS)) {
			Map<String, Object> details = itemstackToMap(inventory.getStackInSlot(0));
			try {
				TileEntity barrel = (TileEntity) inventory;
				NBTTagCompound compound = new NBTTagCompound();
				barrel.writeToNBT(compound);
				details.put("Size", compound.getInteger("item_count"));
				details.put("MaxStack",
					compound.getInteger("upgrade") == 1 ? 65536 : 4096);

			} catch (Exception e) {
			}
			map.put(1, details);
		} else {
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				map.put(i + 1, itemstackToMap(inventory.getStackInSlot(i)));
			}
		}
		return map;
	}

	public static Map<String, Object> getInventorySizeCalculations(IInventory inventory) {
		ItemStack stack;
		int totalSpace = 0;
		int itemCount = 0;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			stack = inventory.getStackInSlot(i);
			if (stack == null) {
				totalSpace += 64;
			} else {
				totalSpace += stack.getMaxStackSize();
				itemCount += stack.stackSize;
			}
		}

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("TotalSpace", totalSpace);
		response.put("ItemCount", itemCount);
		if (totalSpace > 0) {
			double percent = (double) 100 / totalSpace * itemCount;
			percent = Math.max(Math.min(percent, 100), 0);
			response.put("InventoryPercentFull", percent);
		}

		return response;
	}

	public static String getNameForItemStack(ItemStack is) {
		String name = "Unknown";
		try {
			name = is.getDisplayName();
		} catch (Exception e) {
		}
		return name;
	}

	public static String getRawNameForStack(ItemStack is) {

		String rawName = "unknown";

		try {
			rawName = is.getDisplayName().toLowerCase();
		} catch (Exception e) {
		}
		try {
			if (rawName.length() - rawName.replaceAll("\\.", "").length() == 0) {
				String packageName = is.getItem().getClass().getName()
					.toLowerCase();
				String[] packageLevels = packageName.split("\\.");
				if (!rawName.startsWith(packageLevels[0])
					&& packageLevels.length > 1) {
					rawName = packageLevels[0] + "." + rawName;
				}
			}
		} catch (Exception e) {

		}

		return rawName.trim();
	}

	public static ItemStack getStackInSlot(World world, HashMap targets, String targetName, int slot) {
		if (targets.containsKey(targetName)) {
			Object target = targets.get(targetName);
			if (target instanceof IInventory) {
				IInventory inventory = (IInventory) target;
				if (slot < inventory.getSizeInventory()) {
					return inventory.getStackInSlot(slot);
				}
			}
		}
		return null;
	}

	public static Map<String, Object> getMapData(World world, HashMap targets, String targetName, int slot) {
		ItemStack stack = getStackInSlot(world, targets, targetName, slot);

		if (stack != null) {
			Item item = stack.getItem();
			if (item != null && item instanceof ItemMap) {
				// Create a new map
				MapData data = ((ItemMap) item).getMapData(stack, world);

				// prepare the return data
				HashMap<String, Object> ret = new HashMap<String, Object>();
				ret.put("MapName", data.mapName);
				ret.put("Scale", (int) data.scale);
				ret.put("CenterX", data.xCenter);
				ret.put("CenterZ", data.zCenter);
				HashMap<Integer, Integer> colors = new HashMap<Integer, Integer>();
				// put all the colours in
				for (int b = 0; b < data.colors.length; b++) {
					colors.put(b + 1, mapColors[data.colors[b] / 4]);
				}
				ret.put("Colors", colors);
				return ret;
			}
		}
		return null;
	}
}
