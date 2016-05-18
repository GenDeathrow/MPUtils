package com.gendeathrow.mputils.client.windows;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.api.client.gui.ScrollWindowBase;
import com.gendeathrow.mputils.api.client.gui.elements.TextScrollWindow;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.utils.Tools;

public class GuiChangeLogWindow extends ScrollWindowBase
{
	String textfile = "Found no File";
	
	public GuiChangeLogWindow(GuiScreen paramGuiBase) 
	{
		super(paramGuiBase);
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
		
		try 
		{

			if(Settings.isHttp)
			{
				textfile = Tools.URLReader(Settings.url);
			}
			else textfile = readFile(ConfigHandler.configDir + "/"+ Settings.url, Charset.defaultCharset());
			
		} catch (Exception e) 
		{
			MPUtils.logger.log(Level.ERROR, "Issue trying to load '"+ Settings.url +"' File not found");
			e.printStackTrace();
		}

		this.scrollWindow = new TextScrollWindow(this.parent.mc, textfile, sizeX, sizeY, posY, posY + sizeY, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+2);
		this.scrollWindow.setSlotXBoundsFromLeft(posX);
		
		this.setTitle(Settings.changeLogTitle);

    }
	
	static String readFile(String path, Charset encoding) throws IOException 
	{
		 byte[] encoded = Files.readAllBytes(Paths.get(path));
		 return new String(encoded, encoding);
	}
	 
	 
	protected void actionPerformed(GuiButton button) throws IOException
	{
		 if (button.id == 2)
		 {
				this.mc.displayGuiScreen(this.parent);
		 }
	}
	 
	@Override
	public void updateSize(int w, int h)
	{
			this.sizeX = (int) (w/1.5);
			this.sizeY = (int) (h/1.5);
			
			this.posX = (int)(w/2) - (int)(sizeX/2);
			this.posY = (int)(h/2) - (int)(sizeY/2);
	}

}
