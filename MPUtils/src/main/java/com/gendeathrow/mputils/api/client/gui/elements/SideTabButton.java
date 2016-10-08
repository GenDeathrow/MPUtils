package com.gendeathrow.mputils.api.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.mputils.core.MPUtils;

public class SideTabButton extends GuiButton
{
    protected static final ResourceLocation buttonTexture = new ResourceLocation(MPUtils.MODID,"textures/gui/widgets.png");
    protected static final ResourceLocation iconTexture = new ResourceLocation(MPUtils.MODID,"textures/gui/icons.png");
   
    boolean isFullOpen;
    
    private int xOffset = 0;
  
    private int xOpened;
    
    public SideTabButton(int buttonId, String buttonText)
    {
    	this(buttonId, 0, 0, buttonText);
    }
    
    public SideTabButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 100, 20, buttonText);
        this.xOffset = this.width - 26;
        this.xOpened = xOffset;
    }
    
	public SideTabButton(int buttonId, int x, int y, int widthIn, int heightIn,	String buttonText) 
	{
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		int textInt = Minecraft.getMinecraft().fontRendererObj.getStringWidth(buttonText);

	    this.xOffset = this.width - 26;
	    this.xOpened = xOffset;
	}
	
	int iconX;
	int iconY;
	
	public SideTabButton setIcon(int x, int y)
	{
		this.iconX = x * 16;
		this.iconY = y * 16;
		
		return this;
	}
	
	//Get the system time
	long lastTime = Minecraft.getSystemTime();
	//Specify how many seconds there are in a minute as a double
	//store as a double cause 60 sec in nanosec is big and store as final so it can't be changed
	final double ticks = 45;
	//Set definition of how many ticks per 1000000000 ns or 1 sec
	double ns = 1000 / ticks;    
	double delta = 0;
	
	public void update()
	{
	    long now = Minecraft.getSystemTime();
	    
	    //calculate change in time since last known time
	    delta += (now - lastTime) / ns;
	    
	    //update last known time    
	    lastTime = now;
	    
	    //continue while delta is less than or equal to 1
	      if(delta >= 1)
	      {
	        	if(this.hovered && !this.isFullOpen)
	        	{
	        		xOffset -= 10;
	        		if(xOffset <= 0) 
	        		{
	        			if(xOffset < 0 ) xOffset = 0;
	        			this.isFullOpen = true;
	        		}

	        	}
	        	else if(!this.hovered)
	        	{

	        		xOffset += xOffset >= xOpened ? 0 : 10;
	        		
	        		if(xOffset > xOpened ) xOffset = xOpened;
	        		this.isFullOpen = false;
	        	}
	    	  
	          delta--;
	      }
	      
	}
	
	
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
        	this.update();
        	
            FontRenderer fontRendererObj = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition + xOffset && mouseY >= this.yPosition && mouseX < this.xPosition + this.width + xOffset&& mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            this.drawTexturedModalRect(this.xPosition + xOffset, this.yPosition, 0, 6 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2 + xOffset, this.yPosition, 200 - this.width / 2, 6 + i * 20, this.width / 2, this.height);
            
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawString(fontRendererObj, this.displayString, this.xPosition + 25 + xOffset, this.yPosition + (this.height - 8) / 2, j);
            
            mc.getTextureManager().bindTexture(iconTexture);
            
            this.drawTexturedModalRect(this.xPosition + 7 + xOffset, this.yPosition + 1, iconX, iconY, 16, 16);
        }
    }

}
