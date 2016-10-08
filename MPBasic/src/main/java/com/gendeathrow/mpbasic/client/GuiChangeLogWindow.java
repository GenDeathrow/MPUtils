package com.gendeathrow.mpbasic.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mpbasic.core.MPBSettings;
import com.gendeathrow.mputils.api.client.gui.ScrollWindowBase;
import com.gendeathrow.mputils.api.client.gui.elements.TextScrollWindow;
import com.gendeathrow.mputils.client.gui.elements.TextEditor;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.utils.Tools;

public class GuiChangeLogWindow extends ScrollWindowBase
{
	String textfile = "Found no File";
	private TextEditor editor;
	
	
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

			if(MPBSettings.isHttp)
			{
				textfile = Tools.URLReader(MPBSettings.url);
			}
			else textfile = readFile(ConfigHandler.configDir + "/"+ MPBSettings.url, Charset.defaultCharset());
			
		} catch (Exception e) 
		{
			MPUtils.logger.log(Level.ERROR, "Issue trying to load '"+ MPBSettings.url +"' File not found");
			e.printStackTrace();
		}
		
		scrollWindow = new TextScrollWindow(this.parent.mc, textfile, sizeX, sizeY, posY, posY + sizeY, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+2);
		this.scrollWindow.setSlotXBoundsFromLeft(posX);		 
		this.setTitle(MPBSettings.changeLogTitle);
		
		if(Settings.editMode)
		{
			this.buttonList.add(new GuiButton(12, (this.width - 200)/2 , posY + sizeY + 5 , "Use Editor"));
			this.buttonList.add(new GuiButton(13, (this.width - 200)/2 , posY + sizeY + 5  + 22, "Save File"));
			
			if(editor == null)
			{
				editor = new TextEditor(this);
			
				editor.area.setText(((TextScrollWindow)this.scrollWindow).getRawData());
			}
			else
			{
				editor.setTextArea((TextScrollWindow)scrollWindow);
				((TextScrollWindow)scrollWindow).setRawData(editor.area.getText());
			}
		}
		
    }
	
	private void EditorSaveFile()
	{
		Tools.CreateSaveFile(new File(ConfigHandler.configDir + "/"+ MPBSettings.url), ((TextScrollWindow)this.scrollWindow).getRawData());
	}
	
	protected void actionPerformed(GuiButton button)
	{
		if (button.id == 2)
		{
			this.mc.displayGuiScreen(this.parent);
		}else if(button.id == 12 && editor != null)
		{
			editor.setVisible(true);
		}else if(button.id == 13)
		{
			EditorSaveFile();
		}
	}
	
	
	@Override
	public void onGuiClosed() 
	{
		
		if(editor != null)	editor.dispose();
	}
	
	 static String readFile(String path, Charset encoding) throws IOException 
	 {
		 byte[] encoded = Files.readAllBytes(Paths.get(path));
		 return new String(encoded, encoding);
	 }
	 
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
	    super.mouseClicked(mouseX, mouseY, mouseButton);
	    	
	    this.scrollWindow.mouseClicked(mouseX, mouseY, mouseButton);
	}
	 
	 @Override
	 public void updateSize(int w, int h)
	 {
			//this.sizeX = (int) (w/1.5);
			if(this.sizeX > w) this.sizeX = w - 40;
			this.sizeY = (int) (h/1.5);
				
			this.posX = (int)(w/2) - (int)(sizeX/2);
			this.posY = (int)(h/2) - (int)(sizeY/2);
	 }
}
