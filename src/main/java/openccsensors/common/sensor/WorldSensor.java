package openccsensors.common.sensor;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;

import java.util.HashMap;
import java.util.Map;

public class WorldSensor implements ISensor {

	@Override
	public Map<String, Object> getDetails(World world, Object obj, BlockPos sensorLocation, boolean additional) {

		HashMap<String, Object> response = new HashMap<String, Object>();

		response.put("Dimension", world.provider.getDimension());
		response.put("Biome", world.getBiome(sensorLocation).getBiomeName());
		response.put("LightLevel", world.getLight(sensorLocation));
		response.put("Raining", world.isRaining());
		response.put("Thundering", world.isThundering());
		response.put("Daytime", world.isDaytime());
		response.put("MoonPhase", world.getCurrentMoonPhaseFactor());
		response.put("CelestialAngle", world.getCelestialAngle(1.0F));

		return response;
	}

	@Override
	public Map<String, Object> getTargets(World world, BlockPos location, ISensorTier tier) {
		HashMap<String, Object> targets = new HashMap<String, Object>();
		targets.put("CURRENT", location);
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
		return "world_card";
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("openccsensors:items/world");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.ENDER_PEARL);
	}

}
