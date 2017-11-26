package openccsensors.common.util;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.HashMap;
import java.util.Map;

public class TankUtils {

	@SuppressWarnings("deprecation")
	public static IFluidHandler getHandler(Object target) {
		if (target instanceof ICapabilityProvider) {
			IFluidHandler handler = ((ICapabilityProvider) target).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			if (handler != null) return handler;
		}

		if (target instanceof IFluidHandler) {
			return new net.minecraftforge.fluids.capability.wrappers.FluidHandlerWrapper((net.minecraftforge.fluids.IFluidHandler) target, null);
		}

		return null;
	}

	public static Map<Integer, Object> fluidHandlerToMap(IFluidHandler container) {
		IFluidTankProperties[] tanks = container.getTankProperties();

		Map<Integer, Object> allTanks = new HashMap<Integer, Object>();
		int i = 0;
		try {
			if (tanks != null) {
				for (IFluidTankProperties tank : tanks) {
					if (tank != null) {
						HashMap<String, Object> tankMap = new HashMap<String, Object>();
						tankMap.put("Capacity", tank.getCapacity());
						int fluidAmount = 0;
						tankMap.put("Amount", 0);

						FluidStack stack = tank.getContents();
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
						tankMap.put("PercentFull", ((double) fluidAmount) * 100d / (double) tank.getCapacity());

						allTanks.put(++i, tankMap);
					}
				}
			}
		} catch (Exception e) {
		}
		return allTanks;
	}

}
