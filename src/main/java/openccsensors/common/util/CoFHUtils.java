package openccsensors.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import net.minecraft.world.World;

public class CoFHUtils {

	public static boolean isValidPowerTarget(Object target) {
		return target != null && (
			target instanceof IEnergyHandler ||
				target instanceof IEnergyProvider
		);
	}

	public static boolean isValidMachineTarget(Object target) {
		return target != null && (
			target instanceof IEnergyHandler ||
				target instanceof IEnergyProvider
		);
	}

	public static Map<String, Object> getPowerDetails(World world, Object obj, boolean additional) {
		if (obj == null || !(obj instanceof IEnergyHandler || obj instanceof IEnergyProvider) || !additional) {
			return Collections.emptyMap();
		}

		HashMap<String, Object> response = new HashMap<String, Object>();

		int stored;
		int capacity;

		if (obj instanceof IEnergyHandler || obj instanceof IEnergyProvider) {
			if (obj instanceof IEnergyHandler) {
				IEnergyHandler energyHandler = (IEnergyHandler) obj;
				stored = energyHandler.getEnergyStored(null);
				capacity = energyHandler.getMaxEnergyStored(null);
			} else if (obj instanceof IEnergyProvider) {
				IEnergyProvider energyProvider = (IEnergyProvider) obj;
				stored = energyProvider.getEnergyStored(null);
				capacity = energyProvider.getMaxEnergyStored(null);
			} else {
				return response;
			}

			response.put("Stored", stored);
			response.put("Capacity", capacity);
			response.put("StoredPercentage", 0);

			if (capacity > 0) {
				response.put("StoredPercentage", Math.max(Math.min(100, ((100.0 * stored) / capacity)), 0));
			}
		}
		return response;
	}

	public static Map<String, Object> getMachineDetails(World world, Object obj, boolean additional) {
		HashMap<String, Object> response = new HashMap<String, Object>();

		if (obj == null || !(obj instanceof IEnergyHandler || obj instanceof IEnergyProvider) || !additional) {
			return response;
		}

		int stored;
		int capacity;

		if (obj instanceof IEnergyHandler || obj instanceof IEnergyProvider) {
			if (obj instanceof IEnergyHandler) {
				IEnergyHandler energyHandler = (IEnergyHandler) obj;
				stored = energyHandler.getEnergyStored(null);
				capacity = energyHandler.getMaxEnergyStored(null);
			} else if (obj instanceof IEnergyProvider) {
				IEnergyProvider energyProvider = (IEnergyProvider) obj;
				stored = energyProvider.getEnergyStored(null);
				capacity = energyProvider.getMaxEnergyStored(null);
			} else {
				return response;
			}

			response.put("Stored", stored);
			response.put("Capacity", capacity);
			response.put("StoredPercentage", 0);

			if (capacity > 0) {
				response.put("StoredPercentage", Math.max(Math.min(100, ((100.0 * stored) / capacity)), 0));
			}
		}
		return response;
	}
}
