package com.gendeathrow.mputils.client.windows;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import com.gendeathrow.mputils.client.elements.TextScrollWindow;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.prompt.Tip;
import com.gendeathrow.mputils.prompt.TipList;
import com.gendeathrow.mputils.prompt.TipList.TipType;
import com.gendeathrow.mputils.utils.RenderAssist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiSupport extends GuiScreen
{
	TextScrollWindow scrollWindow;
	
	GuiScreen parent;
	
	private int sizeX;
	private int sizeY;

	private int posX;
	private int posY;
	
	private Random rand = new Random();
	
	public GuiSupport(GuiScreen paramGuiBase) 
	{
		this.parent = paramGuiBase;
	}
	
	@Override
    public void initGui()
    {
		String textFile = "No Tips";

		scrollWindow = new TextScrollWindow(this.parent.mc,"", sizeX, sizeY, posY, posY + sizeY, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+2);
  	
		scrollWindow.left = posX;
		scrollWindow.right = posX + sizeX;
		scrollWindow.setShowSelectionBox(true);
		
		GuiButton newTip = new GuiButton(1, posX + (scrollWindow.width / 2) - 100, scrollWindow.height + posY + 6, "Random Tip");

		this.buttonList.add(newTip);
	}
	
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.id == 2)
		{
			this.mc.displayGuiScreen(this.parent);
		}
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
		 
		this.drawCenteredString(fontRendererObj, "", width/2, this.posY - 12, Color.yellow.getRGB());
		 
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
