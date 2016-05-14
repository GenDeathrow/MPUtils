package com.gendeathrow.mputils.client.elements;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.utils.RenderAssist;
import com.google.common.collect.Lists;

public class TextScrollWindow extends GuiListExtended
{
	private final List<Line> Lines = Lists.newArrayList();
	
	private String rawData;
	
	public TextScrollWindow(Minecraft mcIn, String text, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) 
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		
		this.rawData = text;
		parseLines(mcIn);
		
	}

	public void setRawData(String data)
	{
		this.rawData = data;
		parseLines(Minecraft.getMinecraft());
	}
	
    protected void drawListHeader(int x, int y, Tessellator p_148129_3_)
    {
    	Minecraft.getMinecraft().fontRendererObj.drawString("Whats New", x , this.top, Color.white.getRGB());
    }
    
 	 @Override
	 public int getListWidth()
	 {
		 return this.width;
	 }
	 
	 protected int getScrollBarX()
	 {
		 return this.width + this.left;
	 }

	
	
	public void parseLines(Minecraft mc)
	{
		String[] parsedData = rawData.replace("\r", "").split("\n");
		
		List<String> lineBreaks = Lists.newArrayList();
		List<String> wordWrap = Lists.newArrayList();
		
		Lines.clear();
		
		for(String line : parsedData)
		{
			@SuppressWarnings("unchecked")
			List<String> lines = mc.fontRendererObj.listFormattedStringToWidth(line, this.width);
			
			wordWrap.addAll(lines);
			
			for(int i = 1; i <= lines.size(); i++)
			{
				lineBreaks.add(i + "," + lines.size());
			}
		}
		
		addWordWrap(wordWrap, lineBreaks);
	}
	
	
	private void addWordWrap(List<String> wordWrap)
	{
		textType type;
		Iterator<String> wrapped = wordWrap.iterator();
		

		while(wrapped.hasNext())
		{
			Object line = wrapped.next();
			type = textType.DEFAULT;
			Lines.add(new TextScrollWindow.Line(line.toString(), type));
		}

	}
	
	
	/**
	 * Pass String and will wordwrap it to screen and add to list to be drawn
	 * @param wordWrap
	 */
	private void addWordWrap(List<String> wordWrap, List<String> LineBreaks)
	{
		textType type;
		textType lasttype =  textType.DEFAULT;
		
		Iterator<String> wrapped = wordWrap.iterator();
		
		Iterator<String> breaks = LineBreaks.iterator();
		
		
		while(wrapped.hasNext())
		{
			Object line = wrapped.next();
			Object linenum = breaks.next();

			type = textType.DEFAULT;
			
			Lines.add(new TextScrollWindow.Line(line.toString(), type));
		}
	}
	
	
	@Override
	public IGuiListEntry getListEntry(int index) 
	{
		return (TextScrollWindow.Line)this.Lines.get(index);
	}

	@Override
	protected int getSize() 
	{
		return this.Lines.size();
	}
	
	private final float scrollSpeed = 0.025F;
	
	public void scrollByMultiplied(float p_148145_1_)
	{
		super.scrollBy(MathHelper.ceiling_float_int(p_148145_1_ * scrollSpeed));
	}
	
    private void updateTipWindowScale()
    {
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        this.width = this.mc.displayWidth;
        this.height = this.mc.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, (double)this.width, (double)this.height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }
	
	@Override
	public void drawScreen(int p_148128_1_, int p_148128_2_, float p_148128_3_)
	{
		
		if(p_148128_1_ > this.left && p_148128_1_ < this.right && p_148128_2_ > this.top && p_148128_2_ < this.bottom && !(Mouse.isButtonDown(0) && this.field_148163_i))
		{
            try {
				for (; !this.mc.gameSettings.touchscreen && Mouse.next(); this.mc.currentScreen.handleMouseInput())
				{
				    float j1 = Mouse.getEventDWheel();

				    if (j1 != 0)
				    {
				    	j1 *= -1F;

				        this.scrollByMultiplied(j1 * (float)this.slotHeight / 2F);
				    }
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//this.updateTipWindowScale();
		super.drawScreen(p_148128_1_, p_148128_2_, p_148128_3_);
	}
	
	@Override
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha)
    {

    }
	
	ResourceLocation bg = new ResourceLocation(MPUtils.MODID, "textures/gui/bg.png");
	
	@Override
    protected void drawContainerBackground(Tessellator tessellator)
    {
        VertexBuffer buffer = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(bg);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos((double)this.left,  (double)this.bottom, 0.0D).tex((double)((float)this.left  / f), (double)((float)(this.bottom + (int)this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
        buffer.pos((double)this.right, (double)this.bottom, 0.0D).tex((double)((float)this.right / f), (double)((float)(this.bottom + (int)this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
        buffer.pos((double)this.right, (double)this.top,    0.0D).tex((double)((float)this.right / f), (double)((float)(this.top    + (int)this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
        buffer.pos((double)this.left,  (double)this.top,    0.0D).tex((double)((float)this.left  / f), (double)((float)(this.top    + (int)this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
        tessellator.draw();
    }
    
    
    protected void drawSlot(int entryID, int x, int y, int p_180791_4_, int mouseXIn, int mouseYIn)
    {
    	if(isYWithinSlotBounds(y))
    	{
    		this.getListEntry(entryID).drawEntry(entryID, x, y, this.getListWidth(), p_180791_4_, mouseXIn, mouseYIn, this.isMouseYWithinSlotBounds(mouseYIn) && this.getSlotIndexFromScreenCoords(mouseXIn, mouseYIn) == entryID);
    	}
    }
    

    public boolean isYWithinSlotBounds(int y)
    {
        return y >= this.top && y <= (this.bottom-10);
    }
    
	@SideOnly(Side.CLIENT)
	public static class Line implements GuiListExtended.IGuiListEntry
	{
		Minecraft mc = Minecraft.getMinecraft();
		public static int LastYpos = 0;
		private String line;
		private textType type;
		
		public Line(String line, textType type)//
		{
			
			this.line = line;
			this.type = type;
		}
		
		int color;

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) 
		{
			mc.fontRendererObj.drawString(textTypeText(type, line), x, y, textTypeColor(type));
		}
		
		

		@Override
		public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) 
		{
			return false;
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) 
		{
			
		}

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) 
		{
			
		}
		
	}
	
	/**
	 * These Enums are used for Parsing colors or chaning text when drawing to screen
	 * @author GenDeathrow
	 *
	 */
	enum textType
	{
		TITLE, DATE, CREATOR, HR, DEFAULT,
		
		VERSION, HEADER, ADD, REMOVED, CHANGED
		
	}
	
	public static int textTypeColor(textType type)
	{
		switch(type)
		{
			case TITLE:
				return RenderAssist.getColorFromRGBA(21, 153, 21, 255);
				
			case DATE:
				return RenderAssist.getColorFromRGBA(71, 134, 186, 255);
				
			case CREATOR:
				return RenderAssist.getColorFromRGBA(53, 219, 161, 255);
				
			case HR:
				return RenderAssist.getColorFromRGBA(71, 134, 186, 255);
				
			case VERSION:
				return RenderAssist.getColorFromRGBA(255, 251, 0, 255);
				
			case HEADER:
				return RenderAssist.getColorFromRGBA(110, 129, 255, 255);
				
			case ADD:
				return RenderAssist.getColorFromRGBA(0, 255, 0, 255);
				
			case REMOVED:
				return RenderAssist.getColorFromRGBA(255, 0, 0, 255);
				
			case CHANGED:
				return RenderAssist.getColorFromRGBA(255, 98, 0, 255);
				
			default:
				return 16777215;
				
		}
	}
	
	public static String textTypeText(textType type, String line)
	{
		switch(type)
		{
			case TITLE:
				line = line.toUpperCase();
				break;
			
			case HR:
				String hr = "---------------------------------------------------------------------------------------";
				line = Minecraft.getMinecraft().fontRendererObj.trimStringToWidth(hr, 300);
				break;
			
			default:
				break;
		
		}
		
		return line;
	}
}


