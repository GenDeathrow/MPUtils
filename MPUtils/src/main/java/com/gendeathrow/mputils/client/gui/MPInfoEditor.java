package com.gendeathrow.mputils.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.utils.MPInfo;

public class MPInfoEditor extends GuiScreen
{

	protected GuiScreen parent;
	
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiTextField name,
    					description,
    					verMax, verMin, verRev,
    					url,
    					authorList,
    					credits;
    
    
    
    
	public MPInfoEditor(GuiScreen parent)
	{
		this.parent=parent;
	}
	
	
    public void updateScreen()
    {
    	name.updateCursorCounter();
    	description.updateCursorCounter();
    	verMax.updateCursorCounter();
    	verMin.updateCursorCounter();
    	verRev.updateCursorCounter();
    	url.updateCursorCounter();
    	authorList.updateCursorCounter();
    	credits.updateCursorCounter();
    }
    
    @SuppressWarnings("unchecked")
	public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        
        this.buttonList.clear();
        this.buttonList.add(this.doneBtn = new GuiButton(100, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("Save", new Object[0])));
        this.buttonList.add(this.cancelBtn = new GuiButton(101, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));

        
    	int pos = 50;
    	int j = 0;
    	int spacing = 24;
    	
    	
        this.name = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, pos + j++ * spacing, 300, 20);
        this.name.setMaxStringLength(40);
        this.name.setFocused(true);
        this.name.setText(MPInfo.name);

        this.description = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 150, pos + j++ * spacing, 300, 20);
        this.description.setMaxStringLength(32500);
        this.description.setFocused(false);
        this.description.setText(MPInfo.description);
        int[] verions = null;
		try
		{
			String[] verionRaw = MPInfo.version.split("\\.");

			verions = new int[]{Integer.valueOf(verionRaw[0]),Integer.valueOf(verionRaw[1]),Integer.valueOf(verionRaw[2])};
		} catch(IndexOutOfBoundsException e)
		{
			MPUtils.logger.warn("An IndexOutOfBoundsException occured while checking version! Make sure all your Version Numbers are formated as (MajorVersion.MinorVersion.RevesionVersion = 1.2.0) And Contain no special characters or text.", e);
		} catch(NumberFormatException e)
		{
			MPUtils.logger.warn("A NumberFormatException occured while checking version!\n", e);
		}
		
        this.verMax = new GuiTextField(4, this.fontRendererObj, this.width / 2 - 150, pos + j * spacing, 30, 20);
        this.verMax.setMaxStringLength(4);
        this.verMax.setFocused(false);
        this.verMax.setText(""+(verions != null ? verions[0] : 0));
        
        this.verMin = new GuiTextField(5, this.fontRendererObj, (this.width / 2) - 150 + 50, pos + j * spacing, 30, 20);
        this.verMin.setMaxStringLength(4);
        this.verMin.setFocused(false);
        this.verMin.setText(""+(verions != null ? verions[1] : 0));
        
        
        this.verRev = new GuiTextField(6, this.fontRendererObj, this.width / 2 - 150 + 100, pos + j++ * spacing, 30, 20);
        this.verRev.setMaxStringLength(4);
        this.verRev.setFocused(false);
        this.verRev.setText(""+(verions != null ? verions[1] : 0));
        
        System.out.println("1:"+ this.verMax.xPosition +","+ this.verMax.yPosition);
        System.out.println("1:"+ this.verMin.xPosition +","+ this.verMin.yPosition);
        System.out.println("1:"+ this.verRev.xPosition +","+ this.verRev.yPosition);
        
        this.url = new GuiTextField(7, this.fontRendererObj, this.width / 2 - 150, pos + j++ * spacing, 300, 20);
        this.url.setMaxStringLength(32500);
        this.url.setFocused(false);
        this.url.setText(MPInfo.url); 
        
        String authors = "";
        boolean first = true;
        for(String author : MPInfo.authorList)
        {
        	if(first) { first = false;}
        	else { authors += ","; }
        	authors += author.replace("\"", "");
        }
        this.authorList = new GuiTextField(8, this.fontRendererObj, this.width / 2 - 150, pos + j++ * spacing, 300, 20);
        this.authorList.setMaxStringLength(32500);
        this.authorList.setFocused(false);
        this.authorList.setText(authors);
        
        this.credits = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 150, pos + j++ * spacing, 300, 20);
        this.credits.setMaxStringLength(32500);
        this.credits.setFocused(false);
        this.credits.setText(MPInfo.credits);

    }
    
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    
    
    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button == this.cancelBtn)
            {
                this.mc.displayGuiScreen(this.parent);
            }
            else if (button == this.doneBtn)
            {
            	//Save
            	
            	MPInfo.name = this.name.getText();
            	MPInfo.description = this.description.getText();
            	MPInfo.credits = this.credits.getText();
            	MPInfo.url = this.url.getText();
            	MPInfo.version = this.verMax.getText() +"."+ this.verMin.getText() +"."+ this.verRev.getText();

            	
            	String[] splitList = this.authorList.getText().split("\\,");
            		ArrayList<String> newList = new ArrayList<String>();
            			for(String author : splitList)
            			{
            				newList.add(author.trim());
            			}
        		MPInfo.authorList = newList;
            	
            	MPInfo.SaveMPInfo();
            	
            	this.mc.displayGuiScreen(this.parent);
            }
        }
    }
    
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.name.mouseClicked(mouseX, mouseY, mouseButton);
        this.description.mouseClicked(mouseX, mouseY, mouseButton);
        this.verMax.mouseClicked(mouseX, mouseY, mouseButton);
        this.verMin.mouseClicked(mouseX, mouseY, mouseButton);
        this.verRev.mouseClicked(mouseX, mouseY, mouseButton);
        this.url.mouseClicked(mouseX, mouseY, mouseButton);
        this.authorList.mouseClicked(mouseX, mouseY, mouseButton);
        this.credits.mouseClicked(mouseX, mouseY, mouseButton);
        
    }
    
    protected void keyTyped(char typedChar, int keyCode)
    {
    	
         this.name.textboxKeyTyped(typedChar, keyCode);
         this.description.textboxKeyTyped(typedChar, keyCode);

         
         this.authorList.textboxKeyTyped(typedChar, keyCode);
         this.url.textboxKeyTyped(typedChar, keyCode);
         this.credits.textboxKeyTyped(typedChar, keyCode);
         
         
         if((this.verMax.isFocused() || this.verMin.isFocused() || this.verRev.isFocused()))
         {
        	 if((typedChar >= '0' && typedChar <= '9') || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT)
        	 {
        		 this.verMax.textboxKeyTyped(typedChar, keyCode);
        		 this.verMin.textboxKeyTyped(typedChar, keyCode);
        		 this.verRev.textboxKeyTyped(typedChar, keyCode);
        	 }
         }
         
        if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 1)
            {
                this.actionPerformed(this.cancelBtn);
            }
        }
        else
        {
            this.actionPerformed(this.doneBtn);
        }
        
    }
    
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("mp.mpinfoedit.title", new Object[0]), this.width / 2, 25, 16777215);
        this.drawString(this.fontRendererObj, I18n.format("mp.text.mpinfo.name", new Object[0]) + ":", this.name.xPosition - 5 - (this.fontRendererObj.getStringWidth(I18n.format("mp.text.mpinfo.name", new Object[0]))), 			this.name.yPosition + 5, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("mp.text.mpinfo.desc", new Object[0])+ ":", this.description.xPosition - 5 - (this.fontRendererObj.getStringWidth(I18n.format("mp.text.mpinfo.desc", new Object[0]))), 	this.description.yPosition + 5, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("mp.text.mpinfo.ver", new Object[0])+ ":", this.verMax.xPosition - 5 - (this.fontRendererObj.getStringWidth(I18n.format("mp.text.mpinfo.ver", new Object[0]))), 			this.verMax.yPosition + 5, 10526880);
        	this.drawString(this.fontRendererObj, ".", this.verMin.xPosition - 2, this.verMax.yPosition + 19, 10526880);
        	this.drawString(this.fontRendererObj, ".", this.verRev.xPosition - 2, this.verMax.yPosition + 19, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("mp.text.mpinfo.url", new Object[0])+ ":", this.url.xPosition - 5 - (this.fontRendererObj.getStringWidth(I18n.format("mp.text.mpinfo.url", new Object[0]))), 				this.url.yPosition + 5, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("mp.text.mpinfo.authors", new Object[0])+ ":", this.authorList.xPosition - 5 - (this.fontRendererObj.getStringWidth(I18n.format("mp.text.mpinfo.authors", new Object[0]))),this.authorList.yPosition + 5, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("mp.text.mpinfo.credits", new Object[0])+ ":", this.credits.xPosition - 5 - (this.fontRendererObj.getStringWidth(I18n.format("mp.text.mpinfo.credits", new Object[0]))), 	this.credits.yPosition + 5, 10526880);
        
        this.name.drawTextBox();
        this.description.drawTextBox();
        this.verMax.drawTextBox();
        this.verMin.drawTextBox();
        this.verRev.drawTextBox();
        this.url.drawTextBox();
        this.authorList.drawTextBox();
        this.credits.drawTextBox();
        

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
