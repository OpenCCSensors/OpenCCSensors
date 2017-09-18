package openccsensors.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public interface ISensorEnvironment {
	int getFacing();

	ChunkCoordinates getLocation();

	ItemStack getSensorCardStack();

	World getWorld();
}
