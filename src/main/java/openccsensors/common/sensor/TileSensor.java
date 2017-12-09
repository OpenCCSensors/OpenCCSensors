package openccsensors.common.sensor;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.InventoryUtils;

import java.util.HashMap;

public abstract class TileSensor implements ISensor {
	public boolean isValidTarget(Object target) {
		return false;
	}

	public HashMap<String, Object> getDetails(TileEntity tile, BlockPos sensorPos) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Integer> position = new HashMap<String, Integer>();

		position.put("X", tile.getPos().getX() - sensorPos.getX());
		position.put("Y", tile.getPos().getY() - sensorPos.getX());
		position.put("Z", tile.getPos().getZ() - sensorPos.getZ());
		response.put("Position", position);

		ItemStack stack = new ItemStack(tile.getBlockType(), 1, tile.getBlockMetadata());
		response.put("Name", tile.getBlockType().getRegistryName());
		response.put("DisplayName", InventoryUtils.getDisplayNameForItemStack(stack));
		response.put("RawName", InventoryUtils.getRawNameForStack(stack));

		return response;
	}

	public HashMap<String, Object> getTargets(World world, BlockPos location, ISensorTier tier) {
		HashMap<String, Object> targets = new HashMap<String, Object>();
		int distance = (int) tier.getMultiplier();

		for (int x = -distance; x <= distance; x++) {
			for (int y = -distance; y <= distance; y++) {
				for (int z = -distance; z <= distance; z++) {
					String name = String.format("%s,%s,%s", x, y, z);
					TileEntity tile = world.getTileEntity(location.add(x, y, z));

					if (isValidTarget(tile)) {
						targets.put(name, tile);
					}

				}
			}
		}

		return targets;
	}
}
