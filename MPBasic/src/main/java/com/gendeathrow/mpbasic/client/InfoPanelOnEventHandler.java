package com.gendeathrow.mpbasic.client;

import com.gendeathrow.mpbasic.client.gui.GuiInfoPanel;
import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;
import com.gendeathrow.mpbasic.world.SaveData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
public class InfoPanelOnEventHandler {

	public static boolean isLoaded = false;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onTick(TickEvent.PlayerTickEvent event){
		if(event.side == Side.CLIENT && !isLoaded){
			
			if(InfoPanelConfigHandler.hasOnLoginPage() && !SaveData.get(event.player.world).hasSeenPanel(InfoPanelConfigHandler.onLogInLoadInfoPage)) {
				isLoaded = true;
				Minecraft.getMinecraft().displayGuiScreen(new GuiInfoPanel(new GuiIngameMenu(), InfoPanelConfigHandler.onLogInLoadInfoPage));
			}else {
				isLoaded = true;
			}
		}
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
	
}
