package openccsensors.api;

import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGaugeSensor {
	String[] getGaugeProperties();

	boolean isValidTarget(Object obj);

	Map<String, ?> getDetails(World world, Object obj, BlockPos location, boolean additional);
}
