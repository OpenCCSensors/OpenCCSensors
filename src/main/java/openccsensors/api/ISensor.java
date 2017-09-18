package openccsensors.api;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public interface ISensor {
	Map<String, ?> getDetails(World world, Object obj, ChunkCoordinates location, boolean additional);

	Map<String, ?> getTargets(World world, ChunkCoordinates location, ISensorTier tier);

	String[] getCustomMethods(ISensorTier tier);

	Object callCustomMethod(World world, ChunkCoordinates location, int methodID, Object[] args, ISensorTier tier) throws Exception;

	String getName();

	IIcon getIcon();

	ItemStack getUniqueRecipeItem();
}
