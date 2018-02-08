package com.gendeathrow.mpbasic.common.infopanel;

import com.gendeathrow.mpbasic.api.IInfoPanelData;
import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerInfoDelay {

	int delay;
	EntityPlayer player;
	boolean isLoaded = false;
	
	public PlayerInfoDelay(int delayIn, EntityPlayer playerIn) {
		delay = delayIn;
		player = playerIn;
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event){
		
		if(event.player.world == null || delay-- > 0 || !(player instanceof EntityPlayerMP)) return;

		if(InfoPanelConfigHandler.hasOnLoginPage()) {
				IInfoPanelData cap = CapabilityInfoPanel.getInfoPanelData(event.player);
				if(cap != null) {
					if(!cap.hasBeenGivinBook(InfoPanelConfigHandler.onLogInLoadInfoPage.getPanelID())) {
						CapabilityInfoPanel.sendToPlayer(InfoPanelConfigHandler.onLogInLoadInfoPage.getPanelID(), (EntityPlayerMP)event.player);
					}
				}
			}
    	MinecraftForge.EVENT_BUS.unregister(this);
	}
}