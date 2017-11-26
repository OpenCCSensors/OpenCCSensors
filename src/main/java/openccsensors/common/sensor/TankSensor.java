package openccsensors.common.sensor;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import openccsensors.api.IGaugeSensor;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.TankUtils;

import java.util.HashMap;
import java.util.Map;

public class TankSensor extends TileSensor implements ISensor, IGaugeSensor {
	private String[] gaugeProperties = new String[]{
		"PercentFull"
	};

	@Override
	public boolean isValidTarget(Object tile) {
		IFluidHandler handler = TankUtils.getHandler(tile);
		return handler != null && handler.getTankProperties().length != 0;
	}

	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {
		TileEntity tile = (TileEntity) obj;
		HashMap<String, Object> response = super.getDetails(tile, sensorPos);
		IFluidHandler handler = TankUtils.getHandler(tile);
		response.put("Tanks", TankUtils.fluidHandlerToMap(handler));
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
		return "tank_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:items/tank");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.BUCKET);
	}

	@Override
	public String[] getGaugeProperties() {
		return this.gaugeProperties;
	}

}
