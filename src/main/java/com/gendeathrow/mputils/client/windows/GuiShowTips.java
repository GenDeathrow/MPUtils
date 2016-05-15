package com.gendeathrow.mputils.client.windows;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import com.gendeathrow.mputils.client.elements.TextScrollWindow;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.prompt.Tip;
import com.gendeathrow.mputils.prompt.TipList;
import com.gendeathrow.mputils.prompt.TipList.TipType;
import com.gendeathrow.mputils.utils.RenderAssist;

public class GuiShowTips extends GuiScreen
{
	TextScrollWindow scrollWindow;
	
	Tip tip;
	
	GuiScreen parent;
	
	private int sizeX;
	private int sizeY;

	private int posX;
	private int posY;
	
	private GuiCheckBox dontShow;
	
	private Random rand = new Random();
	
	public GuiShowTips(GuiScreen paramGuiBase) 
	{
		this.parent = paramGuiBase;
	}
	
	@Override
    public void initGui()
    {
		if(TipList.TIPS.size() == 0)
		{
			tip = new Tip("null", "No Tips", TipType.INFO);
		}
		else 
		{
			this.tipIndex = rand.nextInt(TipList.TIPS.size());
			tip = TipList.TIPS.get(tipIndex);
		}

		this.sizeX = this.width/2;
		this.sizeY = this.height/2;
		
		this.posX = (this.width/2) - (sizeX/2);
		this.posY = (this.height/2) - (sizeY/2);
		
		String textFile = "No Tips";

		scrollWindow = new TextScrollWindow(this.parent.mc, tip != null ? tip.getFullDescription() : textFile, sizeX, sizeY, posY, posY + sizeY, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+2);
  	
		scrollWindow.left = posX;
		scrollWindow.right = posX + sizeX;
		
		GuiButton newTip = new GuiButton(1, posX + (scrollWindow.width / 2) - 100, scrollWindow.height + posY + 6, "Random Tip");
		
		GuiButton close = new GuiButton(2, posX + (scrollWindow.width - 10), posY - 12, 10,10,  "X");
		
		GuiButton next = new GuiButton(3, (newTip.xPosition + newTip.width) + 2, newTip.yPosition,20, 20, ">");
		GuiButton prev = new GuiButton(4, newTip.xPosition - 22, newTip.yPosition , 20, 20, "<");
		
		GuiButton resetTips = new GuiButton(5, posX + (scrollWindow.width / 2) - 100, scrollWindow.height + posY + 6 + 22, "Reset Notifications Cache");
		
		dontShow = new GuiCheckBox(6, 10 , 10 , "Do not show tips notifications", !Settings.showTips);
		
		this.buttonList.add(newTip);
		this.buttonList.add(close);
		this.buttonList.add(next);
		this.buttonList.add(prev);
		this.buttonList.add(resetTips);
		this.buttonList.add(dontShow);
	}
	
	public void onGuiClosed()
	{
		if(!dontShow.isChecked() != Settings.showTips)
		{
			Settings.showTips = !dontShow.isChecked();
			MPUtils.tipHandler.SaveNBTFile();	
		}
		
	}
	
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.id == 1) 
		{
			if(TipList.TIPS.size() == 0) return;
			this.randomTip();
		}
		else if (button.id == 2)
		{
			this.mc.displayGuiScreen(this.parent);
		}
		else if (button.id == 3)
		{
			if(TipList.TIPS.size() == 0) return;
			this.nextTip();
		}
		else if (button.id == 4)
		{
			if(TipList.TIPS.size() == 0) return;
			this.prevTip();
		}
		else if(button.id == 5)
		{
			if(TipList.TIPS.size() == 0) return;
			
			MPUtils.tipHandler.setClearTips();
			MPUtils.tipHandler.SaveNBTFile();
			
			button.enabled = false;
			button.displayString = "Tips have been reset";
		}
	}
	   
	private void randomTip()
	{
		this.tipIndex = rand.nextInt(TipList.TIPS.size());
		this.setTip();
	}
	
	private void setTip()
	{
		tip = TipList.TIPS.get(tipIndex);
		scrollWindow.setRawData(tip.getFullDescription());
	}
	
	private int tipIndex;
	
	private void nextTip()
	{
		if(tipIndex + 1 > TipList.TIPS.size() - 1)
		{
			tipIndex = 0;
		}
		else tipIndex++;
		
		this.setTip();
	}
	
	private void prevTip()
	{
		if(tipIndex - 1 < 0)
		{
			tipIndex = TipList.TIPS.size()-1;
		}
		else tipIndex--;
		this.setTip();
	}
	
	static String readFile(String path, Charset encoding) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	 
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.parent.drawScreen(0, 0, partialTicks);
		 
		this.drawRect(0, 0, width, height, RenderAssist.getColorFromRGBA(0, 0, 0, 150));
		 
		this.drawRect(this.posX, this.posY - 15, this.posX + this.sizeX, this.posY + this.sizeY, Color.black.getRGB());
		 
		RenderAssist.drawUnfilledRect(this.posX , this.posY- 15, this.posX + this.sizeX+1, this.posY, Color.white.getRGB());
		 
		RenderAssist.drawUnfilledRect(this.posX, this.posY-1, this.posX + this.sizeX+1, this.posY + this.sizeY, Color.white.getRGB());

		this.scrollWindow.drawScreen(mouseX, mouseY, partialTicks);
		 
		this.drawCenteredString(fontRendererObj, tip.getStatName(), width/2, this.posY - 12, Color.yellow.getRGB());
		 
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	 
	@Override
	public void onResize(Minecraft mcIn, int w, int h)
	{
		this.parent.setWorldAndResolution(mcIn, w, h);
		updateSize(mcIn, w, h);
		 
		super.onResize(mcIn, w, h);
	}
	 
	private void updateSize(Minecraft mcIn, int w, int h)
	{
		this.sizeX = this.width/2;
		this.sizeY = this.height/2;
		
		this.posX = (this.width/2) - (sizeX/2);
		this.posY = (this.height/2) - (sizeY/2);
			
		this.scrollWindow.left = posX;
		scrollWindow.right = posX + sizeX;
			
		this.scrollWindow.setDimensions(this.sizeX, this.sizeY, this.posY, this.posY + this.sizeY);
			
	}
}
