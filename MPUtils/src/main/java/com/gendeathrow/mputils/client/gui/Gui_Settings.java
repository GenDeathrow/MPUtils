package com.gendeathrow.mputils.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import com.gendeathrow.mputils.client.config.GuiMPConfig;

public class Gui_Settings extends GuiScreen
{
	protected GuiScreen parent;
	
	private GuiButton 	backButton,
						EditMPInfo,
						ConfigFile;
	   
	public Gui_Settings(GuiScreen parent)
	{
		this.parent=parent;
	}
	
	
	 @SuppressWarnings("unchecked")
	 public void initGui()
	 {
		 this.buttonList.clear();
	       
		 this.buttonList.add(this.backButton = new GuiButton(100, this.width / 2 - 75, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.back", new Object[0])));
	       
	    	int pos = 50;
	    	int j = 0;
	    	int spacing = 24;
	    	
		 this.buttonList.add(EditMPInfo = new GuiButton(1, this.width/2 - 100, pos +  j++ * 25, I18n.format("mp.buttontext.mpinfoedit", new Object[0])));
		 this.buttonList.add(ConfigFile = new GuiButton(1, this.width/2 - 100, pos + j++ * 25, I18n.format("mp.buttontext.configfile", new Object[0])));
		 
		 	
	 }
	
	 
	 protected void actionPerformed(GuiButton button)
	 {
		 if (button.enabled)
		 {
			 if (button == this.backButton)
			 {
				 this.mc.displayGuiScreen(this.parent);
			 }
			 else if (button == this.EditMPInfo)
			 {
				 this.mc.displayGuiScreen(new MPInfoEditor(this));
			 }
			 else if (button == this.ConfigFile)
			 {
				 this.mc.displayGuiScreen(new GuiMPConfig(this));
			 }
	            
		 }
	 }
	 
	 public void drawScreen(int mouseX, int mouseY, float partialTicks)
	 {
	        this.drawDefaultBackground();
	        this.drawCenteredString(this.fontRendererObj, I18n.format("mp.text.settingsgui.title", new Object[0]), this.width / 2, 25, 16777215);
	  
	        super.drawScreen(mouseX, mouseY, partialTicks);
	 }
	 
}
