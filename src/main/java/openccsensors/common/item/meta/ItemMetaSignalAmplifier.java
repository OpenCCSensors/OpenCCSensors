package openccsensors.common.item.meta;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import openccsensors.OpenCCSensors;
import openccsensors.api.IItemMeta;

public class ItemMetaSignalAmplifier implements IItemMeta {

	private int id;

	public ItemMetaSignalAmplifier(int id) {
		this.id = id;

		OpenCCSensors.Items.genericItem.addMeta(this);

		GameRegistry.addRecipe(new ShapedOreRecipe(
			newItemStack(1),
			new Object[]{
				"sgs",
				"rrr",
				"sgs",
				's', "stone",
				'r', "dustRedstone",
				'g', "ingotGold"
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
		return new ResourceLocation("openccsensors", "signal_amplifier");
	}

	@Override
	public ItemStack newItemStack(int size) {
		return new ItemStack(OpenCCSensors.Items.genericItem, size, getId());
	}

	@Override
	public String getName() {
		return "signal_amplifier";
	}
}
