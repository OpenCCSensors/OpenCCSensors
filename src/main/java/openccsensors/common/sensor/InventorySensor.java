package openccsensors.common.sensor;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.IGaugeSensor;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.AppliedEnergisticsUtils;
import openccsensors.common.util.InventoryUtils;
import openccsensors.common.util.Mods;

import java.util.HashMap;
import java.util.Map;

public class InventorySensor extends TileSensor implements ISensor, IGaugeSensor {
	private String[] gaugeProperties = new String[]{
		"InventoryPercentFull"
	};

	@Override
	public boolean isValidTarget(Object target) {
		// TODO: Capabilities
		return target instanceof IInventory || (Mods.AE && AppliedEnergisticsUtils.isValidTarget(target));
	}

	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {

		TileEntity tile = (TileEntity) obj;

		HashMap<String, Object> response = super.getDetails(tile, sensorPos);

		if (Mods.AE && AppliedEnergisticsUtils.isValidTarget(obj)) {
			response.putAll(AppliedEnergisticsUtils.getTileDetails(obj, additional));
		} else {
			response.putAll(InventoryUtils.getInventorySizeCalculations((IInventory) tile));
			if (additional) {
				response.put("Slots", InventoryUtils.invToMap((IInventory) tile));
			}
		}

		return response;
	}

	@Override
	public String[] getCustomMethods(ISensorTier tier) {
		return new String[]{
			"getMapData"
			/*
				"getBeeInfo",
				"getMystcraftBookInfo"
			 */
		};
	}

	@Override
	public Object callCustomMethod(World world, BlockPos location, int methodID, Object[] args, ISensorTier tier) throws Exception {

		if (args.length != 2) {
			throw new Exception("This method expects two parameters");
		}

		if (args[1] instanceof Double) {
			args[1] = ((Double) args[1]).intValue();
		}

		if (!(args[0] instanceof String) || !(args[1] instanceof Integer)) {
			throw new Exception("Incorrect parameters. It should be target name, then slot number");
		}

		String targetName = (String) args[0];
		int slot = ((Integer) args[1]) - 1;

		HashMap targets = getTargets(world, location, tier);

		switch (methodID) {
			case 0:
				return InventoryUtils.getMapData(world, targets, targetName, slot);
			case 1:
				return null;
			case 2:
				return null;
		}
		return null;
	}

	@Override
	public String getName() {
		return "inventory_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:inventory");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Blocks.CHEST);
	}

	@Override
	public String[] getGaugeProperties() {
		return gaugeProperties;
	}

}
