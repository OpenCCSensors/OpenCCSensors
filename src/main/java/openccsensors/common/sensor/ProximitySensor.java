package openccsensors.common.sensor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.EntityUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProximitySensor implements ISensor {

	public static final int MODE_ALL = 0;
	public static final int MODE_PLAYERS = 1;
	public static final int MODE_OWNER = 2;

	@Override
	public Map<String, ?> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {
		return EntityUtils.livingToMap((EntityLivingBase) obj, sensorPos, additional);
	}

	@Override
	public Map<String, ?> getTargets(World world, BlockPos location, ISensorTier tier) {
		double radius = tier.getMultiplier() * 4;
		return EntityUtils.getEntities(world, location, radius, EntityLivingBase.class);
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
		return "proximity_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:proximity");
	}

	public double getDistanceToNearestEntity(World world, Vec3d location, int mode, UUID owner) {
		Class<? extends EntityLivingBase> klazz = EntityLivingBase.class;

		if (mode == MODE_PLAYERS || mode == MODE_OWNER) {
			klazz = EntityPlayer.class;
		}

		List<? extends EntityLivingBase> list = world.getEntitiesWithinAABB(
			klazz,
			new AxisAlignedBB(location.xCoord - 16.0F,
				location.yCoord - 16.0F,
				location.zCoord - 16.0F,
				location.xCoord + 16.0F,
				location.yCoord + 16.0F,
				location.zCoord + 16.0F));

		double closestDistance = Double.MAX_VALUE;
		for (EntityLivingBase current : list) {
			if (mode == MODE_OWNER && !current.getUniqueID().equals(owner)) {
				continue;
			}
			Vec3d livingPos = new Vec3d(
				current.posX + 0.5,
				current.posY + 0.5,
				current.posZ + 0.5
			);

			double distanceTo = location.distanceTo(livingPos);
			if (distanceTo <= 16.0) {
				closestDistance = Math.min(distanceTo, closestDistance);
			}
		}
		return closestDistance;
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Blocks.STONE_PRESSURE_PLATE);
	}
}

