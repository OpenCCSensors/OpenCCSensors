package openccsensors.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IItemMeta {
	int getId();

	boolean displayInCreative();

	ResourceLocation getModelName();

	String getName();

	ItemStack newItemStack(int size);
}
