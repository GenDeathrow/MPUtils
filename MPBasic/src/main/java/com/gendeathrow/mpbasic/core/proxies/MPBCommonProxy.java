package com.gendeathrow.mpbasic.core.proxies;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.gendeathrow.mpbasic.BTEventHandler;
import com.gendeathrow.mpbasic.common.capabilities.CapabilityHandler;
import com.gendeathrow.mpbasic.common.capabilities.player.IMPBasic_PlayerData;
import com.gendeathrow.mpbasic.common.capabilities.player.MPBasic_PlayersData;
import com.gendeathrow.mpbasic.common.capabilities.player.MPBasic_PlayersDataStorage;

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
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
	}
	
	public void preInit(FMLPreInitializationEvent event)
	{
		CapabilityManager.INSTANCE.register(IMPBasic_PlayerData.class, new MPBasic_PlayersDataStorage(), MPBasic_PlayersData.class);
		
		
	}
	
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
