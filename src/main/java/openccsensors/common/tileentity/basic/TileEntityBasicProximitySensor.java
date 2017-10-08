package openccsensors.common.tileentity.basic;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import openccsensors.OpenCCSensors;
import openccsensors.api.IBasicSensor;
import openccsensors.common.sensor.ProximitySensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class TileEntityBasicProximitySensor extends TileEntity implements IBasicSensor, ITickable {

	private Vec3d blockPos = null;

	private int previousOutput = Integer.MIN_VALUE;
	private int output = 0;

	private UUID owner = null;

	private boolean sentFirstChange = false;

	private int entityMode = ProximitySensor.MODE_ALL;

	@Override
	public void update() {
		if (!worldObj.isRemote) {

			boolean flag = false;

			if (blockPos == null) {
				blockPos = new Vec3d(pos);
			}

			double distance = OpenCCSensors.Sensors.proximitySensor.getDistanceToNearestEntity(
				worldObj,
				blockPos,
				entityMode,
				owner
			);

			output = MathHelper.clamp_int(15 - ((Double) distance).intValue(), 0, 15);

			if (output != previousOutput || !sentFirstChange) {
				sentFirstChange = true;
				flag = true;
			}

			previousOutput = output;

			if (flag) {

				Block blockId = OpenCCSensors.Blocks.basicSensorBlock;

				markBlockForUpdate();
				worldObj.notifyNeighborsOfStateChange(pos, blockId);
			}
		}
	}

	public int getEntityMode() {
		return entityMode;
	}

	public void onBlockClicked(EntityPlayer player) {
		if (player.getUniqueID().equals(owner)) {
			entityMode++;
			if (entityMode > 2) {
				entityMode = 0;
			}
			String modeMsg = "";
			switch (entityMode) {
				case ProximitySensor.MODE_ALL:
					modeMsg = "Any Living Entity";
					break;
				case ProximitySensor.MODE_PLAYERS:
					modeMsg = "Any Player";
					break;
				case ProximitySensor.MODE_OWNER:
					modeMsg = "Owner Only";
					break;
			}
			markBlockForUpdate();
			player.addChatMessage(new TextComponentString(String.format("Changing sensor mode to \"%s\"", modeMsg)));
		}
	}

	@Override
	public int getPowerOutput() {
		return output;
	}

	private void markBlockForUpdate() {
		BlockPos pos = getPos();
		IBlockState state = worldObj.getBlockState(pos);
		worldObj.notifyBlockUpdate(getPos(), state, state, 3);
	}

	@Override
	public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
		readFromNBT(tag);
		markBlockForUpdate();
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
		this.entityMode = tag.getInteger("entityMode");
		this.owner = tag.hasKey("owner") ? UUID.fromString(tag.getString("owner")) : null;
		super.readFromNBT(tag);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("entityMode", this.entityMode);
		if (this.owner != null) tag.setString("owner", this.owner.toString());
		return super.writeToNBT(tag);
	}

	public void setOwner(UUID _owner) {
		this.owner = _owner;
	}
}
