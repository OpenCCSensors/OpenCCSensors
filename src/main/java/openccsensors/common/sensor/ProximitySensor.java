package openccsensors.common.sensor;

import com.google.common.collect.Maps;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.EntityUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProximitySensor implements ISensor {
	public enum Mode implements IStringSerializable {
		ALL("all"),
		PLAYERS("players"),
		OWNER("owner");

		public static final Mode[] VALUES = values();
		private static final Map<String, Mode> nameLookup = Maps.newHashMap();

		private final String lowerName;

		Mode(String lowerName) {
			this.lowerName = lowerName;
		}

		static {
			for (Mode mode : VALUES) nameLookup.put(mode.getName(), mode);
		}

		@Override
		@Nonnull
		public String getName() {
			return lowerName;
		}

		public static Mode byName(String name) {
			return name == null ? null : nameLookup.get(name);
		}
	}

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
		return new ResourceLocation("openccsensors:items/proximity");
	}

	public double getDistanceToNearestEntity(World world, Vec3d location, Mode mode, UUID owner) {
		Class<? extends EntityLivingBase> klazz = EntityLivingBase.class;

		if (mode == Mode.PLAYERS || mode == Mode.OWNER) {
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
			if (mode == Mode.OWNER && !current.getUniqueID().equals(owner)) {
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

