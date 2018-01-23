package com.gendeathrow.mputils.client.gui.notification;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.utils.RenderAssist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class NotificationBase extends Gui
{
	public String title;
	public String description;
	
	int xPos;
	int yPos;
	
	int width;
	int height;
	
	int xOffset;
	
	private long lastSeen;
	private boolean shouldNotifiy;
	
	public boolean permanentNotification;
	
	//Minecraft mc;
	
	private static final ResourceLocation Bg = new ResourceLocation(MPUtils.MODID, "textures/gui/achievement_background.png");

	 
	public NotificationBase()
	{
		this(10, 0);
		this.width = 160;
		
	}
	
	public NotificationBase(int x, int y)
	{
		this(x,y,0);
		
		this.width = 160;
	}
	
	public NotificationBase(int x, int y, int xOffset)
	{
		this.xPos = x;
		this.yPos = y;
		this.xOffset = xOffset;
		
		this.width = 160;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setInfo(String title, String description)
	{
		this.title = title;
		this.description = description;
	}
	
	public void setDesc(String description)
	{
		this.description = description;
	}
	
	public void setTextOffset(int offset)
	{
		this.xOffset = offset;
	}
	
	public double getTimeLenght()
	{
		return 6000.0D;
	}
	
	/**
	 * get starting X position
	 * @return
	 */
	public int getXPos()
	{
		return this.xPos;
	}
	
	/**
	 * get starting Y position
	 * @return
	 */
	public int getYPos()
	{
		return this.yPos;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public boolean shouldNotifiy()
	{
		return true;
	}
	
    public void setSeen(long l)
    {
    	this.lastSeen = l;
    }
    
	public long lastSeen()
	{
		return this.lastSeen;
	}
	
	public ResourceLocation getBGTexture()
	{
		return Bg;
	}
	
	public void drawNotification(Minecraft mc, int x, int y)
	{
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        int textHeight = mc.fontRenderer.FONT_HEIGHT * mc.fontRenderer.listFormattedStringToWidth(this.description, this.getWidth()   - (this.xOffset + 16 )).size();
        
        if(getBGTexture() != null)
        {
        	RenderAssist.bindTexture(getBGTexture());
        }
        
        RenderAssist.drawTexturedModalRectCustomSize(x + 4, y + 3, 100, 205, 154, 26, this.getWidth() - 4, textHeight + 30 + y - 6);
        
        RenderAssist.drawScalableTextruedBoxBordered(x, y, 96, 202, this.getWidth(), textHeight + 30 + y);

        mc.fontRenderer.drawString(this.title, x + xOffset + 8, y + 7, -256);
       // mc.fontRenderer.drawSplitString(this.description, x + xOffset + 8, (int) (y + (textHeight * 0.21875)) + 18, this.width - 12 - this.xOffset, -1);
        mc.fontRenderer.drawSplitString(this.description, x + xOffset + 8, (int) y + 18, this.getWidth()  - (this.xOffset + 16 ), -1);
   
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);		
	}
}
