package com.gendeathrow.mputils.client.windows;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import com.gendeathrow.mputils.api.client.gui.ScrollWindowBase;
import com.gendeathrow.mputils.api.client.gui.elements.TextScrollWindow;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.prompt.Tip;
import com.gendeathrow.mputils.prompt.TipList;
import com.gendeathrow.mputils.prompt.TipList.TipType;
import com.gendeathrow.mputils.utils.RenderAssist;

public class GuiTipWindow extends ScrollWindowBase 
{

	Tip tip;
	
	private int tipIndex;
	
	private GuiCheckBox dontShow;
	
	private Random rand = new Random();
	
	public GuiTipWindow(GuiScreen paramGuiBase) 
	{
		super(paramGuiBase);
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
		
		if(TipList.TIPS.size() == 0)
		{
			tip = new Tip("null", "No Tips", TipType.INFO);
		}
		else if(MPUtils.tipHandler.tipNotification.getLastTip() != null)
		{
			tip = MPUtils.tipHandler.tipNotification.getLastTip();
		}
		else 
		{
			this.tipIndex = rand.nextInt(TipList.TIPS.size());
			tip = TipList.TIPS.get(tipIndex);
		}
		
		this.scrollWindow = new TextScrollWindow(this.parent.mc, tip != null ? tip.getFullDescription() : "No Tips", sizeX, sizeY, posY, posY + sizeY, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+2);
		this.scrollWindow.setSlotXBoundsFromLeft(posX);
		
		this.setTitle(tip.getStatName());
		
		GuiButton newTip = new GuiButton(1, posX + (scrollWindow.width / 2) - 100, scrollWindow.height + posY + 6, "Random Tip");
		
		GuiButton next = new GuiButton(3, (newTip.xPosition + newTip.width) + 2, newTip.yPosition,20, 20, ">");
		GuiButton prev = new GuiButton(4, newTip.xPosition - 22, newTip.yPosition , 20, 20, "<");
		
		GuiButton resetTips = new GuiButton(5, posX + (scrollWindow.width / 2) - 100, scrollWindow.height + posY + 6 + 22, "Reset Notifications Cache");
		
		dontShow = new GuiCheckBox(6, 10 , 10 , "Do not show tips notifications", !Settings.showTips);
		
		this.buttonList.add(newTip);
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
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(this.tip.theItemStack == null) return;
		
		int x = this.posX - 34;
		int y = this.posY;
		int ex = this.posX;
		int ey = this.posY + 34;
		
		this.drawRect(x, y, ex, ey, Color.black.getRGB());
		 
		RenderAssist.drawUnfilledRect(x, y, ex, ey, Color.white.getRGB());

		RenderAssist.renderItem(this.tip.theItemStack, x + 1, y + 1, 2);
	
	}
	
	
	private void randomTip()
	{
		this.tipIndex = rand.nextInt(TipList.TIPS.size());
		this.setTip();
	}
	
	private void setTip()
	{
		tip = TipList.TIPS.get(tipIndex);
		((TextScrollWindow) scrollWindow).setRawData(tip.getFullDescription());
		this.setTitle(tip.getStatName());
	}
	

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
	
	@Override
	public void updateSize(int w, int h)
	{
			this.sizeX = (int) (w/2);
			this.sizeY = (int) (h/2);
			
			this.posX = (w/2) - (int)(sizeX/2);
			this.posY = (h/2) - (int)(sizeY/2);
	}

}
