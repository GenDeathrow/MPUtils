package com.gendeathrow.mpbasic.common.infopanel;

import com.gendeathrow.mpbasic.api.IInfoPanelData;
import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;
import com.gendeathrow.mpbasic.core.MPBasic;

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
		
		if(event.player.world == null || delay-- > 0 || event.player != player) return;

		if(InfoPanelConfigHandler.hasOnLoginPage()) {
			IInfoPanelData cap = CapabilityInfoPanel.getInfoPanelData(player);
			if(cap != null) {
				if(!cap.hasBeenGivinBook(InfoPanelConfigHandler.onLogInLoadInfoPage.getPanelID())) {
					try {
						if(player instanceof EntityPlayerMP)
							CapabilityInfoPanel.sendToPlayer(InfoPanelConfigHandler.onLogInLoadInfoPage.getPanelID(), (EntityPlayerMP)player);
					}catch(Exception e) {
						MPBasic.logger.error("Error trying send packet for infopanel: ");
						e.printStackTrace();	
					}
				}
			}
		}
	  	MinecraftForge.EVENT_BUS.unregister(this);
	}
}