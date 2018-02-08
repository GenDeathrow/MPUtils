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
import net.minecraftforge.common.util.FakePlayer;
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
	
	@SideOnly(Side.SERVER)
    @SubscribeEvent
    public static void playerLoggedIn(PlayerLoggedInEvent event)
    {
		
        if ((event.player instanceof FakePlayer) || (event.player.getClass().getName().equals("mezz.jei.util.FakeClientPlayer"))) return;
       
    	if(event.player instanceof EntityPlayerMP) {
    		MinecraftForge.EVENT_BUS.register(new PlayerInfoDelay(10, event.player));
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
