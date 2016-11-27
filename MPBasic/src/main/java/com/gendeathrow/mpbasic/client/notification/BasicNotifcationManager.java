package com.gendeathrow.mpbasic.client.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.gendeathrow.mpbasic.client.MPSave.BTSaveHandler;
import com.gendeathrow.mpbasic.core.MPBSettings;
import com.gendeathrow.mpbasic.core.MPBasic;
import com.gendeathrow.mputils.client.gui.notification.NotificationManager;
import com.gendeathrow.mputils.client.settings.MPUtils_SaveHandler;
import com.gendeathrow.mputils.utils.MPInfo;

public class BasicNotifcationManager extends NotificationManager
{
	public static NotificationManager instance = new NotificationManager(Minecraft.getMinecraft());

	
	public BasicNotifcationManager(Minecraft mc) 
	{
		super(mc);
	}
	
	@SubscribeEvent
	public void drawNotification(RenderGameOverlayEvent.Post event)
	{
		if (!event.getType().equals(ElementType.CHAT)) return;
		instance.updateTipWindow();
	}
	
	
	private boolean hasChecked = false;
	@SubscribeEvent
	public void checkUpdateNotification(TickEvent.PlayerTickEvent event) 
	{
		if((!event.player.worldObj.isRemote || hasChecked) && MPInfo.isActive() || MPUtils_SaveHandler.firstSave || !MPBSettings.showUpdateNotification)
		{
			return;
		}
		hasChecked = true;  
		
		 int[] CurrentVersion = MPInfo.getFormatedMPVer();
		 int[] LastVersion = MPInfo.getFormatedMPVer(BTSaveHandler.savedVersion);

		if(compareVersions(LastVersion, CurrentVersion) == -1)
		{
			UpdateNotification notfiy = new UpdateNotification("Update Notification", "Looks like you have updated \n \""+TextFormatting.YELLOW + MPInfo.name + TextFormatting.WHITE+"\" to v"+ TextFormatting.YELLOW + MPInfo.version + TextFormatting.WHITE +". \n Make sure to check the Changelog in the Game Menu.");
			instance.addQueue(notfiy);
			
			MPUtils_SaveHandler.saveSettings();
		}
		
	}
	
	
	/**
	 * Will compare Versions numbers and give difference
	 * @param oldVer
	 * @param newVer
	 * @return
	 */

	
	public static int compareVersions(String oldVer, String newVer)
	{
		if(oldVer == null || newVer == null || oldVer.isEmpty() || newVer.isEmpty())
		{
			return -2;
		}
		
		int result = 0;
		int[] oldNum;
		int[] newNum;
		
		try
		{
			oldNum = MPInfo.getFormatedMPVer(oldVer);
			newNum = MPInfo.getFormatedMPVer();
			
		} catch(IndexOutOfBoundsException e)
		{
			MPBasic.logger.warn("An IndexOutOfBoundsException occured while checking version! Make sure all your Version Numbers are formated as (MajorVersion.MinorVersion.RevesionVersion = 1.2.0) And Contain no special characters or text.", e);
			return -2;
		} catch(NumberFormatException e)
		{
			MPBasic.logger.warn("A NumberFormatException occured while checking version!\n", e);
			return -2;
		}
		
		for(int i = 0; i < 3; i++)
		{
			if(oldNum[i] < newNum[i])
			{
				return -1;
			} else if(oldNum[i] > newNum[i])
			{
				return 1;
			}
		}
		return result;
	}
	
	
	
	public static int compareVersions(int[] oldVer, int[] newVer)
	{
		if(oldVer == null || newVer == null || oldVer.length == 0 || oldVer.length == 0)
		{
			return -2;
		}
		
		int result = 0;
		
		for(int i = 0; i < 3; i++)
		{
			if(oldVer[i] < newVer[i])
			{
				return -1;
			} else if(oldVer[i] > newVer[i])
			{
				return 1;
			}
		}
		
		return result;
	}
	
}
