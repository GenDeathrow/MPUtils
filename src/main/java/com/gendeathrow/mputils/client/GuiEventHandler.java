package com.gendeathrow.mputils.client;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.mputils.api.client.gui.elements.SideTabButton;
import com.gendeathrow.mputils.client.windows.GuiChangeLogWindow;
import com.gendeathrow.mputils.client.windows.GuiSupport;
import com.gendeathrow.mputils.client.windows.GuiTipWindow;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.Settings;

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
	
	

	@SubscribeEvent//(priority=EventPriority.LOWEST)
	public void guiIntEvent(InitGuiEvent.Post event)
	{
		if(event.getGui() instanceof GuiMainMenu  || event.getGui() instanceof GuiIngameMenu)
		{
			tablist.clear();
			
			yOffset = (event.getGui().height / 4) + 28;
			
			changelog = new SideTabButton(400, event.getGui().width - 100 + 2, 0, 100, 20, Settings.changeLogTitle).setIcon(0, 0);
			tabTips =  new SideTabButton(401, event.getGui().width - 100 + 2, 0, 100, 20, "Helpful Tips").setIcon(1, 0);
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
				event.getGui().mc.displayGuiScreen(new GuiChangeLogWindow(event.getGui()));
			}
			else if (event.getButton() == tabTips)
			{
				event.getGui().mc.displayGuiScreen(new GuiTipWindow(event.getGui()));
			}
			else if (event.getButton() == support)
			{
				event.getGui().mc.displayGuiScreen(new GuiSupport(event.getGui()));
			}
		}
		
	}
	
	
	@SubscribeEvent
	public void configChange(OnConfigChangedEvent event)
	{
		
		ConfigHandler.loadConfiguration();
	}
	
	
	
	
	
	
	public class RestrictedButtonList extends ArrayList<GuiButton>
	{
		private static final long serialVersionUID = 1L;
		
		ArrayList<Integer> restricted = new ArrayList<Integer>();
		
		public RestrictedButtonList()
		{
			super();
		}
		
		public RestrictedButtonList(ArrayList<GuiButton> original)
		{
			super(original);
		}
		
		public void setRestriction(int index, boolean state)
		{
			if(state)
			{
				if(restricted.contains(index))
				{
					restricted.add(index);
				}
			} else
			{
				restricted.remove((Integer)index);
			}
		}
		
		@Override
		public GuiButton remove(int index)
		{
			if(restricted.contains(index))
			{
				// This is a restricted object and cannot be removed normally
				
				System.out.println("dont remove"+ index);
				return null;
			}
			System.out.println("remove"+ index);
			
			return super.remove(0);
		}
	}
	
}
