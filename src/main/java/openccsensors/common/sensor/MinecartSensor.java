package openccsensors.common.sensor;

import mods.railcraft.api.carts.IEnergyTransfer;
import mods.railcraft.api.carts.IExplosiveCart;
import mods.railcraft.api.carts.IRoutableCart;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.*;

import java.util.HashMap;
import java.util.Map;

public class MinecartSensor implements ISensor {
	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {

		EntityMinecart minecart = (EntityMinecart) obj;

		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Double> position = new HashMap<String, Double>();

		position.put("X", minecart.posX - sensorPos.getX());
		position.put("Y", minecart.posY - sensorPos.getY());
		position.put("Z", minecart.posZ - sensorPos.getZ());
		response.put("Position", position);

		response.put("Name", minecart.getName());
		response.put("RawName", EntityList.getEntityString(minecart));

		if (minecart instanceof IInventory) {
			response.put("Slots", InventoryUtils.invToMap((IInventory) minecart));
		}

		if (minecart instanceof IFluidHandler) {
			response.put("Tanks", TankUtils.fluidHandlerToMap((IFluidHandler) minecart));
		}

		if (minecart.getRidingEntity() != null && minecart.getRidingEntity() instanceof EntityLivingBase) {
			response.put("Riding", EntityUtils.livingToMap((EntityLivingBase) minecart.getRidingEntity(), sensorPos, true));
		}

		if (Mods.RAIL) {
			if (minecart instanceof IEnergyTransfer) {
				response.putAll(RailcraftUtils.getEnergyDetails(minecart));
			}

			if (minecart instanceof IExplosiveCart) {
				response.putAll(RailcraftUtils.getExplosiveDetails(minecart));
			}

			if (minecart instanceof IRoutableCart) {
				response.put("RouteDestination", ((IRoutableCart) minecart).getDestination());
			}
		}
		return response;
	}

	@Override
	public Map<String, ?> getTargets(World world, BlockPos location, ISensorTier tier) {
		double radius = tier.getMultiplier() * 4;
		return EntityUtils.getEntities(world, location, radius, EntityMinecart.class);
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
		return "minecart_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:items/minecart");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.MINECART);
	}

}
