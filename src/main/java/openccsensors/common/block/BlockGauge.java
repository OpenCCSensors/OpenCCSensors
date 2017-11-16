package openccsensors.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import openccsensors.OpenCCSensors;
import openccsensors.common.tileentity.TileEntityGauge;

import javax.annotation.Nonnull;

public class BlockGauge extends BlockContainer {
	public static final IProperty<EnumFacing> PROPERTY_FACING = BlockHorizontal.FACING;

	private static final float WIDTH = 0.125F;

	public BlockGauge() {
		super(Material.GROUND);
		setHardness(0.5F);
		setCreativeTab(OpenCCSensors.tabOpenCCSensors);
		GameRegistry.register(setRegistryName("gauge"));
		GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
		GameRegistry.registerTileEntity(TileEntityGauge.class, "gauge");
		setUnlocalizedName("openccsensors.gauge");
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PROPERTY_FACING);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
		return new TileEntityGauge();
	}

	@Override
	public boolean canPlaceBlockOnSide(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {
		return side.getAxis().isHorizontal() && canPlaceOn(world, pos, side);
	}

	@Override
	public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
		for (EnumFacing dir : EnumFacing.HORIZONTALS) {
			if (canPlaceOn(world, pos, dir)) return true;
		}
		return false;
	}

	private static boolean canPlaceOn(World world, BlockPos pos, EnumFacing side) {
		return world.isSideSolid(pos.offset(side.getOpposite()), side) || world.getTileEntity(pos.offset(side.getOpposite())) != null;
	}

	@Nonnull
	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(PROPERTY_FACING)) {
			default:
			case NORTH:
				return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, WIDTH);
			case SOUTH:
				return new AxisAlignedBB(0.0F, 0.0F, 1.0F - WIDTH, 1.0F, 1.0F, 1.0F);
			case EAST:
				return new AxisAlignedBB(1.0F - WIDTH, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			case WEST:
				return new AxisAlignedBB(0.0F, 0.0F, 0.0F, WIDTH, 1.0F, 1.0F);
		}
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = super.getStateFromMeta(meta);
		if (meta >= 0 && meta < EnumFacing.HORIZONTALS.length) {
			state = state.withProperty(PROPERTY_FACING, EnumFacing.HORIZONTALS[meta]);
		}
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PROPERTY_FACING).getHorizontalIndex();
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, meta, placer);

		if (side.getAxis().isHorizontal() && canPlaceOn(world, pos, side)) {
			state = state.withProperty(PROPERTY_FACING, side.getOpposite());
		}

		return state;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn) {
		EnumFacing infront = state.getValue(PROPERTY_FACING);
		if (world.isAirBlock(pos.offset(infront))) {
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}

		super.neighborChanged(state, world, pos, blockIn);
	}
}
