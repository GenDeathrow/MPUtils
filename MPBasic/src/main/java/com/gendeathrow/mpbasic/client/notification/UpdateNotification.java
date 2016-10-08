package com.gendeathrow.mpbasic.client.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import com.gendeathrow.mputils.client.gui.notification.NotificationBase;

public class UpdateNotification extends NotificationBase
{
	public UpdateNotification(String title, String desc)
	{
		super(0,0);
		
		this.title = title;
		this.description = desc;
	}
	
	public int getXPos()
	{
		ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
		return (resolution.getScaledWidth() - this.getWidth())/2;
	}
	
	@Override
	public int getWidth()
	{
		return 250;
	}
	
	public double getTimeLenght()
	{
		return 12000.0D;
	}

}
