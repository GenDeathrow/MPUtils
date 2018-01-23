package com.gendeathrow.mputils.client.gui.elements;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.mputils.client.gui.Gui_Edit_Command;
import com.gendeathrow.mputils.client.gui.Gui_QuickMenu;
import com.gendeathrow.mputils.client.settings.QuickCommandManager;
import com.gendeathrow.mputils.client.settings.QuickCommandManager.CommandElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.TextFormatting;

public class CommandButton extends GuiButton
{
	
	CommandElement command;
	int id;
	int keybind1;
	int keybind2;
	
	int listID;
	
	Gui_QuickMenu parent;
	
	public iconButton edit;
	public iconButton del;
	public iconButton add;
	List<iconButton> buttonList = new ArrayList<iconButton>();
	
	FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
	
	public CommandButton(int buttonId,int listID, int x, int y, int widthIn, int heightIn, CommandElement command, int key1, int key2, Gui_QuickMenu parent) 
	{
		super(buttonId, x, y, widthIn, heightIn, "");
		
		this.command = command;
		
		this.keybind1 = key1;
		this.keybind2 = key2;
		
		this.id = buttonId;
		
		this.listID = listID;
		
		this.parent = parent;
		
		this.buttonList.add(edit = new iconButton(0, 0, 0).setIcon(1, 1));
		this.buttonList.add(del = new iconButton(1, 0, 0).setIcon(2, 1));
		this.buttonList.add(add = new iconButton(1, 0, 0).setIcon(3, 1));
		
		
	}
	
	public boolean hasCommand()
	{
		return this.command != null;
	}
	
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	return this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y - 2 && mouseX < this.x + this.width && mouseY < this.y + this.height + 3;
    }
    
    public boolean MousePressedIcon(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = false;
    	for(int l=0; l < this.buttonList.size(); l++)
    	{
    		boolean b= this.buttonList.get(l).mousePressed(mc, mouseX, mouseY);
     		if(b)
    		{
    			actionPerformed(this.buttonList.get(l));
    		}
    		
    	}
    	
        return pressed;
    }
    
	public boolean keyPressed(char typedChar, int keyCode) throws IOException
	{
		if(!hasCommand()) return false;
		
		if (keyCode == this.keybind1 || keyCode == this.keybind2 ) 
		{
			runCommand();
			
			return true;
		}
		return false;
	}
	
	public boolean runCommand()
	{
		if(!hasCommand()) return false;
		
		this.parent.sendChatMessage(command.parseCommand());
		return true;
	}
	
    public void actionPerformed(GuiButton button)
    {
    	if(button.equals(this.edit)) 
    	{
    		Minecraft.getMinecraft().displayGuiScreen(new Gui_Edit_Command(this.parent.selectedList, this.command, this.id));
    	}
    	else if(button.equals(this.del)) 
    	{
    		QuickCommandManager.instance.removeCommand(QuickCommandManager.getList(this.parent.selectedList), this.id);
    		this.parent.mc.displayGuiScreen(this.parent);
    	}
    	else if(button.equals(this.add))
    	{
    		Minecraft.getMinecraft().displayGuiScreen(new Gui_Edit_Command(this.parent.selectedList,this.command, this.id));
    	}
    }
    
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
        	String title = "No Command";
        	if(hasCommand()) 
        	{
        		title = command.getTitle();
        		this.del.enabled = true;
        		this.del.visible = true;
        		this.edit.visible = true;
        		this.edit.enabled = true;
        		this.add.enabled = false;
        		this.add.visible = false;
        	}
        	else 
        	{
        		this.del.enabled = false;
        		this.del.visible = false;
        		this.add.enabled = true;
        		this.add.visible = true;
        		this.edit.visible = false;
        		this.edit.enabled = false;
        	}
        	FontRenderer fontrenderer = mc.fontRenderer;

            this.hovered = mouseX >= this.x && mouseY >= this.y - 2 && mouseX < this.x + this.width && mouseY < this.y + this.height + 3;
            int i = this.getHoverState(this.hovered);

        	
        	this.drawString(fontrenderer, TextFormatting.BOLD +""+ TextFormatting.GOLD + (id+1) + TextFormatting.RESET +". "+ title, this.x + 10, this.y, !this.hovered ? Color.WHITE.getRGB() : Color.YELLOW.getRGB());

        	int nextX = 0;
        	for(int l=0; l < this.buttonList.size(); l++)
        	{
        		this.buttonList.get(l).x = this.x + 95 + (16 * nextX);
        		this.buttonList.get(l).y = this.y + -5;
        		this.buttonList.get(l).drawButton(mc, mouseX, mouseY);
        		
        		if(this.buttonList.get(l).enabled)
        		{
        			nextX++;
        		}
        	}
        }
    }
    
    public void setPosition(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    }
    
    public boolean isHovered()
    {
    	return this.hovered;
    }
	
}
