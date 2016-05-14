package com.gendeathrow.mputils.client.windows;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import com.gendeathrow.mputils.client.elements.TextScrollWindow;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.utils.RenderAssist;
import com.gendeathrow.mputils.utils.Tools;

public class GuiChangeLog extends GuiScreen
{
	TextScrollWindow scrollWindow;
	String textfile = "Found no File";
	GuiScreen parent;
	
	private int sizeX;
	private int sizeY;

	private int posX;
	private int posY;
	
	public GuiChangeLog(GuiScreen paramGuiBase) 
	{
		this.parent = paramGuiBase;
	}
	
	@Override
    public void initGui()
    {
		
		try 
		{

			if(Settings.isHttp)
			{
				textfile = Tools.URLReader(Settings.url);
			}
			else textfile = readFile(ConfigHandler.configDir + "/"+ Settings.url, Charset.defaultCharset());
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		this.sizeX = (int) (this.width/1.5);
		this.sizeY = (int) (this.height/1.5);
		
		this.posX = (this.width/2) - (sizeX/2);
		this.posY = (this.height/2) - (sizeY/2);
		
		scrollWindow = new TextScrollWindow(this.parent.mc, textfile, sizeX, sizeY, posY, posY + sizeY, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+2);
  	
		scrollWindow.left = posX;
		scrollWindow.right = posX + sizeX;

		GuiButton close = new GuiButton(2, posX + (scrollWindow.width - 5), posY - 14, 10,10,  "X");
		this.buttonList.add(close);
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
		 
		 this.drawRect(this.posX, this.posY - 15, this.posX + this.sizeX + 7, this.posY + this.sizeY, Color.black.getRGB());
		 
		 RenderAssist.drawUnfilledRect(this.posX , this.posY- 15, this.posX + this.sizeX + 7, this.posY, Color.white.getRGB());
		 
		 RenderAssist.drawUnfilledRect(this.posX, this.posY-1, this.posX + this.sizeX + 7, this.posY + this.sizeY, Color.white.getRGB());

		 this.scrollWindow.drawScreen(mouseX, mouseY, partialTicks);
		 
		 this.drawCenteredString(fontRendererObj, Settings.changeLogTitle, width/2, this.posY - 12, Color.yellow.getRGB());
		 
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
