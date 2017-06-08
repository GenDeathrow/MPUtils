package com.gendeathrow.mputils.modsupport;

import net.minecraft.client.gui.GuiScreen;

public class ccmUtils 
{

	private static boolean cmmExist = false;
	
	
	private static Class cMainMenu;
	private static  Class cFMainMenu;
	
    static
	{
	    try
	    {
	      cFMainMenu = Class.forName("lumien.custommainmenu.gui.GuiFakeMain");
	      cMainMenu = Class.forName("lumien.custommainmenu.gui.GuiCustom");
	      
	      cmmExist = true;
	      //System.out.println("Custom Main Menu is loaded");
	    }
	    catch (Throwable localThrowable1) 
	    {

	    }

	}
    
    public static boolean isInstanceofCMM(GuiScreen gui)
    {

    	if(cmmExist && cMainMenu != null)
    	{
    		if(cMainMenu.isInstance(gui) || cFMainMenu.isInstance(gui))
    		{
    			return true;
    		}
    		
    	}

    	
    	
    	return false;
    }
	  
	  
	  
	  
}
