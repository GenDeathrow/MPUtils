package com.gendeathrow.mputils.items;

import com.gendeathrow.mputils.client.gui.StringFormattor;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Tablet extends Item 
{

	public Tablet()
	{
		super();
		
		this.setUnlocalizedName("mputiltablet");
//		this.setCreativeTab(CreativeTabs.tabTools);
//		this.setTextureName(MPUtils.MODID + ":tablet");
		this.setMaxStackSize(1);
	}
	

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	if(world.isRemote)
    	{
    		Minecraft.getMinecraft().displayGuiScreen(new StringFormattor());

    		
    	}
        return stack;
    }
	
}
