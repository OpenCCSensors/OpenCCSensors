package openccsensors.common.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TankUtils {

	public static Map<Integer, Object> fluidHandlerToMap(IFluidHandler container) {

		FluidTankInfo[] tanks = container.getTankInfo(ForgeDirection.UNKNOWN);

		Map<Integer, Object> allTanks = new HashMap<Integer, Object>();
		int i = 0;
		try {
			if (tanks != null) {
				for (FluidTankInfo tank : tanks) {
					if (tank != null) {
						HashMap<String, Object> tankMap = new HashMap<String, Object>();
						tankMap.put("Capacity", tank.capacity);
						int fluidAmount = 0;
						tankMap.put("Amount", 0);

						FluidStack stack = tank.fluid;

						if (stack != null) {
							Fluid fluid = stack.getFluid();
							tankMap.put("Name", FluidRegistry.getFluidName(stack));
							tankMap.put("RawName", fluid.getUnlocalizedName());
							fluidAmount = stack.amount;
							tankMap.put("Luminousity", fluid.getLuminosity());
							tankMap.put("Temperature", fluid.getTemperature());
							tankMap.put("Viscosity", fluid.getViscosity());
							tankMap.put("IsGaseous", fluid.isGaseous());
						}
						tankMap.put("Amount", fluidAmount);
						tankMap.put("PercentFull", ((double) fluidAmount) * 100d / (double) tank.capacity);

						allTanks.put(++i, tankMap);
					}
				}
			}
		} catch (Exception e) {
		}
		return allTanks;
	}

}
