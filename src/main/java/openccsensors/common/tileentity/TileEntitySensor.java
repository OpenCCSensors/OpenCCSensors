package openccsensors.common.tileentity;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import openccsensors.api.ISensorEnvironment;
import openccsensors.common.block.BlockSensor;
import openccsensors.common.peripheral.PeripheralSensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitySensor extends TileEntity implements ISensorEnvironment, IPeripheral, IInventory, ITickable {

	private PeripheralSensor peripheral;

	private IInventory inventory;

	private float rotation;
	private final static float rotationSpeed = 3.0F;

	public TileEntitySensor() {
		peripheral = new PeripheralSensor(this, false);
		inventory = new InventoryBasic("Sensor", true, 1);
		rotation = 0;
	}

	public float getRotation() {
		return rotation;
	}

	/* Tile entity overrides */

	@Override
	public void update() {
		peripheral.update();
		rotation = (rotation + rotationSpeed) % 360;
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
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTTagCompound item = compound.getCompoundTag("item");
		inventory.setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(item));
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagCompound item = new NBTTagCompound();
		ItemStack sensorStack = inventory.getStackInSlot(0);
		if (sensorStack != null) {
			sensorStack.writeToNBT(item);
		}
		tag.setTag("item", item);
		return super.writeToNBT(tag);
	}

	private void markBlockForUpdate() {
		BlockPos pos = getPos();
		IBlockState state = worldObj.getBlockState(pos);
		worldObj.notifyBlockUpdate(getPos(), state, state, 3);
	}

	/* Inventory proxy methods */

	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return inventory.decrStackSize(i, j);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory.setInventorySlotContents(i, itemstack);
		markBlockForUpdate();
	}

	@Nonnull
	@Override
	public String getName() {
		return inventory.getName();
	}

	@Override
	public boolean hasCustomName() {
		return inventory.hasCustomName();
	}

	@Override
	public int getInventoryStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(@Nonnull EntityPlayer entityplayer) {
		return inventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public boolean isItemValidForSlot(int i, @Nonnull ItemStack itemstack) {
		return inventory.isItemValidForSlot(i, itemstack);
	}

	@Nullable
	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack removed = inventory.removeStackFromSlot(index);
		if (removed != null) markBlockForUpdate();
		return removed;
	}

	@Override
	public void openInventory(@Nonnull EntityPlayer player) {
		inventory.openInventory(player);
	}

	@Override
	public void closeInventory(@Nonnull EntityPlayer player) {
		inventory.closeInventory(player);
	}

	@Override
	public int getField(int id) {
		return inventory.getField(id);
	}

	@Override
	public void setField(int id, int value) {
		inventory.setField(id, value);
	}

	@Override
	public int getFieldCount() {
		return inventory.getFieldCount();
	}

	@Override
	public void clear() {
		inventory.clear();
		markBlockForUpdate();
	}
	
	/* Peripheral proxy methods */

	@Nonnull
	@Override
	public String getType() {
		return peripheral.getType();
	}

	@Nonnull
	@Override
	public String[] getMethodNames() {
		return peripheral.getMethodNames();
	}

	@Override
	public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException, InterruptedException {
		return peripheral.callMethod(computer, context, method, arguments);
	}

	@Override
	public void attach(@Nonnull IComputerAccess computer) {
		peripheral.attach(computer);
	}

	@Override
	public void detach(@Nonnull IComputerAccess computer) {
		peripheral.detach(computer);
	}

	@Override
	public EnumFacing getFacing() {
		if (worldObj == null || !worldObj.isBlockLoaded(pos)) return EnumFacing.NORTH;
		IBlockState state = worldObj.getBlockState(pos);
		return state.getBlock() instanceof BlockSensor ? state.getValue(BlockSensor.PROPERTY_FACING) : EnumFacing.NORTH;
	}

	@Override
	public BlockPos getLocation() {
		return pos;
	}

	@Override
	public ItemStack getSensorCardStack() {
		return getStackInSlot(0);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return this == other;
	}
}
