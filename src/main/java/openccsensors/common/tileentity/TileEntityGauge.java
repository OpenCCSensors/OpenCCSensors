package openccsensors.common.tileentity;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import openccsensors.OpenCCSensors;
import openccsensors.api.IGaugeSensor;
import openccsensors.api.IMethodCallback;
import openccsensors.common.block.BlockGauge;
import openccsensors.common.util.CallbackEventManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TileEntityGauge extends TileEntity implements IPeripheral, ITickable {
	private static ArrayList<IGaugeSensor> gaugeSensors = new ArrayList<IGaugeSensor>();

	public static void addGaugeSensor(IGaugeSensor sensor) {
		gaugeSensors.add(sensor);
	}

	public static ArrayList<IGaugeSensor> getGaugeSensors() {
		return gaugeSensors;
	}

	private HashMap<String, Object> tileProperties = new HashMap<String, Object>();
	private CallbackEventManager eventManager = new CallbackEventManager();
	private int percentage = 0;
	private String updatePropertyName = "";

	private int lastBroadcast = 1;

	public TileEntityGauge() {
		eventManager.registerCallback(new IMethodCallback() {

			@Override
			public String getMethodName() {
				return "getPercentage";
			}

			@Override
			public Object execute(IComputerAccess computer, Object[] arguments)
				throws Exception {
				return getPercentage();
			}

		});

		eventManager.registerCallback(new IMethodCallback() {

			@Override
			public String getMethodName() {
				return "setTrackedProperty";
			}

			@Override
			public Object execute(IComputerAccess computer, Object[] arguments)
				throws Exception {
				updatePropertyName = (String) arguments[0];
				return null;
			}

		});
	}

	@Override
	public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
		readFromNBT(tag);
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		this.percentage = tag.getInteger("percentage");
		this.updatePropertyName = tag.getString("property");
		super.readFromNBT(tag);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("percentage", this.percentage);
		tag.setString("property", this.updatePropertyName);
		return super.writeToNBT(tag);
	}

	private void markBlockForUpdate() {
		BlockPos pos = getPos();
		IBlockState state = worldObj.getBlockState(pos);
		worldObj.notifyBlockUpdate(getPos(), state, state, 3);
	}

	public EnumFacing getFacing() {
		if (worldObj == null || !worldObj.isBlockLoaded(pos)) return EnumFacing.NORTH;
		IBlockState state = worldObj.getBlockState(pos);
		return state.getBlock() instanceof BlockGauge ? state.getValue(BlockGauge.PROPERTY_FACING) : EnumFacing.NORTH;
	}

	public int getPercentage() {
		return percentage;
	}

	@Nonnull
	@Override
	public String getType() {
		return "gauge";
	}

	@Nonnull
	@Override
	public String[] getMethodNames() {
		return eventManager.getMethodNames();
	}

	@Override
	public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException, InterruptedException {
		return new Object[]{
			eventManager.queueMethodCall(computer, method, arguments)
		};
	}

	@Override
	public void attach(@Nonnull IComputerAccess computer) {
		IMount mount = ComputerCraftAPI.createResourceMount(OpenCCSensors.class, "openccsensors", "openccsensors/mods/OCSLua/lua");
		computer.mount("ocs", mount);
	}

	@Override
	public void detach(@Nonnull IComputerAccess computer) {
	}

	private Map<String, ?> checkForKeys(Map<?, ?> data, String[] keys, String prefix) {
		HashMap<String, Object> properties = new HashMap<String, Object>();
		for (Object key : data.keySet()) {
			Object value = data.get(key);
			if (value instanceof Map) {
				properties.putAll(checkForKeys((Map<?, ?>) value, keys, prefix + key.toString()));
			} else if (key instanceof String) {
				for (String property : keys) {
					if (property.equals(key)) {
						properties.put(prefix + property, value);
					}
				}
			}
		}
		return properties;
	}

	@Override
	public void update() {
		tileProperties.clear();
		if (this.worldObj != null && !worldObj.isRemote) {

			EnumFacing behind = getFacing();
			TileEntity behindTile = worldObj.getTileEntity(pos.offset(behind));
			if (behindTile != null) {
				for (IGaugeSensor gaugeSensor : gaugeSensors) {
					if (gaugeSensor.isValidTarget(behindTile)) {
						Map<String, ?> details = gaugeSensor.getDetails(worldObj, behindTile, behindTile.getPos(), true);
						tileProperties.putAll(checkForKeys(details, gaugeSensor.getGaugeProperties(), ""));
					}
				}
			}
			percentage = 0;
			if (tileProperties.size() > 0) {
				if (updatePropertyName.equals("") || !tileProperties.containsKey(updatePropertyName)) {
					updatePropertyName = "";
					for (String property : new String[]{
						"HeatPercentage",
						"Progress",
						"StoredPercentage",
						"InventoryPercentFull"
					}) {
						if (updatePropertyName.equals("") && tileProperties.containsKey(property)) {
							updatePropertyName = property;
						}
					}
					if (updatePropertyName.equals("")) {
						Entry<String, Object> entry = tileProperties.entrySet().iterator().next();
						updatePropertyName = entry.getKey();
					}
				}
				percentage = ((Number) (tileProperties.get(updatePropertyName))).intValue();
			}
			if (lastBroadcast++ % 10 == 0) {
				lastBroadcast = 1;
				markBlockForUpdate();
			}
			eventManager.process();
		}
	}

	@Override
	public boolean equals(IPeripheral other) {
		return this == other;
	}
}
