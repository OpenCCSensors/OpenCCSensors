package openccsensors.common.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class MiscUtils {


	public static void dropInventoryItems(TileEntity tileEntity) {

		if (tileEntity != null && tileEntity instanceof IInventory) {
			IInventory inventory = (IInventory) tileEntity;
			Random rand = tileEntity.getWorld().rand;
			for (int i = 0; i < inventory.getSizeInventory(); ++i) {
				ItemStack itemStack = inventory.getStackInSlot(i);
				if (itemStack != null) {
					float var10 = rand.nextFloat() * 0.8F + 0.1F;
					float var11 = rand.nextFloat() * 0.8F + 0.1F;
					EntityItem item;
					for (float j = rand.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; tileEntity.getWorld().spawnEntityInWorld(item)) {
						int var13 = rand.nextInt(21) + 10;

						if (var13 > itemStack.stackSize) {
							var13 = itemStack.stackSize;
						}

						itemStack.stackSize -= var13;
						item = new EntityItem(tileEntity.getWorld(),
							tileEntity.getPos().getX() + var10,
							tileEntity.getPos().getY() + var11,
							tileEntity.getPos().getZ() + j, new ItemStack(
							itemStack.getItem(), var13,
							itemStack.getItemDamage()));
						float var15 = 0.05F;
						item.motionX = (float) rand.nextGaussian() * var15;
						item.motionY = (float) rand.nextGaussian() * var15 + 0.2F;
						item.motionZ = (float) rand.nextGaussian() * var15;

						if (itemStack.hasTagCompound()) {
							item.getEntityItem().setTagCompound(itemStack.getTagCompound().copy());
						}
					}
				}
			}
		}
	}
}
