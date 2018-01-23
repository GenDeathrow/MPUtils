package com.gendeathrow.mpbasic;

import com.gendeathrow.mpbasic.configs.MPBConfigHandler;
import com.gendeathrow.mpbasic.core.MPBasic;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BTEventHandler 
{
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equals(MPBasic.MODID))
		{
			MPBConfigHandler.config.save();
			MPBConfigHandler.load();
		}
	}
}
