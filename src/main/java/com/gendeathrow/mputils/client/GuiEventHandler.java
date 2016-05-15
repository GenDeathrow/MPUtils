package com.gendeathrow.mputils.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.mputils.client.elements.SideTabButton;
import com.gendeathrow.mputils.client.windows.GuiChangeLog;
import com.gendeathrow.mputils.client.windows.GuiShowTips;
import com.gendeathrow.mputils.client.windows.GuiSupport;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.modsupport.ccmUtils;

public class GuiEventHandler 
{
	

	SideTabButton changelog;
	SideTabButton tabTips;
	SideTabButton support;
	
	private int yOffset;
	private ArrayList<SideTabButton> tablist = new ArrayList<SideTabButton>();
	
	public void addButtonTab(SideTabButton button)
	{
		button.yPosition = yOffset += button.height + 4;
		
		tablist.add(button);
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void guiIntEvent(InitGuiEvent.Post event)
	{
		if(event.getGui() instanceof GuiMainMenu  || event.getGui() instanceof GuiIngameMenu)
		{
			tablist.clear();
			
			yOffset = (event.getGui().height / 4) + 28;
			
			changelog = new SideTabButton(400, event.getGui().width - 100 + 2, 0, 100, 20, Settings.changeLogTitle).setIcon(0, 0);
			tabTips =  new SideTabButton(401, event.getGui().width - 100 + 2, 0, 100, 20, "HelpFul Tips").setIcon(1, 0);
			//support =  new SideTabButton(402, event.getGui().width - 90 + 2, 0, 90, 20, "Tips").setIcon(2, 0);

			if(Settings.showChangeLogButton) 
			{
				addButtonTab(changelog);
			}
			if(Settings.tipsMenuButton)
			{
				addButtonTab(tabTips); 
			}
			
			
			//addButtonTab(support);
				
			for(SideTabButton tab : tablist)
			{
					event.getButtonList().add(tab);
			}
			

		}
	}
	
	

	
	@SubscribeEvent
	public void action(ActionPerformedEvent.Post event)
	{

		if(event.getGui() instanceof GuiMainMenu || event.getGui() instanceof GuiIngameMenu)
		{
			if(event.getButton() == changelog)
			{
				event.getGui().mc.displayGuiScreen(new GuiChangeLog(event.getGui()));
			}
			else if (event.getButton() == tabTips)
			{
				event.getGui().mc.displayGuiScreen(new GuiShowTips(event.getGui()));
			}
			else if (event.getButton() == support)
			{
				event.getGui().mc.displayGuiScreen(new GuiSupport(event.getGui()));
			}
		}
		
	}
	
}
