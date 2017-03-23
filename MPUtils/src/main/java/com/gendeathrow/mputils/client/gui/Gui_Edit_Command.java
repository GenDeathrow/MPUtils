package com.gendeathrow.mputils.client.gui;

import java.io.IOException;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.gendeathrow.mputils.client.settings.QuickCommandManager;
import com.gendeathrow.mputils.client.settings.QuickCommandManager.CommandElement;
import com.gendeathrow.mputils.client.settings.MPUtils_SaveHandler;

@SideOnly(Side.CLIENT)
public class Gui_Edit_Command extends GuiScreen
{
    /** Text field containing the command block's command. */
    private GuiTextField commandTextField;
    private GuiTextField titleTextField;
    private final CommandElement command;
    /** "Done" button for the GUI. */
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private int index;
    private int commandList;
    
    public Gui_Edit_Command(int commandList, CommandElement commandIn, int index)
    {
    	if(commandIn == null) this.command = QuickCommandManager.instance.new CommandElement(I18n.format("mp.text.newCmd", new Object[0]),"");
    	else this.command = commandIn;
    	this.index = index;
    	this.commandList = commandList;
    	
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.commandTextField.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @SuppressWarnings("unchecked")
	public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.commandTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 90, 300, 20);
        this.commandTextField.setMaxStringLength(32500);
        this.commandTextField.setFocused(true);
        this.commandTextField.setText(command.getCommand());
        
        this.titleTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
        this.titleTextField.setMaxStringLength(12);
        this.titleTextField.setFocused(false);
        this.titleTextField.setText(command.getTitle());
        
        if(this.commandTextField.getText().length()>0)
        {
        	 this.doneBtn.enabled = true;
        }
        else this.doneBtn.enabled = false;

     }

    public void func_184075_a()
    {

       this.doneBtn.enabled = true;

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
            if (button.id == 1)
            {
                this.mc.displayGuiScreen(new Gui_QuickMenu());
            }
            else if (button.id == 0)
            {
            	this.command.setCommand(this.commandTextField.getText());
            	this.command.setTitle(this.titleTextField.getText());
            	QuickCommandManager.instance.addCommand(QuickCommandManager.instance.getList(this.commandList), this.index , this.command);
            	MPUtils_SaveHandler.saveSettings();
            	this.mc.displayGuiScreen(new Gui_QuickMenu());
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode)
    {
    	
         this.commandTextField.textboxKeyTyped(typedChar, keyCode);
        this.titleTextField.textboxKeyTyped(typedChar, keyCode);

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
        
        if(this.commandTextField.getText().length()>0)
        {
        	 this.doneBtn.enabled = true;
        }
        else this.doneBtn.enabled = false;
    }
 
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.titleTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the screen and all the components in it.
     *  
     * @param mouseX Mouse x coordinate
     * @param mouseY Mouse y coordinate
     * @param partialTicks How far into the current tick (1/20th of a second) the game is
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("mp.quickCommands.edit.title", new Object[0]), this.width / 2, 25, 16777215);
        this.drawString(this.fontRendererObj, I18n.format("mp.text.title", new Object[0]), this.width / 2 - 150, 37, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 77, 10526880);
        this.commandTextField.drawTextBox();
        this.titleTextField.drawTextBox();
        
        int i = 120;
        int j = 0;
        this.drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.toggleBoolean", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);

        this.drawString(this.fontRendererObj, "", this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);


        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}