package openccsensors.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import am2.api.power.IPowerNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;

public class MagicUtils {

	public static boolean isValidAspectTarget(Object target) {
		return target != null && target instanceof IAspectContainer;
	}

	public static boolean isValidEssenceTarget(Object target) {
		return target != null && target instanceof IPowerNode;
	}

	public static Map<String, Object> getMapOfAspects(World world, Object obj, boolean additional) {
		if (obj == null || !(obj instanceof TileEntity) || !additional) {
			return Collections.emptyMap();
		}

		HashMap<String, Object> response = new HashMap<String, Object>();

		if (obj instanceof IAspectContainer) {
			IAspectContainer aspectContainer = (IAspectContainer) obj;
			AspectList aspectList = aspectContainer.getAspects();
			if (aspectList != null) {
				Aspect[] aspects = aspectList.getAspects();
				for (int i = 0; i < aspectList.size(); i++) {
					response.put(aspects[i].getName(), aspectList.getAmount(aspects[i]));
				}
			}
		}
		return response;
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
