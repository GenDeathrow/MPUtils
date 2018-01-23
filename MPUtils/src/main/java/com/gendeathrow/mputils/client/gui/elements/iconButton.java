package com.gendeathrow.mputils.client.gui.elements;

import com.gendeathrow.mputils.core.MPUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class iconButton extends GuiButton
{
	   protected static final ResourceLocation iconTexture = new ResourceLocation(MPUtils.MODID,"textures/gui/icons.png");
	    
	    private boolean forcedHighlight = false;
	    
		public iconButton(int buttonId, int x, int y)
	    {
	        this(buttonId, x, y, 16, 16, "");
	    }
		
		public iconButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) 
		{
			super(buttonId, x, y, widthIn, heightIn, buttonText);
		}
		
		
		
		int iconX;
		int iconY;
		
		public iconButton setIcon(int x, int y)
		{
			this.iconX = x * 16;
			this.iconY = y * 16;
			
			return this;
		}
		
		
	    public void drawButton(Minecraft mc, int mouseX, int mouseY)
	    {
	        if (this.visible)
	        {
	        	
	            FontRenderer fontrenderer = mc.fontRenderer;
	            mc.getTextureManager().bindTexture(iconTexture);
	            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	            int i = this.getHoverState(this.forcedHighlight ? true : this.hovered);
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

	        	this.drawTexturedModalRect(this.x, this.y, this.iconX, this.iconY + ((i * 16) - 16), 16, 16);
	        	
	            this.mouseDragged(mc, mouseX, mouseY);
	            int j = 14737632;
	        }
	    }

		public boolean isHovered() 
		{
			return this.hovered;
		}
		
		public void setHighlighted()
		{
			this.forcedHighlight = true;
		}
	
	

}
