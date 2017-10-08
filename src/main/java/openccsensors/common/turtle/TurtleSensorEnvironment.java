package openccsensors.common.turtle;

import javax.annotation.Nonnull;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openccsensors.api.ISensorEnvironment;

public class TurtleSensorEnvironment implements ISensorEnvironment {

	private final ITurtleAccess turtle;

	public TurtleSensorEnvironment(ITurtleAccess _turtle) {
		turtle = _turtle;
	}

	@Override
	public EnumFacing getFacing() {
		return turtle.getDirection();
	}

	@Override
	public BlockPos getLocation() {
		return turtle.getPosition();
	}

	@Override
	public ItemStack getSensorCardStack() {
		IInventory turtleInventory = turtle.getInventory();
		return turtleInventory.getStackInSlot(15);
	}

	@Nonnull
	@Override
	public World getWorld() {
		return turtle.getWorld();
	}

}
