package openccsensors.common.sensor;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.MagicUtils;
import openccsensors.common.util.Mods;

import java.util.HashMap;
import java.util.Map;

public class MagicSensor extends TileSensor implements ISensor {
	@Override
	public boolean isValidTarget(Object tile) {
		return (Mods.AM && MagicUtils.isValidEssenceTarget(tile));
	}

	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {
		TileEntity tile = (TileEntity) obj;
		HashMap<String, Object> response = super.getDetails(tile, sensorPos);
		if (Mods.AM) {
			response.putAll(MagicUtils.getMapOfArsMagicaPower(world, obj, additional));
		}
		return response;
	}

	@Override
	public String[] getCustomMethods(ISensorTier tier) {
		return null;
	}

	@Override
	public Object callCustomMethod(World world, BlockPos location, int methodID, Object[] args, ISensorTier tier) {
		return null;
	}

	@Override
	public String getName() {
		return "magic_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:magic");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.GOLD_INGOT);
	}

}
