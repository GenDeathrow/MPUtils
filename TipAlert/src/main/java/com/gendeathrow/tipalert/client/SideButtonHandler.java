package com.gendeathrow.tipalert.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.mputils.api.client.gui.elements.SideTabButton;
import com.gendeathrow.mputils.client.TabRegistry;
import com.gendeathrow.tipalert.core.TipSettings;

public class SideButtonHandler
{
	public static SideTabButton tipButton;
	
	Minecraft mc = Minecraft.getMinecraft();
	
	public static void RegisterSideButtons()
	{
		TabRegistry.registerTab(tipButton = new SideTabButton(400, "Helpful Tips").setIcon(1, 0));
	
		if(!TipSettings.tipsMenuButton) TabRegistry.disableTab(tipButton);

	}

	@SubscribeEvent
	public void action(ActionPerformedEvent.Post event)
	{
		if(event.getGui() instanceof GuiMainMenu || event.getGui() instanceof GuiIngameMenu)
		{
			if(event.getButton() == tipButton)
			{
				event.getGui().mc.displayGuiScreen(new GuiShowTips(event.getGui()));
			}
		}
	}

}
