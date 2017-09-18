package openccsensors.common.sensor;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import openccsensors.api.IRequiresIconLoading;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;

public class SignSensor extends TileSensor implements ISensor, IRequiresIconLoading {

	private IIcon icon;

	@Override
	public Map<String, Object> getDetails(World world, Object obj, ChunkCoordinates sensorPos, boolean additional) {

		TileEntitySign sign = (TileEntitySign) obj;
		HashMap<String, Object> response = super.getDetails(sign, sensorPos);
		if (additional) {

			StringBuilder signText = new StringBuilder();
			for (int i = 0; i < sign.signText.length; i++) {
				signText.append(sign.signText[i]);
				if (i < 3 && !sign.signText[i].equals("")) {
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
	public Object callCustomMethod(World world, ChunkCoordinates location, int methodID, Object[] args, ISensorTier tier) {
		return null;
	}

	@Override
	public String getName() {
		return "signCard";
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}

	@Override
	public void loadIcon(IIconRegister iconRegistry) {
		icon = iconRegistry.registerIcon("openccsensors:sign");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack((Item) Item.itemRegistry.getObject("sign"));
	}

}
