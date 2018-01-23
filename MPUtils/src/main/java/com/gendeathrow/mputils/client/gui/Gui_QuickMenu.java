package com.gendeathrow.mputils.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.gendeathrow.mputils.client.gui.elements.CommandButton;
import com.gendeathrow.mputils.client.gui.elements.iconButton;
import com.gendeathrow.mputils.client.keybinds.KeyBinds;
import com.gendeathrow.mputils.client.settings.QuickCommandManager;
import com.gendeathrow.mputils.client.settings.QuickCommandManager.CommandElement;
import com.gendeathrow.mputils.core.MPUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class Gui_QuickMenu extends GuiScreen 
{
    protected static final ResourceLocation backgroundTexture = new ResourceLocation(MPUtils.MODID, "textures/gui/qmbackground.png");
    protected static final ResourceLocation iconTexture = new ResourceLocation(MPUtils.MODID,"textures/gui/icons.png");
    
    public int selectedList = 0;

    boolean isFullOpen;
    
    private int xOffset = 0;
    
	private int xOpened;

	public static boolean pauseGame = false;
	public static GuiCheckBox isPaused = new GuiCheckBox(200, 0, 0, I18n.format("mp.quickCommands.pause", new Object[0]), false);
	public iconButton global = new iconButton(56, 0, 0).setIcon(0, 3);
	public iconButton local = new iconButton(57, 0, 0).setIcon(1, 3);
			
			
	CommandButton command1;
	CommandButton command2;
	CommandButton command3;
	CommandButton command4;
	CommandButton command5;
	CommandButton command6;
	CommandButton command7;
	CommandButton command8;
	CommandButton command9;
	CommandButton command10;
	
	private GuiButton selectedCommand;
	
	List<CommandButton> commandButtons = new ArrayList<CommandButton>();
	
	
	public Gui_QuickMenu()
	{
		super();
		this.xOpened = 130;
        this.xOffset = xOpened;
	}
	
	public Gui_QuickMenu(int selected)
	{
        this();
        this.selectedList = selected;
        this.xOffset = 0;
	}
	
    public boolean doesGuiPauseGame()
    {
        return isPaused.isChecked();
    }
	
	@Override
	public void initGui()
	{
//		for(int j = 0; j<=9; j++)
//		{
//			this.buttonList.add(new iconButton(j, 0, 0).setIcon(1, 1));
//		}
		
		this.commandButtons.clear();
		
		this.commandButtons.add(command1 = new CommandButton(0, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(0), Keyboard.KEY_1, Keyboard.KEY_NUMPAD1, this));
		this.commandButtons.add(command2 = new CommandButton(1, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(1), Keyboard.KEY_2, Keyboard.KEY_NUMPAD2, this));
		this.commandButtons.add(command3 = new CommandButton(2, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(2), Keyboard.KEY_3, Keyboard.KEY_NUMPAD3, this));
		this.commandButtons.add(command4 = new CommandButton(3, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(3), Keyboard.KEY_4, Keyboard.KEY_NUMPAD4, this));
		this.commandButtons.add(command5 = new CommandButton(4, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(4), Keyboard.KEY_5, Keyboard.KEY_NUMPAD5, this));
		this.commandButtons.add(command6 = new CommandButton(5, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(5), Keyboard.KEY_6, Keyboard.KEY_NUMPAD6, this));
		this.commandButtons.add(command7 = new CommandButton(6, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(6), Keyboard.KEY_7, Keyboard.KEY_NUMPAD7, this));
		this.commandButtons.add(command8 = new CommandButton(7, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(7), Keyboard.KEY_8, Keyboard.KEY_NUMPAD8, this));
		this.commandButtons.add(command9 = new CommandButton(8, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(8), Keyboard.KEY_9, Keyboard.KEY_NUMPAD9, this));
		this.commandButtons.add(command10 =new CommandButton(9, this.selectedList, 0, 0, 90, 5, this.getCommandList().get(9), Keyboard.KEY_0, Keyboard.KEY_NUMPAD0, this));
		
		this.isPaused.setIsChecked(this.pauseGame);
		
		if(selectedList == 1) 
			global.setHighlighted();
		if(selectedList == 0)
			local.setHighlighted();
		
		this.buttonList.add(global);
		this.buttonList.add(local);
		
		this.buttonList.add(this.isPaused);
	}
	
	

	//Get the system time
	long lastTime = Minecraft.getSystemTime();
	//Specify how many seconds there are in a minute as a double
	//store as a double cause 60 sec in nanosec is big and store as final so it can't be changed
	final double ticks = 45;
	//Set definition of how many ticks per 1000000000 ns or 1 sec
	double ns = 1000 / ticks;    
	double delta = 0;
	
	public void update()
	{
	    long now = Minecraft.getSystemTime();
	    
	    //calculate change in time since last known time
	    delta += (now - lastTime) / ns;
	    
	    //update last known time    
	    lastTime = now;
	    
	    //continue while delta is less than or equal to 1
	      
	      if(delta >= 1)
	      {
	        	if(!this.isFullOpen)
	        	{
	        		xOffset -= 20;
	        		if(xOffset <= 0) 
	        		{
	        			if(xOffset < 0 ) xOffset = 0;
	        			this.isFullOpen = true;
	        		}

	        	}
	    	  
	          delta--;
	      }
	}
	
	@Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            for (int i = 0; i < this.commandButtons.size(); ++i)
            {
                CommandButton guibutton = (CommandButton)this.commandButtons.get(i);

                if (guibutton.mousePressed(this.mc, mouseX, mouseY))
                {
                    this.selectedCommand = guibutton;
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
                }
                guibutton.MousePressedIcon(mc, mouseX, mouseY);

            }
        }
        
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    }

	int lineY;
	int line;
	
	
	private int getNextLine()
	{
		this.line += fontRenderer.FONT_HEIGHT + 8;
		return this.line;
	}
	
	private ArrayList<CommandElement> getCommandList()
	{
		return QuickCommandManager.getList(this.selectedList);
	}
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		
		if(keyCode == KeyBinds.menu.getKeyCode())
		{
			if(this.selectedList == 0) 
				Minecraft.getMinecraft().displayGuiScreen(new Gui_QuickMenu(1));
			else 
				Minecraft.getMinecraft().displayGuiScreen(new Gui_QuickMenu(0));
			
			return;
		}
		
		for(CommandButton button : this.commandButtons)
		{
				if(button.keyPressed(typedChar, keyCode)) break;
		}
		
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
	
    protected void actionPerformed(GuiButton button)
    {
    	if(button instanceof CommandButton) ((CommandButton)button).runCommand();
    	else if(button == this.global) Minecraft.getMinecraft().displayGuiScreen(new Gui_QuickMenu(1));
    	else if (button == this.local) Minecraft.getMinecraft().displayGuiScreen(new Gui_QuickMenu(0));
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		update();
		int tabHeight = 163;
		
		int xPos = (this.width - xOpened) + xOffset;
		int yPos = (this.height/2) - (tabHeight/2) - 40;
		
		mc.getTextureManager().bindTexture(backgroundTexture);

		this.line = 10;

        this.drawTexturedModalRect(xPos, yPos, 0, 0, 205, 221);
        String title = "mp.quickCommands.title";
        
        if(this.selectedList == 0)
        	title = "mp.quickCommands.title.local";
        if(this.selectedList == 1)
        	title = "mp.quickCommands.title.global";
        
        this.drawString(fontRenderer, TextFormatting.BOLD +""+ TextFormatting.UNDERLINE + I18n.format(title, new Object[0]), xPos + 14, yPos+ 5, Color.WHITE.getRGB());
        
        this.isPaused.x = xPos + 10;
        this.isPaused.y = yPos + 33;
        
        this.global.x = (this.width - 55) + xOffset;
        this.global.y = yPos + 16;

        this.local.x = (this.width - 75) + xOffset;
        this.local.y = yPos + 16;


        
		super.drawScreen(mouseX, mouseY, partialTicks);
	
        //int i = 0;
		this.line = yPos + 32;
		
        for (int i = 0; i < this.commandButtons.size(); ++i)
        {
    		int num = i+1 < 10 ? i+1 : 0;
    		line = getNextLine();
    		this.commandButtons.get(i).setPosition(xPos, line);
    		//updateButton(xPos, line, i);
            this.commandButtons.get(i).drawButton(this.mc, mouseX, mouseY);
    		
            if(this.commandButtons.get(i).isHovered()) 
    		{
    			this.drawHoveringText(Arrays.asList(I18n.format("mp.quickCommands.hover", num), I18n.format("mp.quickCommands.hover2", new Object[0])), mouseX, mouseY, fontRenderer);
    			 GL11.glDisable(GL11.GL_LIGHTING);
    		}
        }
        
        if(this.global.isHovered())
        {
        	this.drawHoveringText(Arrays.asList(I18n.format("mp.quickCommands.global")), mouseX, mouseY, fontRenderer);
        }
        else if(this.local.isHovered())
        {
        	this.drawHoveringText(Arrays.asList(I18n.format("mp.quickCommands.local")), mouseX, mouseY, fontRenderer);	
        }

	}
		
}
