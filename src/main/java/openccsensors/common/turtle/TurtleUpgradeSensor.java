package openccsensors.common.turtle;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import openccsensors.OpenCCSensors;
import openccsensors.client.ClientProxy;
import openccsensors.common.peripheral.PeripheralSensor;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

public class TurtleUpgradeSensor implements ITurtleUpgrade {

	private ArrayList<PeripheralSensor> peripheral = new ArrayList<PeripheralSensor>();

	public TurtleUpgradeSensor() {
	}

	@Override
	public IPeripheral createPeripheral(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side) {
		this.peripheral.add(new PeripheralSensor(new TurtleSensorEnvironment(turtle), true));
		return this.peripheral.get(this.peripheral.size() - 1);
	}

	@Nonnull
	@Override
	public String getUnlocalisedAdjective() {
		return "turtle.openccsensors.sensor.adjective";
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(OpenCCSensors.Blocks.sensorBlock);
	}

	@Nonnull
	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Nonnull
	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation("openccsensors", "sensor");
	}

	@Override
	public int getLegacyUpgradeID() {
		return 180;
	}

	@Nonnull
	@Override
	public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull EnumFacing direction) {
		return TurtleCommandResult.failure();
	}

	@Nonnull
	@Override
	@SideOnly(Side.CLIENT)
	public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess iTurtleAccess, @Nonnull TurtleSide side) {
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher()
			.getBlockModelShapes().getModelManager().getModel(ClientProxy.SENSOR_DISH_MODEL);

		// Translate [-0.5, -0.5, -0.5]
		// Scale [0.6]
		// Left:  RotateZ  [PI/2], Translate [0.1, 0.5, 0.5]
		// Right: RotateZ [-PI/2], Translate [0.9, 0.5, 0.5]
		float delta = side == TurtleSide.Left ? -1 : 1;
		Matrix4f m = new Matrix4f(
			0.0f, delta * 0.6f, 0.0f, 0.5f + +delta * 0.1f,
			delta * -0.6f, 0.0f, 0.0f, 0.5f + delta * 0.3f,
			0.0f, 0.0f, 0.6f, 0.2f,
			0.0f, 0.0f, 0.0f, 1.0f
		);

		return Pair.of(model, m);
	}

	public void addTurtlesToCreative(List<ItemStack> subItems) {
		for (int i = 0; i <= 7; i++) {
			Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("ComputerCraft", "CC-TurtleExpanded"));
			if (item != null) {
				ItemStack turtle = new ItemStack(item, 1);
				NBTTagCompound tag = turtle.getTagCompound();
				if (tag == null) {
					tag = new NBTTagCompound();
					turtle.writeToNBT(tag);
				}
				tag.setString("leftUpgrade", getUpgradeID().toString());
				tag.setShort("rightUpgrade", (short) i);
				turtle.setTagCompound(tag);
				subItems.add(turtle);
			}
		}
	}

	@Override
	public void update(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side) {
		if (this.peripheral != null) {
			for (PeripheralSensor peripheral : this.peripheral) {
				if (peripheral != null) {
					peripheral.update();
				}
			}
		}
	}

}
