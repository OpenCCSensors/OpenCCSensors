package openccsensors.common.util;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityUtils {


	public static HashMap<String, Entity> getEntities(World world, BlockPos location, double radius, Class<? extends Entity> filter) {
		HashMap<String, Entity> map = new HashMap<String, Entity>();
		int x = location.getX();
		int y = location.getY();
		int z = location.getZ();

		for (Entity entity : world.getEntitiesWithinAABB(filter, new AxisAlignedBB(
			x - radius, y - radius, z - radius,
			x + radius, y + radius, z + radius
		))) {
			Vec3d livingPos = new Vec3d(
				entity.posX + 0.5,
				entity.posY + 0.5,
				entity.posZ + 0.5
			);
			if ((new Vec3d((double) location.getX(), (double) location.getY(), (double) location.getZ())).distanceTo(livingPos) <= radius && filter.isAssignableFrom(entity.getClass())) {
				String targetName = (entity instanceof EntityPlayer) ? entity
					.getName() : entity
					.getName() + entity.getEntityId();
				targetName = targetName.replaceAll("\\s", "");
				map.put(targetName, entity);
			}
		}

		return map;
	}


	public static HashMap<String, Object> livingToMap(EntityLivingBase living, BlockPos sensorPos, boolean additional) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		HashMap<String, Double> position = new HashMap<String, Double>();
		position.put("X", living.posX - sensorPos.getX());
		position.put("Y", living.posY - sensorPos.getY());
		position.put("Z", living.posZ - sensorPos.getZ());
		map.put("Position", position);

		map.put("Name", (living instanceof EntityPlayerMP) ? "Player" : living.getName());
		map.put("RawName", living.getClass().getName());
		map.put("IsPlayer", living instanceof EntityPlayerMP);

		if (additional) {

			map.put("HeldItem", InventoryUtils.itemstackToMap(living.getHeldItem(EnumHand.MAIN_HAND)));
			map.put("OffHandItem", InventoryUtils.itemstackToMap(living.getHeldItem(EnumHand.OFF_HAND)));

			HashMap<String, Object> armour = new HashMap<String, Object>();
			armour.put("Boots", InventoryUtils.itemstackToMap(living.getItemStackFromSlot(EntityEquipmentSlot.FEET)));
			armour.put("Leggings", InventoryUtils.itemstackToMap(living.getItemStackFromSlot(EntityEquipmentSlot.LEGS)));
			armour.put("Chestplate", InventoryUtils.itemstackToMap(living.getItemStackFromSlot(EntityEquipmentSlot.CHEST)));
			armour.put("Helmet", InventoryUtils.itemstackToMap(living.getItemStackFromSlot(EntityEquipmentSlot.HEAD)));

			map.put("Armour", armour);
			map.put("Health", living.getHealth());
			map.put("IsAirborne", !living.onGround);
			map.put("IsBurning", living.isBurning());
			map.put("IsAlive", living.isEntityAlive());
			map.put("IsInWater", living.isInWater());
			map.put("IsOnLadder", living.isOnLadder());
			map.put("IsSleeping", living.isPlayerSleeping());
			map.put("IsRiding", living.isRiding());
			map.put("IsSneaking", living.isSneaking());
			map.put("IsSprinting", living.isSprinting());
			map.put("IsWet", living.isWet());
			map.put("IsChild", living.isChild());
			map.put("Yaw", living.rotationYaw);
			map.put("Pitch", living.rotationPitch);
			map.put("YawHead", living.rotationYawHead);

			HashMap<Integer, String> potionEffects = new HashMap<Integer, String>();
			Collection<PotionEffect> effects = living.getActivePotionEffects();
			int count = 1;
			for (PotionEffect effect : effects) {
				potionEffects.put(count, effect.getEffectName());
				count++;
			}
			map.put("PotionEffects", potionEffects);
		}

		if (living instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) living;
			map.put("Username", player.getName());
			map.put("ExperienceTotal", player.experienceTotal);
			map.put("ExperienceLevel", player.experienceLevel);
			map.put("Experience", player.experience);
			if (additional) {
				map.put("FoodLevel", player.getFoodStats().getFoodLevel());
				map.put("Gamemode", player.capabilities.isCreativeMode);
				map.put("Inventory", InventoryUtils.invToMap(player.inventory));
			}

			map.put("Experience", player.experience);
		}

		if (living instanceof EntityTameable) {
			EntityTameable tameable = (EntityTameable) living;
			map.put("IsSitting", tameable.isSitting());
			map.put("IsTamed", tameable.isTamed());
			if (tameable.isTamed()) {
				map.put("OwnerName", tameable.getOwner().getName());
			}
			if (tameable instanceof EntityWolf) {
				EntityWolf wolf = (EntityWolf) tameable;
				map.put("IsAngry", wolf.isAngry());
				if (wolf.isTamed()) {
					map.put("CollarColor", wolf.getCollarColor());
				}
			}
		}


		return map;
	}
}
