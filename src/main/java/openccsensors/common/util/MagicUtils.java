package openccsensors.common.util;

import am2.api.power.IPowerNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MagicUtils {
	public static boolean isValidEssenceTarget(Object target) {
		return target != null && target instanceof IPowerNode;
	}
	
	public static Map<String, Object> getMapOfArsMagicaPower(World world, Object obj, boolean additional) {
		if (obj == null || !(obj instanceof TileEntity) || !additional) {
			return Collections.emptyMap();
		}

		HashMap<String, Object> response = new HashMap<String, Object>();

		if (obj instanceof IPowerNode) {
			IPowerNode powerObj = (IPowerNode) obj;
			//response.put("EssenceCharge", powerObj.getCharge());
			response.put("Capacity", powerObj.getCapacity());
			response.put("IsSource", powerObj.isSource());
			response.put("ChargeRate", powerObj.getChargeRate());
		}
		return response;
	}
}
