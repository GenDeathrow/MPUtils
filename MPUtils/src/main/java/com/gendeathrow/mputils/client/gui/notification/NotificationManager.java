package com.gendeathrow.mputils.client.gui.notification;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class NotificationManager extends Gui
{
    private static final ResourceLocation tipsBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    
    private Minecraft mc;
    private int width;
    private int height;
    
    private long notificationTime;
    private NotificationBase theNotification;
    private boolean permanentNotification;
    private ArrayList<NotificationBase> queue = new ArrayList<NotificationBase>();
    
    public NotificationManager(Minecraft mc)
    {
        this.mc = mc;
    }

    public void displayNotification(NotificationBase notification)
    {
    	this.notificationTime = Minecraft.getSystemTime();
    	this.permanentNotification = false;
    	this.theNotification = notification;
    }
    
    
  public void addQueue(NotificationBase notification)
  {
  	if(notification.shouldNotifiy())
  	{
  		this.queue.add(notification);
  	}
  	else 
  	{
   		notification.lastSeen();
  	}
  }    
 
   
    public boolean hasNextQueue()
    {
    	return this.queue.size() > 0 ? (this.queue.get(0) != null ? true : false) : false;
    }
    
    public NotificationBase getNextQueue()
    {
    	
    	NotificationBase notification = this.queue.get(0);
    	
    	if(notification != null)
    	{
    		this.queue.remove(0);
    		return notification;
    	}
    	return null;
    }
    
    public boolean isInQueue(NotificationBase checkagainst)
    {
    	if(this.theNotification == checkagainst) return true;
    	else return this.queue.contains(checkagainst);
    }

    public void displayUnformattedTip(NotificationBase notificationIn)
    {
        this.notificationTime = Minecraft.getSystemTime() + 2500L;
        this.theNotification = notificationIn;
        this.permanentNotification = true;
    }
    
    

    private void updateTipWindowScale()
    {
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.width = this.mc.displayWidth;
        this.height = this.mc.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)this.width, (double)this.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }
    

    public void updateTipWindow()
    {
        if (this.theNotification != null && this.notificationTime != 0L && Minecraft.getMinecraft().player != null)
        {
        	
            double d0 = (double)(Minecraft.getSystemTime() - this.notificationTime) /  this.theNotification.getTimeLenght();

            if (!this.permanentNotification)
            {
                if (d0 < 0.0D || d0 > 1.0D)
                {
                    this.notificationTime = 0L;
                    return;
                }
            }
            else if (d0 > 0.5D)
            {
                d0 = 0.5D;
            }

            this.updateTipWindowScale();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            double d1 = d0 * 2.0D;

            if (d1 > 1.0D)
            {
                d1 = 2.0D - d1;
            }

            d1 = d1 * 4.0D;
            d1 = 1.0D - d1;

            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 = d1 * d1;
            d1 = d1 * d1;

            //	            int i = this.width - 160;
            int i = this.theNotification.getXPos();
            
            int j = 0 - (int)(d1 * 36.0D);
            
            
            this.theNotification.drawNotification(mc, i, j);
            

        }else if(hasNextQueue())
        {
            	this.displayNotification(getNextQueue()); 
        }
    }

    public void clearAchievements()
    {
//        this.theTip = null;
//        this.notificationTime = 0L;
    }
}
