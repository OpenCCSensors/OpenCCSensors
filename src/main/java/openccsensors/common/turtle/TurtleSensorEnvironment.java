package openccsensors.common.turtle;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import openccsensors.api.ISensorEnvironment;

public class TurtleSensorEnvironment implements ISensorEnvironment {

	ITurtleAccess turtle;

	public TurtleSensorEnvironment(ITurtleAccess _turtle) {
		turtle = _turtle;
	}

	@Override
	public int getFacing() {
		return (turtle.getDirection() + 1) % 4;
	}

	@Override
	public ChunkCoordinates getLocation() {
		return turtle.getPosition();
	}

	@Override
	public ItemStack getSensorCardStack() {
		IInventory turtleInventory = turtle.getInventory();
		return turtleInventory.getStackInSlot(15);
	}

	@Override
	public World getWorld() {
		return turtle.getWorld();
	}

}
