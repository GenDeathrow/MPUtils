package com.gendeathrow.mputils;

import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.MPUtils;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler 
{

	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equals(MPUtils.MODID))
		{
			ConfigHandler.config.save();
			ConfigHandler.load();
		}
	}
}
