package openccsensors.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IItemMeta {
	int getId();

	boolean displayInCreative();

	ResourceLocation getIcon();

	String getName();

	ItemStack newItemStack(int size);
}
