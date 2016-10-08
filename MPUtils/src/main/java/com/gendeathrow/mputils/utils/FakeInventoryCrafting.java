package com.gendeathrow.mputils.utils;

import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class FakeInventoryCrafting extends InventoryCrafting{

	private ItemStack[] stackList;
	public int inventoryWidth;
	public int inventoryHeight;

	public FakeInventoryCrafting(int width, int height) 
	{
		super(null, width, height);
		
        int i = width * height;
        this.stackList = new ItemStack[i];
        this.inventoryWidth = width;
        this.inventoryHeight = height;
	}
	
    
	@Override
    @Nullable
    public ItemStack getStackInSlot(int index)
    {
        return index >= this.getSizeInventory() ? null : this.stackList[index];
    }

    /**
     * Gets the ItemStack in the slot specified.
     */
    
	@Override
    @Nullable
    public ItemStack getStackInRowAndColumn(int row, int column)
    {
        return row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight ? this.getStackInSlot(row + column * this.inventoryWidth) : null;
    }
    
	@Nullable
    public void setStackInRowAndColumn(int row, int column, @Nullable ItemStack stack)
    {
        if(row >= 0 && row < this.inventoryHeight && column >= 0 && column <= this.inventoryWidth)
        {
        	this.setInventorySlotContents(column + row * this.inventoryWidth, stack);
        }
    }
	
	@Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        this.stackList[index] = stack;
    }
	
	
	public String printOut()
	{
		String output = "";
		for(int row=0; row<this.inventoryHeight; row++)
		{
			output += " [";
			for(int column = 0; column<this.inventoryWidth; column++)
			{
				
				ItemStack stack = this.getStackInRowAndColumn(row, column);
				
				if(stack != null)
				{
					output += " <"+stack.getItem().getRegistryName() + (stack.getMetadata() > 0 ? ":"+ (stack.getMetadata() != Short.MAX_VALUE ?  stack.getMetadata() : "*") : "" )   +">";
				}
				else output+= "null";
				
				output +=  (column<this.inventoryWidth-1 ? "," : "" );
				
			}
			output += "]"+ (row < this.inventoryHeight-1 ? "," : "" );
		}
		return output;
	}


}
