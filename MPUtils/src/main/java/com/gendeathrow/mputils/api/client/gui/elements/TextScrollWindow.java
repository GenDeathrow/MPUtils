package com.gendeathrow.mputils.api.client.gui.elements;

import java.awt.Color;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Mouse;

import com.gendeathrow.mputils.client.gui.elements.TextEditor;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.utils.RenderAssist;
import com.google.common.collect.Lists;

public class TextScrollWindow extends GuiListExtended
{
	private final List<Line> Lines = Lists.newArrayList();
	
	private String rawData;
	private Minecraft mc;
	
	private TextEditor editor;
	
	public TextScrollWindow(Minecraft mcIn, String text, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) 
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		
		this.rawData = text;
		parseLines(mcIn);
		
		this.mc = mcIn;
		
	}

	public synchronized  void setRawData(String data)
	{
		this.rawData = data;
		parseLines(Minecraft.getMinecraft());
	}
	
	public synchronized  String getRawData()
	{
		return this.rawData;
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


	public synchronized void parseLines(Minecraft mc)
	{
		String[] parsedData = rawData.replace("\r", "").replace("\t", "    ").split("\n");
		
		List<String> lineBreaks = Lists.newArrayList();
		List<String> wordWrap = Lists.newArrayList();
		
		Lines.clear();
		
		for(String line : parsedData)
		{
			List<String> lines = mc.fontRendererObj.listFormattedStringToWidth(line, this.width);
			
			wordWrap.addAll(lines);
			
			for(int i = 1; i <= lines.size(); i++)
			{
				lineBreaks.add(i + "," + lines.size());
			}
		}
		
		addWordWrap(wordWrap, lineBreaks);
	}
	
	
	@SuppressWarnings("unused")
	private synchronized void addWordWrap(List<String> wordWrap)
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
	private synchronized  void addWordWrap(List<String> wordWrap, List<String> LineBreaks)
	{
		textType type;
		textType lasttype =  textType.DEFAULT;
		
		Iterator<String> wrapped = wordWrap.iterator();
		
		Iterator<String> breaks = LineBreaks.iterator();
		
		
		while(wrapped.hasNext())
		{
			Object line = wrapped.next();
			Object linenum = breaks.next();

			type = parseChangelog(line.toString());
			Lines.add((new TextScrollWindow.Line(line.toString(), type).setURL((type == textType.URL ? getURL(line.toString()) : ""))));
			}
	}
	
	
	private String getURL(String line)
	{
		Pattern url = Pattern.compile("((https?|ftp|file):\\/\\/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])");
		Matcher m = url.matcher(line);
		
		if(m.matches())
		{
			
			
			if (m.find()) 
			{
				return m.group(1);
			}
			return line;
		}
		return line;
	}
	
	
	private textType parseChangelog(String line)
	{
		line = line.toLowerCase();
		//Pattern versionNum = Pattern.compile("\\[.+\\]");
		//Pattern change = Pattern.compile(".*(fixed|\\*|fix|fixes|bug|changed).*");
		//Pattern add = Pattern.compile(".*(added|\\+|new|adding).*");
		//Pattern removed = Pattern.compile(".*(removed|deleted|revert).*");
		//Pattern header = Pattern.compile(".*full enviromine changelog.*");
		Pattern url = Pattern.compile("((https?|ftp|file):\\/\\/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])");
		
//		if(versionNum.matcher(line).matches())
//		{
//			//System.out.println(line + versionNum.matcher(line).matches());
//			return textType.VERSION;
//		} else if(change.matcher(line).matches())
//		{
//			//System.out.println(line + change.matcher(line).matches());
//			return textType.CHANGED;
//		} else if(header.matcher(line).matches())
//		{
//			//System.out.println(line + header.matcher(line).matches());
//			return textType.HEADER;
//		} else if(removed.matcher(line).matches())
//		{
//			//System.out.println(line + removed.matcher(line).matches());
//			return textType.REMOVED;
//		} else if(add.matcher(line).matches())
//		{
//			//System.out.println(line + add.matcher(line).matches());
//			return textType.ADD;
//		}
//		
		
		if(url.matcher(line).matches())
		{
			return textType.URL;
		}
		else

			return textType.DEFAULT;
	}
	
	
	@Override
	public synchronized IGuiListEntry getListEntry(int index) 
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
	
	@Override
	public void handleMouseInput()
	{

	        if (this.isMouseYWithinSlotBounds(this.mouseY))
	        {
	            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom)
	            {
	                int i = (this.width - this.getListWidth()) / 2;
	                int j = (this.width + this.getListWidth()) / 2;
	                int k = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
	                int l = k / this.slotHeight;

	                if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0)
	                {
	                    this.elementClicked(l, false, this.mouseX, this.mouseY);
	                    this.selectedElement = l;
	                }
	                else if (this.mouseX >= i && this.mouseX <= j && k < 0)
	                {
	                    this.clickedHeader(this.mouseX - i, this.mouseY - this.top + (int)this.amountScrolled - 4);
	                }
	            }

	            if (Mouse.isButtonDown(0) && this.getEnabled())
	            {
	                if (this.initialClickY == -1)
	                {
	                    boolean flag1 = true;

	                    if (this.mouseY >= this.top && this.mouseY <= this.bottom)
	                    {
	                        int j2 = (this.width - this.getListWidth()) / 2;
	                        int k2 = (this.width + this.getListWidth()) / 2;
	                        int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
	                        int i1 = l2 / this.slotHeight;

	                        if (i1 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0)
	                        {
	                            boolean flag = i1 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
	                            this.elementClicked(i1, flag, this.mouseX, this.mouseY);
	                            this.selectedElement = i1;
	                            this.lastClicked = Minecraft.getSystemTime();
	                        }
	                        else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0)
	                        {
	                            this.clickedHeader(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
	                            flag1 = false;
	                        }

	                        int i3 = this.getScrollBarX();
	                        int j1 = i3 + 6;

	                        if (this.mouseX >= i3 && this.mouseX <= j1)
	                        {
	                            this.scrollMultiplier = -1.0F;
	                            int k1 = this.getMaxScroll();

	                            if (k1 < 1)
	                            {
	                                k1 = 1;
	                            }

	                            int l1 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
	                            l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
	                            this.scrollMultiplier /= (float)(this.bottom - this.top - l1) / (float)k1;
	                        }
	                        else
	                        {
	                            this.scrollMultiplier = 1.0F;
	                        }

	                        if (flag1)
	                        {
	                            this.initialClickY = this.mouseY;
	                        }
	                        else
	                        {
	                            this.initialClickY = -2;
	                        }
	                    }
	                    else
	                    {
	                        this.initialClickY = -2;
	                    }
	                }
	                else if (this.initialClickY >= 0)
	                {
	                    this.amountScrolled -= (float)(this.mouseY - this.initialClickY) * this.scrollMultiplier;
	                    this.initialClickY = this.mouseY;
	                }
	            }
	            else
	            {
	                this.initialClickY = -1;
	            }

	            int i2 = Mouse.getEventDWheel();

	            if (i2 != 0)
	            {
	                if (i2 > 0)
	                {
	                    i2 = -1;
	                }
	                else if (i2 < 0)
	                {
	                    i2 = 1;
	                }

	                this.amountScrolled += (float)(i2 * this.slotHeight / 2);
	            }
	        }
	    }

	   
	@Override
	public void drawScreen(int mouseX, int mouseY, float p_148128_3_)
	{
		handleMouseInput();
		super.drawScreen(mouseX, mouseY, p_148128_3_);
	}
	
	
	@Override
    protected void overlayBackground(int p_148136_1_, int p_148136_2_, int p_148136_3_, int p_148136_4_)
    {
    
    }
	

	@Override
    protected void drawSlot(int entryID, int x, int y, int p_180791_4_, int mouseXIn, int mouseYIn)
    {
    	if(isYWithinSlotBounds(y))
    	{
    		this.getListEntry(entryID).drawEntry(entryID, x, y, this.getListWidth(), p_180791_4_, mouseXIn, mouseYIn, this.isMouseYWithinSlotBounds(mouseYIn) && this.getSlotIndexFromScreenCoords(mouseXIn, mouseYIn) == entryID);
    	}
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
	    
    public int getSlotIndexFromScreenCoords(int p_148124_1_, int p_148124_2_)
    {
        int i = this.left + this.width / 2 - this.getListWidth() / 2;
        int j = this.left + this.width / 2 + this.getListWidth() / 2;
        int k = p_148124_2_ - this.top - this.headerPadding + (int)this.getAmountScrolled() - 4;
        int l = k / this.slotHeight;
        return p_148124_1_ < this.getScrollBarX() && p_148124_1_ >= i && p_148124_1_ <= j && l >= 0 && k >= 0 && l < this.getSize() ? l : -1;
    }
    
    public boolean isYWithinSlotBounds(int y)
    {
        return y >= this.top && y <= this.bottom - 10;
    }
    
    public boolean isMouseYWithinSlotBounds(int p_148141_1_)
    {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }
    
	@SideOnly(Side.CLIENT)
	public static class Line implements GuiListExtended.IGuiListEntry , GuiYesNoCallback
	{
		Minecraft mc = Minecraft.getMinecraft();
		public static int LastYpos = 0;
		private String line;
		private textType type;
		private String url;
		
		public Line(String line, textType type)//
		{
			
			this.line = line;
			this.type = type;
			this.url = "";
		}
		
		public Line setURL(String url)
		{
			this.url = url;
			return this;
		}
		
		
		int color;

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) 
		{
			if(this.type == textType.URL)
			{
				boolean b= mouseX > x && mouseX < x+listWidth && mouseY > y && mouseY < y+slotHeight;
				
				mc.fontRendererObj.drawString(textTypeText(type, line), x, y, b ? RenderAssist.getColorFromRGBA(153,204,255,255) : Color.BLUE.getRGB());
			}
			else
			{
				mc.fontRendererObj.drawString(textTypeText(type, line), x, y, textTypeColor(type));
			}
		}
		
		@Override
		public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) 
		{
			if(this.type == textType.URL)
			{
				gotoHttp();
			}
			return false;
		}
		
	    private void gotoHttp()
	    {
	    	mc.displayGuiScreen(new GuiConfirmOpenLink(this, this.url, 0, true));
	    }
		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) 
		{
			
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
					oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(this.url)});
				}	
				catch (Throwable throwable)
				{
					MPUtils.logger.error("Couldn\'t open link", throwable);
				}
			
		}

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_,int p_178011_3_) 
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
		
		VERSION, HEADER, ADD, REMOVED, CHANGED, URL
		
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
			case URL: 
				return Color.BLUE.getRGB();
				
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


