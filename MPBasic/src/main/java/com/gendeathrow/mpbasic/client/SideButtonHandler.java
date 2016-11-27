package com.gendeathrow.mpbasic.client;

import java.net.URI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.mpbasic.client.gui.BugReporterWindow;
import com.gendeathrow.mpbasic.core.MPBSettings;
import com.gendeathrow.mputils.api.client.gui.elements.SideTabButton;
import com.gendeathrow.mputils.client.TabRegistry;
import com.gendeathrow.mputils.core.MPUtils;

public class SideButtonHandler implements GuiYesNoCallback
{
	public static SideTabButton changelogButton;
	public static SideTabButton bugReportButton;
	public static SideTabButton supportButton;
	
	Minecraft mc = Minecraft.getMinecraft();
	
	public static void RegisterSideButtons()
	{
		TabRegistry.registerTab(changelogButton = new SideTabButton(401, MPBSettings.changeLogTitle).setIcon(0, 0));
		TabRegistry.registerTab(bugReportButton = new SideTabButton(402, "Report Bug").setIcon(3, 0));
		TabRegistry.registerTab(supportButton = new SideTabButton(403, "Support").setIcon(2, 0));
		
		if(!MPBSettings.showChangeLogButton) TabRegistry.disableTab(changelogButton);
		if(!MPBSettings.showBugReporter) TabRegistry.disableTab(bugReportButton);
		if(!MPBSettings.showSupport) TabRegistry.disableTab(supportButton);
	}

	@SubscribeEvent
	public void action(ActionPerformedEvent.Post event)
	{
		if(event.getGui() instanceof GuiMainMenu || event.getGui() instanceof GuiIngameMenu)
		{
			if(event.getButton() == changelogButton)
			{
				event.getGui().mc.displayGuiScreen(new GuiChangeLogWindow(event.getGui()));
			}
			
			if(event.getButton() == bugReportButton)
			{
				 //BugReporterWindow.main(null);
				
				
				if(MPBSettings.useInGameForm)
				{
					BugReporterWindow window = new BugReporterWindow();
					window.frame.setVisible(true);
				}
				else
				{
					event.getGui().mc.displayGuiScreen(new GuiConfirmOpenLink(this, MPBSettings.issuetrackerURL, 0, true));
				}

				//event.getGui().mc.displayGuiScreen(new GuiConfirmOpenLink(this, MPBSettings.bugURL, 0, true));
			}
			
			if(event.getButton() == supportButton)
			{
				event.getGui().mc.displayGuiScreen(new GuiConfirmOpenLink(this, MPBSettings.supportURL, 1, true));
			}
		}
	}

	@Override
	public void confirmClicked(boolean result, int id) 
	{
		if(!result) 
		{
			mc.displayGuiScreen(null);
			return;
		}
			try
			{
				Class oclass = Class.forName("java.awt.Desktop");
				Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
				String url = "";
				if(id == 0) url = MPBSettings.issuetrackerURL;
				else if(id == 1) url = MPBSettings.supportURL;
				
				oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(url)});
			}	
			catch (Throwable throwable)
			{
				MPUtils.logger.error("Couldn\'t open link", throwable);
			}
		
	
	}
	
}
