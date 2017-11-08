package openccsensors.common.sensor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;

import java.util.HashMap;
import java.util.Map;

public class SonicSensor implements ISensor {
	private static final int BASE_RANGE = 1;

	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {
		Vec3d target = (Vec3d) obj;
		int x = (int) target.xCoord;
		int y = (int) target.yCoord;
		int z = (int) target.zCoord;

		IBlockState block = world.getBlockState(new BlockPos(target));

		HashMap<String, Object> response = new HashMap<String, Object>();

		String type = "UNKNOWN";

		if (block.getMaterial().isLiquid()) {
			type = "LIQUID";
		} else if (block.getMaterial().isSolid()) {
			type = "SOLID";
		}

		response.put("Type", type);
		HashMap<String, Integer> position = new HashMap<String, Integer>();
		position.put("X", x - sensorPos.getX());
		position.put("Y", y - sensorPos.getY());
		position.put("Z", z - sensorPos.getZ());
		response.put("Position", position);

		return response;
	}

	@Override
	public Map<String, Object> getTargets(World world, BlockPos location, ISensorTier tier) {

		HashMap<String, Object> targets = new HashMap<String, Object>();

		int range = (new Double(tier.getMultiplier())).intValue() + BASE_RANGE;

		int sx = location.getX();
		int sy = location.getY();
		int sz = location.getZ();

		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {

					BlockPos pos = new BlockPos(sx + x, sy + y, sz + z);
					if (!(x == 0 && y == 0 && z == 0) && world.isBlockLoaded(pos)) {
						if (!world.isAirBlock(pos)) {
							Vec3d targetPos = new Vec3d(pos);
							if ((new Vec3d(location).distanceTo(targetPos) <= range)) {
								targets.put(String.format("%s,%s,%s", x, y, z), targetPos);
							}

						}
					}
				}
			}
		}
		return targets;
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
		return "sonic_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:items/sonic");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Blocks.JUKEBOX);
	}

}
