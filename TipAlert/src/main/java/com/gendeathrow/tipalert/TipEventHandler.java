package com.gendeathrow.tipalert;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.tipalert.config.TipConfigHandler;
import com.gendeathrow.tipalert.core.TipAlert;

public class TipEventHandler 
{
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equals(TipAlert.MODID))
		{
			TipConfigHandler.config.save();
			TipConfigHandler.load();
		}
	}
}
