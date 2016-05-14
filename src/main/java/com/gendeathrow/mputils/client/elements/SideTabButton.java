package com.gendeathrow.mputils.client.elements;

import com.gendeathrow.mputils.core.MPUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class SideTabButton extends GuiButton
{
    protected static final ResourceLocation buttonTexture = new ResourceLocation(MPUtils.MODID,"textures/gui/widgets.png");
    protected static final ResourceLocation iconTexture = new ResourceLocation(MPUtils.MODID,"textures/gui/icons.png");

    
    boolean isFullOpen;
    
    private int xOffset = 0;
    
    private int phase;
    
    public SideTabButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
        this.xOffset = this.width - 25;
    }
    
	public SideTabButton(int buttonId, int x, int y, int widthIn, int heightIn,	String buttonText) 
	{
		super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.xOffset = this.width - 25;
	}
	
	int iconX;
	int iconY;
	
	public SideTabButton setIcon(int x, int y)
	{
		this.iconX = x * 16;
		this.iconY = y * 16;
		
		return this;
	}
	
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
        	//if(!this.isFullOpen) xOffset = this.width - 20;
        	
        	if(this.hovered && !this.isFullOpen)
        	{
        		xOffset -= 5;
        		if(xOffset == 0) this.isFullOpen = true;
        	}
        	else if(!this.hovered)
        	{
        		xOffset += xOffset >= this.width - 25 ? 0 : 5;
        		this.isFullOpen = false;
        	}
        	
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTexture);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition + xOffset && mouseY >= this.yPosition && mouseX < this.xPosition + this.width + xOffset&& mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            
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

            this.drawString(fontrenderer, this.displayString, this.xPosition + 25 + xOffset, this.yPosition + (this.height - 8) / 2, j);
            
            mc.getTextureManager().bindTexture(iconTexture);
            
            //if(!this.isFullOpen) 
            	this.drawTexturedModalRect(this.xPosition + 4 + xOffset, this.yPosition + 1, iconX, iconY, 16, 16);
        }
    }

}
