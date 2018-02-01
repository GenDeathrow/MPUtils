package com.gendeathrow.mpbasic.common.infopanel;

import com.gendeathrow.mpbasic.api.IInfoPanelData;
import com.gendeathrow.mpbasic.client.gui.GuiInfoPanel;
import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber()
public class InfoPanelOnHandler {

	//public static boolean isLoaded = false;
	
	
    @SubscribeEvent
    public static void playerLoggedIn(PlayerLoggedInEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new PlayerInfoDelay(10, event.player));
    }
    
	@SideOnly(Side.CLIENT)	
	@SubscribeEvent
	public static void onBookCheck(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getItemStack().getItem() == Items.BOOK && event.getItemStack().hasTagCompound())
		{
			NBTTagCompound tag = event.getItemStack().getTagCompound();
			if(tag.hasKey("infopanel")) {
				if(InfoPanelConfigHandler.PAGES.containsKey(tag.getString("infopanel")))
					Minecraft.getMinecraft().displayGuiScreen(new GuiInfoPanel(null, InfoPanelConfigHandler.PAGES.get(tag.getString("infopanel")), true));
			}
		}
	}
	
	
	public static class PlayerInfoDelay {

		int delay;
		EntityPlayer player;
		boolean isLoaded = false;
		
		public PlayerInfoDelay(int delayIn, EntityPlayer playerIn) {
			delay = delayIn;
			player = playerIn;
		}
		
		@SubscribeEvent
		public void onTick(TickEvent.PlayerTickEvent event){
			
			
			if(event.player.world == null || delay-- > 0) return;

			if(InfoPanelConfigHandler.hasOnLoginPage()) {
					IInfoPanelData cap = CapabilityInfoPanel.getInfoPanelData((EntityPlayerMP)event.player);
					if(cap != null) {
						if(!cap.hasBeenGivinBook(InfoPanelConfigHandler.onLogInLoadInfoPage.getPanelID())) {
							CapabilityInfoPanel.sendToPlayer(InfoPanelConfigHandler.onLogInLoadInfoPage.getPanelID(), (EntityPlayerMP)event.player);
						}
					}
				}
	    	MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
}
