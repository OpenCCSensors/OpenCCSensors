package openccsensors.common.item.meta;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import openccsensors.OpenCCSensors;
import openccsensors.api.IItemMeta;

public class ItemMetaRangeExtensionAntenna implements IItemMeta {
	private int id;

	public ItemMetaRangeExtensionAntenna(int id) {
		this.id = id;

		OpenCCSensors.Items.genericItem.addMeta(this);

		GameRegistry.addRecipe(new ShapedOreRecipe(
			newItemStack(1),
			new Object[]{
				" t ",
				"srs",
				"sis",
				't', new ItemStack(Blocks.REDSTONE_TORCH),
				's', "stone",
				'r', "dustRedstone",
				'i', "ingotIron",
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
	public ResourceLocation getModelName() {
		return new ResourceLocation("openccsensors", "range_extension_antenna");
	}

	@Override
	public ItemStack newItemStack(int size) {
		return new ItemStack(OpenCCSensors.Items.genericItem, size, getId());
	}

	@Override
	public String getName() {
		return "range_extension_antenna";
	}
}
