package openccsensors.common.sensor;

import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.Ic2Utils;
import openccsensors.common.util.Mods;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CropSensor implements ISensor {

	public static final String STATUS_NEW = "New";
	public static final String STATUS_GROWING = "Growing";
	public static final String STATUS_GROWN = "Grown";

	class CropTarget {
		public BlockPos pos;
		public IBlockState blockState;

		public CropTarget(BlockPos pos, IBlockState b) {
			this.pos = pos;
			this.blockState = b;
		}
	}

	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {

		HashMap<String, Object> response = new HashMap<String, Object>();

		if (obj instanceof CropTarget) {

			CropTarget target = (CropTarget) obj;
			Block block = target.blockState.getBlock();
			Collection<IProperty<?>> properties = target.blockState.getPropertyNames();

			HashMap<String, Integer> position = new HashMap<String, Integer>();
			position.put("X", target.pos.getX() - sensorPos.getX());
			position.put("Y", target.pos.getY() - sensorPos.getY());
			position.put("Z", target.pos.getZ() - sensorPos.getZ());
			response.put("Position", position);

			response.put("Name", target.blockState.getBlock().getLocalizedName());
			response.put("RawName", target.blockState.getBlock().getUnlocalizedName());
			PropertyInteger ageProperty = null;
			if (properties.contains(BlockCrops.AGE)) {
				ageProperty = BlockCrops.AGE;
			} else if (properties.contains(BlockStem.AGE)) {
				ageProperty = BlockStem.AGE;
			} else if (properties.contains(BlockBeetroot.AGE)) {
				ageProperty = BlockBeetroot.AGE;
			} else if (properties.contains(BlockNetherWart.AGE)) {
				ageProperty = BlockNetherWart.AGE;
			}

			if (ageProperty != null) {
				int age = target.blockState.getValue(ageProperty);
				int maxAge = Collections.max(ageProperty.getAllowedValues());

				response.put("Age", age);
				if (age == 0) {
					response.put("Status", STATUS_NEW);
				} else if (age == maxAge) {
					response.put("Status", STATUS_GROWN);
				} else {
					response.put("Status", STATUS_GROWING);
				}
			} else if (target.blockState instanceof BlockPumpkin || target.blockState instanceof BlockMelon) {
				response.put("Status", STATUS_GROWN);
			}
		} else if (Mods.IC2 && obj instanceof TileEntity) {
			response.putAll(Ic2Utils.getCropDetails(obj, sensorPos, additional));
		}

		return response;

	}

	@Override
	public String[] getCustomMethods(ISensorTier tier) {
		return null;
	}

	@Override
	public Object callCustomMethod(World world, BlockPos location, int methodID, Object[] args, ISensorTier tier) throws Exception {
		return null;
	}

	@Override
	public String getName() {
		return "crop_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:items/crop");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.WHEAT);
	}

	@Override
	public Map<String, Object> getTargets(World world, BlockPos location, ISensorTier tier) {

		HashMap<String, Object> targets = new HashMap<String, Object>();
		int distance = (int) tier.getMultiplier();

		for (int x = -distance; x <= distance; x++) {
			for (int y = -distance; y <= distance; y++) {
				for (int z = -distance; z <= distance; z++) {
					String name = String.format("%s,%s,%s", x, y, z);
					BlockPos pos = location.add(x, y, z);
					IBlockState b = world.getBlockState(pos);

					if ((b.getBlock() instanceof BlockCrops ||
						b.getBlock() instanceof BlockNetherWart ||
						b.getBlock() instanceof BlockStem ||
						b.getBlock() instanceof BlockPumpkin ||
						b.getBlock() instanceof BlockMelon
					)) {
						CropTarget potentialTarget = new CropTarget(pos, b);
						targets.put(name, potentialTarget);
					} else {
						TileEntity tile = world.getTileEntity(pos);
						if (Mods.IC2 && Ic2Utils.isValidCropTarget(tile)) {
							targets.put(name, tile);
						}

					}
				}
			}
		}
		return targets;
	}

}
