package com.gendeathrow.mputils.client.gui.tablet;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.mputils.client.gui.elements.iconButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;


public class TabletMain extends TabletWindow
{
	
	public TabletMain() 
	{
		super();
	}
	
	
	@Override
	public void initGui()
	{
		super.initGui();
	}
    
	int index = 0;
	int row = 0;
	protected void AddApp(iconButton button)
	{
		button.x = 25 + (index * 20);
		button.y = 12 + (row * 40);
		this.buttonList.add(button);
		
		if(index++ > 8) 
		{
			index = 0;
			row++;
		}
		
	}

	

	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
    	this.drawBackground(p_73863_1_);
    	
    	super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
    
    

	
    public class button3d extends GuiButton
    {
    	EntityItem renderItem;
    	
    	public button3d(int buttonId, int x, int y, ItemStack stack)
        {
            this(buttonId, x, y, 25, 40, "");
            
            renderItem = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, stack);
            
            this.displayString = "Test String";
            
        }
    	
    	public button3d(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) 
    	{
    		super(buttonId, x, y, widthIn, heightIn, buttonText);
    		
    	}
    	
    	int iconX;
    	int iconY;
    	
    	public button3d setIcon(int x, int y)
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

            	boolean mouseOver = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            	
            	drawObject(mouseOver);
            	
            	
            	if(mouseOver) fontrenderer.drawString(this.displayString, this.x + (fontrenderer.getStringWidth(this.displayString + this.width)/2), this.y + 15, Color.BLACK.getRGB());
            	

            	
//                FontRenderer fontrenderer = mc.fontRenderer;
//                mc.getTextureManager().bindTexture(iconTexture);
//                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//                this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
//                int i = this.getHoverState(this.field_146123_n);
//                GL11.glEnable(GL11.GL_BLEND);
//                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//           
//            	this.drawTexturedModalRect(this.xPosition, this.yPosition, this.iconX, this.iconY + ((i * 16) - 16), 16, 16);
//            	
//                this.mouseDragged(mc, mouseX, mouseY);
//                int j = 14737632;

            }
        }
        
    	
    	
    	private void drawObject(boolean animate)
    	{
    		if (renderItem == null) 
    		{
    			return;
    		}

    		

    		GL11.glPushMatrix();
    		GL11.glColor3f(1F, 1F, 1F);
    		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
    		GL11.glEnable(2903 /* GL_COLOR_MATERIAL */);
    		GL11.glPushMatrix();
    		GL11.glTranslatef(this.x + 12, this.y + 12, 50F);
    		float f1 = 75F;
    		GL11.glScalef(-f1, f1, f1);
    		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
    		GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
    		RenderHelper.enableStandardItemLighting();
    		GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
    		GL11.glRotatef(0.0F, 1.0F, 0.0F, 0.0F);
    		renderItem.rotationPitch = 0.0F;
    		//GL11.glTranslatef(0.0F, renderItem.yOffset, 0.0F);
    		GL11.glTranslatef(0.0F, 0f, 0.0F);
    		
    		Minecraft.getMinecraft().getRenderManager().playerViewY = 180F;
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    		
    			//Minecraft.getMinecraft().getRenderManager()..func_147939_a(renderItem, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, true);
    			renderItem.setPosition(0, 0, 0);
    		//RenderManager.instance.renderEntitySimple(renderItem, 1.0f);
    			//RenderManager.instance.renderEntityStatic(renderItem, 0.0f, false); //(renderItem, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
    		GL11.glPopMatrix();
    		RenderHelper.disableStandardItemLighting();
    		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
    		GL11.glTranslatef(0F, 0F, 0.0F);
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
    		int i1 = 240;
    		int k1 = 240;
    		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,	i1 / 1.0F, k1 / 1.0F);
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
    		RenderHelper.disableStandardItemLighting();
    		GL11.glDisable(2896 /* GL_LIGHTING */);
    		GL11.glDisable(2929 /* GL_DEPTH_TEST */);
    		GL11.glPopMatrix();
    		
//    		if (animate) renderItem.getAge()++;
//    		else renderItem.age = 0;
    	}

    }
}
