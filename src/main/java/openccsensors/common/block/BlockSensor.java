package openccsensors.common.block;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import openccsensors.OpenCCSensors;
import openccsensors.common.tileentity.TileEntitySensor;
import openccsensors.common.util.MiscUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSensor extends BlockContainer implements IPeripheralProvider {
	public static final IProperty<EnumFacing> PROPERTY_FACING = BlockHorizontal.FACING;

	public BlockSensor() {
		super(Material.GROUND);
		setHardness(0.5F);
		setCreativeTab(OpenCCSensors.tabOpenCCSensors);
		GameRegistry.register(setRegistryName("sensor"));
		GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
		GameRegistry.registerTileEntity(TileEntitySensor.class, "sensor");
		setUnlocalizedName("openccsensors.sensor");
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
		return new TileEntitySensor();
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PROPERTY_FACING);
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = super.getStateFromMeta(meta);
		if (meta >= 0 && meta < EnumFacing.HORIZONTALS.length) {
			state = state.withProperty(PROPERTY_FACING, EnumFacing.HORIZONTALS[meta ]);
		}
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PROPERTY_FACING).getHorizontalIndex();
	}

	@Override
	public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		MiscUtils.dropInventoryItems(world.getTileEntity(pos));
		super.breakBlock(world, pos, state);
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase entity) {
		return super
			.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, entity)
			.withProperty(PROPERTY_FACING, EnumFacing.fromAngle(entity.rotationYaw));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (player.isSneaking()) {
				return false;
			}
			player.openGui(OpenCCSensors.instance, 1987, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return true;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
		TileEntity entity = world.getTileEntity(pos);
		if (entity instanceof TileEntitySensor) {
			return (IPeripheral) entity;
		}
		return null;
	}
}
