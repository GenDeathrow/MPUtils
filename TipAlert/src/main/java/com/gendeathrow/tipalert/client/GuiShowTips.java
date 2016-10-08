package com.gendeathrow.tipalert.client;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderItem;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import com.gendeathrow.mputils.api.client.gui.ScrollWindowBase;
import com.gendeathrow.mputils.api.client.gui.elements.TextScrollWindow;
import com.gendeathrow.mputils.utils.RenderAssist;
import com.gendeathrow.tipalert.core.TipAlert;
import com.gendeathrow.tipalert.core.TipSettings;
import com.gendeathrow.tipalert.prompt.TipList;
import com.gendeathrow.tipalert.prompt.TipList.TipType;
import com.gendeathrow.tipalert.prompt.Tips.Tip;

public class GuiShowTips extends ScrollWindowBase
{
	Tip tip;
	
	private Random rand = new Random();

	private GuiCheckBox dontShow;

	
	public GuiShowTips(GuiScreen paramGuiBase) 
	{
		super(paramGuiBase);
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public void initGui()
    {
		super.initGui();
		
		if(TipList.TIPS.size() == 0)
		{
			tip = new Tip("null", "No Tips", TipType.INFO);
		}
		else if(TipAlert.tipManager.getLastTip() != null)
		{
			tip = TipAlert.tipManager.getLastTip();
		}
		else 
		{
			this.tipIndex = rand.nextInt(TipList.TIPS.size());
			tip = TipList.TIPS.get(tipIndex);
		}

		String textFile = "No Tips";

		this.scrollWindow = new TextScrollWindow(this.mc, tip != null ? tip.getFullDescription() : textFile, sizeX, sizeY, posY, posY + sizeY, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+2);
		this.scrollWindow.setSlotXBoundsFromLeft(posX);
		
		this.setTitle(tip.getTitle());
		
		GuiButton newTip = new GuiButton(1, posX + (scrollWindow.width / 2) - 100, scrollWindow.height + posY + 6, "Random Tip");
		
		GuiButton next = new GuiButton(3, (newTip.xPosition + newTip.width) + 2, newTip.yPosition,20, 20, ">");
		GuiButton prev = new GuiButton(4, newTip.xPosition - 22, newTip.yPosition , 20, 20, "<");
		
		GuiButton resetTips = new GuiButton(5, posX + (scrollWindow.width / 2) - 100, scrollWindow.height + posY + 6 + 22, "Reset Notifications Cache");
		
		dontShow = new GuiCheckBox(6, 10 , 20 , "Do not show tips notifications", !TipSettings.showTips);
		
		this.buttonList.add(newTip);
		this.buttonList.add(next);
		this.buttonList.add(prev);
		this.buttonList.add(resetTips);
		this.buttonList.add(dontShow);
	}
	
	public void onGuiClosed()
	{
		if(!dontShow.isChecked() != TipSettings.showTips)
		{
			TipSettings.showTips = !dontShow.isChecked();
			TipAlert.tipManager.SaveNBTFile();	
		}
		
	}
	
	protected void actionPerformed(GuiButton button)
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
			
			TipAlert.tipManager.setClearTips();
			TipAlert.tipManager.SaveNBTFile();
			
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
		this.setTitle(tip.getTitle());
		((TextScrollWindow) scrollWindow).setRawData(tip.getFullDescription());
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
		super.drawScreen(mouseX, mouseY, partialTicks);	
		
		if(this.tip.theItemStack == null) return;
		
		int x = this.posX - 34;
		int y = this.posY;
		int ex = this.posX;
		int ey = this.posY + 34;
		
		drawRect(x, y, ex, ey, Color.black.getRGB());
		 
		RenderAssist.drawUnfilledRect(x, y, ex, ey, Color.white.getRGB());

		RenderAssist.renderItem(this.tip.theItemStack, x + 1, y + 1, 2);
	}
	 
	@SuppressWarnings("unused")
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
