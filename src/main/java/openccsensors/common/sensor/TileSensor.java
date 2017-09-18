package openccsensors.common.sensor;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.InventoryUtils;

public abstract class TileSensor {


	public boolean isValidTarget(Object target) {
		return false;
	}

	public HashMap<String, Object> getDetails(TileEntity tile, ChunkCoordinates sensorPos) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Integer> position = new HashMap<String, Integer>();

		position.put("X", tile.xCoord - sensorPos.posX);
		position.put("Y", tile.yCoord - sensorPos.posY);
		position.put("Z", tile.zCoord - sensorPos.posZ);
		response.put("Position", position);

		ItemStack stack = new ItemStack(tile.getBlockType(), 1, tile.getBlockMetadata());

		response.put("Name", InventoryUtils.getNameForItemStack(stack));
		response.put("RawName", InventoryUtils.getRawNameForStack(stack));
		response.put("DamageValue", stack.getItemDamage());

		return response;
	}

	public HashMap<String, Object> getTargets(World world, ChunkCoordinates location, ISensorTier tier) {
		HashMap<String, Object> targets = new HashMap<String, Object>();
		int distance = (int) tier.getMultiplier();

		for (int x = -distance; x <= distance; x++) {
			for (int y = -distance; y <= distance; y++) {
				for (int z = -distance; z <= distance; z++) {

					int tileX = x + location.posX;
					int tileY = y + location.posY;
					int tileZ = z + location.posZ;

					String name = String.format("%s,%s,%s", x, y, z);

					TileEntity tile = world.getTileEntity(tileX, tileY, tileZ);

					if (isValidTarget(tile)) {
						targets.put(name, tile);
					}

				}
			}
		}

		return targets;

	}
}
