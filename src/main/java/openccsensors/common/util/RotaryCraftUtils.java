package openccsensors.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Reika.RotaryCraft.API.Interfaces.ThermalMachine;
import Reika.RotaryCraft.API.Power.PowerGenerator;
import Reika.RotaryCraft.API.Power.ShaftMachine;
import net.minecraft.world.World;

public class RotaryCraftUtils {

	public static boolean isValidPowerTarget(Object target) {
		return target != null && ((target instanceof ShaftMachine) || (target instanceof PowerGenerator));
	}

	public static boolean isValidMachineTarget(Object target) {
		return target != null && (target instanceof ThermalMachine);
	}

	public static Map<String, Object> getPowerDetails(World world, Object obj, boolean additional) {
		if (obj == null || (!(obj instanceof ShaftMachine) && !(obj instanceof PowerGenerator)) || !additional) {
			return Collections.emptyMap();
		}
		
		HashMap<String, Object> response = new HashMap<String, Object>();

		if (obj instanceof ShaftMachine) {
			ShaftMachine shaftMachine = (ShaftMachine) obj;
			response.put("Speed", shaftMachine.getOmega());
			response.put("Torque", shaftMachine.getTorque());
			response.put("Power", shaftMachine.getPower());
		}
		if (obj instanceof PowerGenerator) {
			PowerGenerator powerGen = (PowerGenerator) obj;
			response.put("PowerOutput", powerGen.getCurrentPower());
			response.put("PowerOutputMax", powerGen.getMaxPower());
		}

		return response;
	}

	public static Map<String, Object> getMachineDetails(World world, Object obj, boolean additional) {
		if (obj == null || !(obj instanceof ThermalMachine) || !additional) {
			return Collections.emptyMap();
		}
		
		HashMap<String, Object> response = new HashMap<String, Object>();

		ThermalMachine machine = (ThermalMachine) obj;
		int heat = machine.getTemperature();
		int maxHeat = machine.getMaxTemperature();
		response.put("Heat", heat);
		response.put("MaxHeat", maxHeat);
		response.put("HeatPercentage", 0);

		if (maxHeat > 0) {
			response.put("HeatPercentage", Math.max(Math.min(100, ((100.0 / maxHeat) * heat)), 0));
		}
		return response;
	}
}
