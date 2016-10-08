package com.gendeathrow.mputils.api.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.utils.MPInfo;
import com.gendeathrow.mputils.utils.RenderAssist;

public class ScrollWindowBase extends GuiScreen
{
	protected GuiScreen parent;

	protected int sizeX;
	protected int sizeY;

	protected int posX;
	protected int posY;
	
	public GuiListExtended scrollWindow;
	
	String title= "needs title";
	
	public ScrollWindowBase(GuiScreen paramGuiBase) 
	{
		this.parent = paramGuiBase;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public void initGui()
    {
		this.sizeX = 400;
		
		updateSize(this.width, this.height);
		
		GuiButton close = new GuiButton(2, posX + (sizeX - 10), posY - 12, 10,10,  "X");
		
		this.buttonList.add(close);

    }
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void drawHeader()
	{
		drawRect(this.posX, this.posY - 15, this.posX + this.sizeX + 7, this.posY, RenderAssist.getColorFromRGBA(82, 81, 80, 255));

		RenderAssist.drawUnfilledRect(this.posX , this.posY- 15, this.posX + this.sizeX + 7, this.posY, Color.white.getRGB());

		this.drawCenteredString(fontRendererObj, title, width/2, this.posY - 12, Color.yellow.getRGB());
	}
	
	@SuppressWarnings("unused")
	private String versionText = "MPUtils version: "+ MPUtils.MCVERSION  + (MPUtils.MCVERSION != MPUtils.VERSION ? "("+ MPUtils.VERSION +")" : "");
	private String mpVersion = "Pack: "+ MPInfo.name + " v"+ MPInfo.version;


	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		 this.parent.drawScreen(0, 0, partialTicks);
		 
		 drawRect(0, 0, width, height, RenderAssist.getColorFromRGBA(0, 0, 0, 150));
		 
		 drawRect(this.posX, this.posY, this.posX + this.sizeX + 7, this.posY + this.sizeY, Color.black.getRGB());
		 
		 RenderAssist.drawUnfilledRect(this.posX, this.posY-1, this.posX + this.sizeX + 7, this.posY + this.sizeY, Color.white.getRGB());

		 this.scrollWindow.drawScreen(mouseX, mouseY, partialTicks);
		 
		 drawHeader();
		 
		 this.drawString(fontRendererObj, versionText, this.width - fontRendererObj.getStringWidth(versionText) - 5, 5, Color.yellow.getRGB());
		 if(MPInfo.isActive()) this.drawString(fontRendererObj, mpVersion, 2, 5, Color.yellow.getRGB());
		 if(Settings.editMode) 
		 {
			 this.drawCenteredString(this.fontRendererObj, "------->EDIT MODE<-------", this.width/2, 5, Color.yellow.getRGB());
			 this.drawCenteredString(this.fontRendererObj, "Check MPUtils.cfg for Defaults", this.width/2, 5 + 2 + this.fontRendererObj.FONT_HEIGHT, Color.yellow.getRGB());
		 }
		 
		 super.drawScreen(mouseX, mouseY, partialTicks);
	}
	 
	
	
	 public void setWorldAndResolution(Minecraft mcIn, int w, int h)
	 {
		 this.parent.setWorldAndResolution(mcIn, w, h);
		// updateSize(w, h);
		 //updatePositions();
		 super.setWorldAndResolution(mcIn, w, h);
	 }
	 
	 public void updateSize(int w, int h)
	 {
			//this.sizeX = w/2;
			if(this.sizeX > w) this.sizeX = w - 40;
			this.sizeY = h/2;
			
			this.posX = (w/2) - (sizeX/2);
			this.posY = (h/2) - (sizeY/2);
	 }
	 
	 public void updatePositions()
	 {
			this.scrollWindow.setDimensions(this.sizeX, this.sizeY, this.posY, this.posY + this.sizeY);
			scrollWindow.setSlotXBoundsFromLeft(posX);
	 }
}
