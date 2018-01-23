package com.gendeathrow.mputils.client.gui;

import java.util.ArrayList;

import com.gendeathrow.mputils.api.client.gui.elements.SideTabButton;
import com.gendeathrow.mputils.client.TabRegistry;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.utils.MPInfo;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEventHandler 
{
	
	private int yOffset;
	private ArrayList<SideTabButton> tablist = new ArrayList<SideTabButton>();
	
	public static SideTabButton SettingsButton;
	
	public static void addMPUtilsTabs()
	{
		TabRegistry.registerTab(SettingsButton = new SideTabButton(400, "Settings").setIcon(4, 0));
		
	}
	
	@SubscribeEvent
	public void action(ActionPerformedEvent.Post event)
	{
		if(event.getGui() instanceof GuiMainMenu || event.getGui() instanceof GuiIngameMenu)
		{
			if(event.getButton() == SettingsButton)
			{
				event.getGui().mc.displayGuiScreen(new Gui_Settings(event.getGui()));
			}
		}
	}
	
	public void addButtonTab(SideTabButton button)
	{
		button.y = yOffset += button.height + 4;
		
		tablist.add(button);
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void guiIntEvent(InitGuiEvent.Post event)
	{
		if(event.getGui() instanceof GuiMainMenu  || event.getGui() instanceof GuiIngameMenu)
		{
			tablist.clear();
			
			yOffset = (event.getGui().height / 4) + 28;
			
			if(Settings.editMode && !TabRegistry.getActiveTabList().contains(SettingsButton))
			{
				TabRegistry.enableTab(SettingsButton);
			}
			else if(!Settings.editMode && TabRegistry.getActiveTabList().contains(SettingsButton))
			{
				TabRegistry.disableTab(SettingsButton);
			}
			
			for(SideTabButton b : TabRegistry.getActiveTabList())
			{
				b.x = event.getGui().width - b.width + 2;
				addButtonTab(b);
			}
			
			for(SideTabButton tab : tablist)
			{
					event.getButtonList().add(tab);
			}

		}
		
		if(event.getGui() instanceof GuiModList && Settings.useMPInfo)
		{
			MPInfo.post((GuiModList) event.getGui());
		}
		
	}
	
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void guiIntEventPre(InitGuiEvent.Pre event)
	{
		if(event.getGui() instanceof GuiModList && Settings.useMPInfo)
		{
			MPInfo.AddtoGui((GuiModList) event.getGui());
		}
	}
	
	@SubscribeEvent
	public void drawscreen(DrawScreenEvent event)
	{

		if(event.getGui() instanceof GuiModList &&  Settings.useMPInfo)
		{
			MPInfo.DrawScreen((GuiModList)event.getGui());
		}
		
	}
	
//	@SubscribeEvent
//	public void action(ActionPerformedEvent.Post event)
//	{
//
//		if(event.getGui() instanceof GuiMainMenu || event.getGui() instanceof GuiIngameMenu || ccmUtils.isInstanceofCMM(event.getGui()))
//		{
//
//		}
//		
//	}
//	
	
//	@SubscribeEvent
//	public void configChange(OnConfigChangedEvent event)
//	{
//		
//		ConfigHandler.loadConfiguration();
//	}
	

	
}
