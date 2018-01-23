package com.gendeathrow.mputils.client;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.mputils.api.client.gui.elements.SideTabButton;
import com.gendeathrow.mputils.core.MPUtils;

import net.minecraft.nbt.NBTTagCompound;

public class TabRegistry 
{
	protected static List<SideTabButton> tabList = new ArrayList<SideTabButton>();
	protected static boolean initialLoadComplete = false;
	
	protected static List<SideTabButton> tabListActive = new ArrayList<SideTabButton>();
	
	
	public static void registerTab(SideTabButton button)
	{
        if (button.id <= 25 && initialLoadComplete)
        {
            MPUtils.logger.info("Rejecting " + button.displayString + " due to invalid ID.");
        }
        if (!tabList.contains(button)) 
        {
        	tabList.add(button);

        	enableTab(button);
        }
	}
	
	public static List<SideTabButton> getTabList() 
	{
	        return tabList;
	}
	
	public static List<SideTabButton> getActiveTabList() 
	{
	        return tabListActive;
	}
	
	public static void enableTab(SideTabButton button) 
	{
		 if (tabList.contains(button) && !tabListActive.contains(button)) 
		 {
			 	tabListActive.add(button);
		 }
	}
	
	public static void disableTab(SideTabButton button)
	{
		if(tabListActive.contains(button))
		{
			tabListActive.remove(button);
		}
	}
	
	protected static void readFromNBT(NBTTagCompound nbt)
	{
		
	}
	
	protected static void writeToNBT(NBTTagCompound nbt)
	{
		
	}
	
    public static void setInitialLoadComplete(boolean b) 
    {
        initialLoadComplete = b;
    }
}
