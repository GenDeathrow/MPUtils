package com.gendeathrow.mputils.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author thislooksfun
 */
public class ContainerNBTBase extends Container
{
	
	IInventory inv = new InventoryBasic("NBTReader", false, 0);
	
	
	public ContainerNBTBase(EntityPlayer player)
	{
		
		this.addSlotToContainer(new CustomSlot(inv, 0, 21, 154)); //Inventory slot
		
		for (int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 56 + (i * 18), 209)); //Hotbar
		}
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(player.inventory, (i * 9) + j + 9, 56 + (j * 18), 151 + (i * 18))); //Main player inventory
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		ItemStack stack = null;
		Slot slot = this.getSlot(slotIndex);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if (slotIndex == 0)
			{
				if (!this.mergeItemStack(stack1, 1, 37, false))
				{
					return null;
				}
			} else if (slotIndex >= 1 && slotIndex <= 36)
			{
				if (!this.mergeItemStack(stack1, 0, 1, false))
				{
					return null;
				}
			}
			
			if (stack1.getCount() == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (stack1.getCount() == stack.getCount())
			{
				return null;
			}
			
			slot.onTake(player, stack);
		}
		
		return stack;
	}
	
	public static class CustomSlot extends Slot
	{
		public CustomSlot(IInventory inv, int invPos, int x, int y)
		{
			super(inv, invPos, x, y);
		}
		
		@Override
		public void putStack(ItemStack p_75215_1_)
		{
			super.putStack(p_75215_1_);
		}
		
		@Override
		public void onSlotChanged()
		{
			super.onSlotChanged();
		}
	}
}