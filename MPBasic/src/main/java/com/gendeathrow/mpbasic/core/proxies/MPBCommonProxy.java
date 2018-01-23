package com.gendeathrow.mpbasic.core.proxies;

import com.gendeathrow.mpbasic.BTEventHandler;
import com.gendeathrow.mpbasic.commands.MPInfoPanelCommand;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class MPBCommonProxy
{
	public boolean isClient()
	{
		return false;
	}
	
	public boolean isOpenToLAN()
	{
		return false;
	}
	
	public void registerTickHandlers() 
	{
	
	}
	
	public void registerEventHandlers()
	{
		BTEventHandler eventhandler = new BTEventHandler();
		MinecraftForge.EVENT_BUS.register(eventhandler);
	}
	
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}
	
	public void init(FMLInitializationEvent event)
	{
		MPInfoPanelCommand.register();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
