package openccsensors.api;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISensor {
	Map<String, ?> getDetails(World world, Object obj, BlockPos location, boolean additional);

	Map<String, ?> getTargets(World world, BlockPos location, ISensorTier tier);

	String[] getCustomMethods(ISensorTier tier);

	Object callCustomMethod(World world, BlockPos location, int methodID, Object[] args, ISensorTier tier) throws Exception;

	String getName();

	ResourceLocation getIcon();

	ItemStack getUniqueRecipeItem();
}
