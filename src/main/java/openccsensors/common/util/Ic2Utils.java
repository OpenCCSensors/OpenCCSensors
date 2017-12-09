package openccsensors.common.util;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.NodeStats;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.common.sensor.CropSensor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Ic2Utils {

	public static final int MASSFAB_MAX_ENERGY = 1100000;
	protected static final String MASS_FAB_CLASS = "ic2.core.block.machine.tileentity.TileEntityMatter";

	public static boolean isValidPowerTarget(Object target) {
		IEnergyTile energyTarget = null;
		if (target instanceof TileEntity) {
			TileEntity tile = (TileEntity) target;
			energyTarget = EnergyNet.instance.getTile(tile.getWorld(), tile.getPos());
		}

		return energyTarget != null;
	}

	public static boolean isValidMachineTarget(Object target) {
		return target != null &&
			(
				//target.getClass().getName() == MASS_FAB_CLASS ||
				target instanceof IReactor ||
					target instanceof IReactorChamber
			);
	}

	public static Map<String, Object> getMachineDetails(World world, Object obj, boolean additional) {
		if (obj == null || !(obj instanceof TileEntity) || !additional) {
			return Collections.emptyMap();
		}

		HashMap<String, Object> response = new HashMap<String, Object>();

		TileEntity tile = (TileEntity) obj;

		IReactor reactor = null;
		if (tile instanceof IReactor) {
			reactor = (IReactor) tile;
		} else if (tile instanceof IReactorChamber) {
			reactor = ((IReactorChamber) tile).getReactorInstance();
		}

		if (reactor != null) {
			int maxHeat = reactor.getMaxHeat();
			int heat = reactor.getHeat();
			response.put("Heat", heat);
			response.put("MaxHeat", maxHeat);
			response.put("Output", reactor.getReactorEUEnergyOutput());
			response.put("Active", reactor.produceEnergy());
			response.put("HeatPercentage", 0);
			if (maxHeat > 0) {
				double heatPercentage = ((100.0 / maxHeat) * heat);
				response.put("HeatPercentage", Math.max(Math.min(heatPercentage, 100), 0));
			}
		}

		if (tile.getClass().getName().equals(MASS_FAB_CLASS)) {
			NBTTagCompound tagCompound = getTagCompound(tile);
			response.put("Energy", tagCompound.getInteger("energy"));
			response.put("MaxEnergy", MASSFAB_MAX_ENERGY);
			response.put("Progress", 0);
			double progress = ((100.0 / MASSFAB_MAX_ENERGY) * tagCompound.getInteger("energy"));
			response.put("Progress", Math.min(Math.max(0, progress), 100));
		}

		return response;
	}

	public static Map<String, Object> getPowerDetails(World world, Object obj, boolean additional) {
		if (obj == null || !(obj instanceof TileEntity) || !additional) {
			return Collections.emptyMap();
		}

		HashMap<String, Object> response = new HashMap<String, Object>();

		TileEntity tile = (TileEntity) obj;

		if (tile instanceof IEnergyStorage) {

			IEnergyStorage storage = (IEnergyStorage) tile;
			int capacity = storage.getCapacity();
			int stored = storage.getStored();

			response.put("Stored", stored);
			response.put("Capacity", capacity);
			response.put("Output", storage.getOutput());
			response.put("StoredPercentage", 0);

			if (capacity > 0) {
				response.put("StoredPercentage", Math.max(Math.min(100, ((100.0 / capacity) * stored)), 0));
			}
		}

		if (tile instanceof IEnergyTile) {
			NodeStats stats = EnergyNet.instance.getNodeStats((IEnergyTile) tile);
			double energyIn = stats.getEnergyIn();
			double energyOut = stats.getEnergyOut();
			response.put("EnergyEmitted", energyOut);
			response.put("EnergySunken", energyIn);
			response.put("EnergyOut", energyOut);
			response.put("EnergyIn", energyIn);
			response.put("Voltage", stats.getVoltage());
		}

		return response;
	}

	protected static NBTTagCompound getTagCompound(TileEntity tile) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tile.writeToNBT(tagCompound);
		return tagCompound;
	}

	public static boolean isValidCropTarget(TileEntity tile) {
		return tile instanceof ICropTile;
	}

	public static Map<String, Object> getCropDetails(Object obj, BlockPos sensorPos, boolean additional) {
		if (obj == null) return Collections.emptyMap();
		HashMap<String, Object> response = new HashMap<String, Object>();

		TileEntity tile = (TileEntity) obj;
		HashMap<String, Integer> position = new HashMap<String, Integer>();
		position.put("X", tile.getPos().getX() - sensorPos.getX());
		position.put("Y", tile.getPos().getY() - sensorPos.getY());
		position.put("Z", tile.getPos().getZ() - sensorPos.getZ());
		response.put("Position", position);

		ItemStack stack = new ItemStack(tile.getBlockType(), 1, tile.getBlockMetadata());
		response.putAll(InventoryUtils.itemstackToBasicMap(stack));

		if (obj instanceof ICropTile && additional) {
			ICropTile crop = (ICropTile) obj;
			response.put("AirQuality", crop.getTerrainAirQuality());
			response.put("Growth", crop.getStatGrowth());
			response.put("Gain", crop.getStatGain());
			response.put("Humidity", crop.getTerrainHumidity());
			response.put("HydrationStorage", crop.getStorageWater());
			response.put("LightLevel", crop.getLightLevel());
			response.put("Nutrients", crop.getTerrainNutrients());
			response.put("NutrientStorage", crop.getStorageNutrients());
			response.put("Resistance", crop.getStatResistance());
			response.put("ScanLevel", crop.getScanLevel());
			response.put("Size", crop.getCurrentSize());
			response.put("WeedExStorage", crop.getStorageWeedEX());
			response.put("Status", "Empty");
			CropCard cropCard = crop.getCrop();
			if (cropCard != null) {
				response.put("IsWeed", cropCard.isWeed(crop));
				response.put("CanBeHarvested", cropCard.canBeHarvested(crop));
				if (cropCard.canBeHarvested(crop)) {
					response.put("Status", CropSensor.STATUS_GROWN);
				} else if (crop.getCurrentSize() == 0) {
					response.put("Status", CropSensor.STATUS_NEW);
				} else {
					response.put("Status", CropSensor.STATUS_GROWING);
				}
				response.put("DiscoveredBy", cropCard.getDiscoveredBy());
				response.put("EmittedLight", cropCard.getEmittedLight(crop));
				response.put("SizeAfterHarvest", cropCard.getSizeAfterHarvest(crop));
				response.put("CanCross", cropCard.canCross(crop));
				response.put("CanGrow", cropCard.canGrow(crop));
				response.put("Name", cropCard.getClass().getSimpleName());
			}
		}
		return response;
	}
}
