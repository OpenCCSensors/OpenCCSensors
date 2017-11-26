package openccsensors.common.sensor;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import openccsensors.api.IGaugeSensor;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.CoFHUtils;
import openccsensors.common.util.Ic2Utils;
import openccsensors.common.util.Mods;

import java.util.HashMap;
import java.util.Map;

public class PowerSensor extends TileSensor implements ISensor, IGaugeSensor {

	private String[] gaugeProperties = new String[]{
		"StoredPercentage"
	};

	@Override
	public boolean isValidTarget(Object target) {
		if (!(target instanceof TileEntity)) {
			return false;
		}

		return ((TileEntity) target).hasCapability(CapabilityEnergy.ENERGY, null) ||
			(Mods.IC2 && Ic2Utils.isValidPowerTarget(target)) ||
			(Mods.COFH && CoFHUtils.isValidPowerTarget(target)) ||
			(Mods.TE && CoFHUtils.isValidPowerTarget(target));
	}

	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {
		HashMap<String, Object> response = super.getDetails((TileEntity) obj, sensorPos);
		if (Mods.IC2) {
			response.putAll(Ic2Utils.getPowerDetails(world, obj, additional));
		}
		if (Mods.COFH || Mods.TE) {
			response.putAll(CoFHUtils.getPowerDetails(world, obj, additional));
		}

		IEnergyStorage energyStorage = ((TileEntity) obj).getCapability(CapabilityEnergy.ENERGY, null);
		if (energyStorage != null) {
			double stored = energyStorage.getEnergyStored();
			double capacity = energyStorage.getMaxEnergyStored();
			response.put("Stored", stored);
			response.put("Capacity", capacity);
			response.put("StoredPercentage", 0);

			if (capacity > 0) {
				response.put("StoredPercentage", Math.max(Math.min(100, ((100.0 * stored) / capacity)), 0));
			}
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
		return "power_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:items/power");
	}

	@Override
	public String[] getGaugeProperties() {
		return gaugeProperties;
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.COAL);
	}
}
