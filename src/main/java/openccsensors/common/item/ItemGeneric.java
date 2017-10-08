package openccsensors.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import openccsensors.OpenCCSensors;
import openccsensors.api.IItemMeta;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ItemGeneric extends Item {
	private HashMap<Integer, IItemMeta> metaitems = new HashMap<Integer, IItemMeta>();

	public ItemGeneric() {
		super();
		setHasSubtypes(true);
		setMaxDamage(0);
		setMaxStackSize(64);
		setCreativeTab(OpenCCSensors.tabOpenCCSensors);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> subItems) {
		OpenCCSensors.turtleUpgradeSensor.addTurtlesToCreative(subItems);
		for (Entry<Integer, IItemMeta> entry : metaitems.entrySet()) {
			if (entry.getValue().displayInCreative()) {
				subItems.add(new ItemStack(item, 1, entry.getKey()));
			}
		}
	}

	public void addMeta(IItemMeta meta) {
		metaitems.put(meta.getId(), meta);
	}

	public IItemMeta getMeta(ItemStack stack) {
		return getMeta(stack.getItemDamage());
	}

	public IItemMeta getMeta(int id) {
		return metaitems.get(id);
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		IItemMeta meta = getMeta(itemStack);
		if (meta == null) {
			return "";
		}
		return String.format("item.openccsensors.%s", meta.getName());
	}

}
