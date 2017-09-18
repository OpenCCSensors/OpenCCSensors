package openccsensors.api;

import java.util.HashMap;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public interface IGaugeSensor {
	String[] getGaugeProperties();

	boolean isValidTarget(Object obj);

	HashMap getDetails(World world, Object obj, ChunkCoordinates location, boolean additional);
}
