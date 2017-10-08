package openccsensors.common.item.meta;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import openccsensors.OpenCCSensors;
import openccsensors.api.IItemMeta;

public class ItemMetaAdvancedAmplifier implements IItemMeta {
	private int id;

	public ItemMetaAdvancedAmplifier(int id) {
		this.id = id;

		OpenCCSensors.Items.genericItem.addMeta(this);

		GameRegistry.addRecipe(new ShapedOreRecipe(
			newItemStack(1),
			new Object[]{
				"igi",
				"rdr",
				"igi",
				'i', "ingotIron",
				'g', "ingotGold",
				'r', "dustRedstone",
				'd', "gemDiamond",
			}
		));
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean displayInCreative() {
		return true;
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors", "advanced_amplifier");
	}

	@Override
	public ItemStack newItemStack(int size) {
		return new ItemStack(OpenCCSensors.Items.genericItem, size, getId());
	}

	@Override
	public String getName() {
		return "advanced_amplifier";
	}
}
