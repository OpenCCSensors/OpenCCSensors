package openccsensors.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IItemMeta {
	int getId();

	boolean displayInCreative();

	IIcon getIcon();

	String getName();

	ItemStack newItemStack(int size);
}
