package openccsensors.common.sensor;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;

import java.util.HashMap;
import java.util.Map;

public class SignSensor extends TileSensor implements ISensor {
	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorPos, boolean additional) {

		TileEntitySign sign = (TileEntitySign) obj;
		HashMap<String, Object> response = super.getDetails(sign, sensorPos);
		if (additional) {

			StringBuilder signText = new StringBuilder();
			for (int i = 0; i < sign.signText.length; i++) {
				signText.append(sign.signText[i]);
				if (i < 3 && !sign.signText[i].getUnformattedText().isEmpty()) {
					signText.append(" ");
				}
			}
			response.put("Text", signText.toString().trim());
		}

		return response;
	}

	@Override
	public boolean isValidTarget(Object target) {
		return target instanceof TileEntitySign;
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
		return "sign_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:sign");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.SIGN);
	}

}
