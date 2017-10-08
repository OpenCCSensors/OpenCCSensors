package openccsensors.api;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISensorEnvironment {
	EnumFacing getFacing();

	BlockPos getLocation();

	ItemStack getSensorCardStack();

	@Nonnull
	World getWorld();
}
