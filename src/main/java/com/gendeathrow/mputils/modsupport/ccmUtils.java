package com.gendeathrow.mputils.modsupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ccmUtils 
{

	private static boolean cmmExist = false;
	
	
	private static Class cMainMenu;
	private static  Class cFMainMenu;
	
	private static Method getButtonList;
	
    static
	{
	    try
	    {
	      cFMainMenu = Class.forName("lumien.custommainmenu.gui.GuiFakeMain");
	      cMainMenu = Class.forName("lumien.custommainmenu.gui.GuiCustom");
	      
	      //Method getButtonList = cMainMenu.getMethod("getButtonList");
	      
	      cmmExist = true;
	      System.out.println("Custom Main Menu is loaded");
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
    
//    public static List<GuiButton> getButtonList(GuiScreen gui)
//    {
//    	
//    	if(cmmExist && cMainMenu != null && isInstanceofCMM(gui))
//    	{
//    		
//
//    		try 
//    		{
//				return (List<GuiButton>) getButtonList.invoke(gui);
//			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				e.printStackTrace();
//			}
//    	}
//		return null;
//    	
//    }
	  
	  
	  
	  
}
