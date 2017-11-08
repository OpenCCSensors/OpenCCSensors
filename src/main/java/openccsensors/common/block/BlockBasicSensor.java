package openccsensors.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import openccsensors.OpenCCSensors;
import openccsensors.api.IBasicSensor;
import openccsensors.common.sensor.ProximitySensor;
import openccsensors.common.tileentity.basic.TileEntityBasicProximitySensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockBasicSensor extends BlockContainer {
	private static final PropertyEnum<ProximitySensor.Mode> PROPERTY_MODE = PropertyEnum.create("mode", ProximitySensor.Mode.class);

	public BlockBasicSensor() {
		super(Material.GROUND);
		setHardness(0.5F);
		setCreativeTab(OpenCCSensors.tabOpenCCSensors);
		GameRegistry.register(setRegistryName("basic_proximity_sensor"));
		GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
		GameRegistry.registerTileEntity(TileEntityBasicProximitySensor.class, "basic_proximity_sensor");
		setUnlocalizedName("openccsensors.proximity_sensor");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(new ItemStack(this, 1, 0));
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
		return new TileEntityBasicProximitySensor();
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PROPERTY_MODE);
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
		state = super.getExtendedState(state, world, pos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityBasicProximitySensor) {
			state = state.withProperty(PROPERTY_MODE, ((TileEntityBasicProximitySensor) tile).getEntityMode());
		}

		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	@Deprecated
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		TileEntity tile = world.getTileEntity(pos);
		if ((tile != null) && ((tile instanceof IBasicSensor))) {
			return ((IBasicSensor) tile).getPowerOutput();
		}
		return 0;
	}

	@Override
	@Deprecated
	public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return getWeakPower(state, world, pos, side);
	}

	@Override
	public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		world.notifyNeighborsOfStateChange(pos, this);
		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityBasicProximitySensor && placer instanceof EntityPlayerMP && !(placer instanceof FakePlayer)) {
			((TileEntityBasicProximitySensor) tile).setOwner(placer.getUniqueID());
		}
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (player.isSneaking()) {
				return false;
			}
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof TileEntityBasicProximitySensor) {
				((TileEntityBasicProximitySensor) tile).onBlockClicked(player);
			}
		}
		return true;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
