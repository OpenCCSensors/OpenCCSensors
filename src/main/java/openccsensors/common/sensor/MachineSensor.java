package openccsensors.common.sensor;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.IGaugeSensor;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.CoFHUtils;
import openccsensors.common.util.Ic2Utils;
import openccsensors.common.util.Mods;

import java.util.Map;

public class MachineSensor extends TileSensor implements ISensor, IGaugeSensor {
	private String[] gaugeProperties = new String[]{
		"HeatPercentage",
		"Progress"
	};

	@Override
	public String[] getGaugeProperties() {
		return gaugeProperties;
	}

	@Override
	public boolean isValidTarget(Object target) {
		return (Mods.IC2 && Ic2Utils.isValidMachineTarget(target)) ||
			(Mods.TE && CoFHUtils.isValidMachineTarget(target));
	}

	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {
		TileEntity tile = (TileEntity) obj;
		Map<String, Object> response = super.getDetails(tile, sensorPos);
		if (Mods.IC2) {
			response.putAll(Ic2Utils.getMachineDetails(world, obj, additional));
		}
		if (Mods.TE) {
			response.putAll(CoFHUtils.getMachineDetails(world, obj, additional));
		}
		return response;
	}

	@Override
	public String[] getCustomMethods(ISensorTier tier) {
		return null;
	}

	@Override
	public Object callCustomMethod(World world, BlockPos location, int methodID, Object[] args, ISensorTier tier) throws Exception {
		return null;
	}

	@Override
	public String getName() {
		return "machine_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:machine");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.REDSTONE);
	}

}
