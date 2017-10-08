package openccsensors.common.sensor;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.EntityUtils;
import openccsensors.common.util.InventoryUtils;

import java.util.HashMap;
import java.util.Map;

public class DroppedItemSensor implements ISensor {
	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorLocation, boolean additional) {

		EntityItem item = (EntityItem) obj;

		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Double> position = new HashMap<String, Double>();

		position.put("X", item.posX - sensorLocation.getX());
		position.put("Y", item.posY - sensorLocation.getY());
		position.put("Z", item.posZ - sensorLocation.getZ());

		response.put("Position", position);

		ItemStack stack = item.getEntityItem();

		response.put("Name", InventoryUtils.getNameForItemStack(stack));
		response.put("RawName", InventoryUtils.getRawNameForStack(stack));

		if (additional) {

			response.putAll(InventoryUtils.itemstackToMap(stack));
			response.put("IsBurning", item.isBurning());

		}

		return response;
	}

	@Override
	public Map<String, ?> getTargets(World world, BlockPos location, ISensorTier tier) {
		double radius = tier.getMultiplier() * 4;
		return EntityUtils.getEntities(world, location, radius, EntityItem.class);
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
		return "dropped_item_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:dropped_item");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.WHEAT);
	}

}
