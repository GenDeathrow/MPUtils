package com.gendeathrow.mpbasic.world;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class WorldHandler {

	@SubscribeEvent
	public static void onSave(WorldEvent.Save event) {
			//SaveData.get(event.getWorld());
	}
	
}
