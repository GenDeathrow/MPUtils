package com.gendeathrow.mpbasic.client.gui;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mpbasic.client.InfoPanelPages;
import com.gendeathrow.mpbasic.client.InfoPanelPages.PageProperties;
import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;
import com.gendeathrow.mpbasic.world.SaveData;
import com.gendeathrow.mputils.api.client.gui.ScrollWindowBase;
import com.gendeathrow.mputils.api.client.gui.elements.TextScrollWindow;
import com.gendeathrow.mputils.client.gui.elements.TextEditor;
import com.gendeathrow.mputils.core.MPUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiInfoPanel extends ScrollWindowBase
{
	String textfile = "Found no File";
	protected InfoPanelPages infoFile;
	private TextEditor editor;
	
	private GuiButton Prev;
	private GuiButton Next;
	private GuiButton CloseThis;
	private GuiButton Agree;
	
	boolean hasLoaded = false;

	
	public GuiInfoPanel(GuiScreen paramGuiBase, InfoPanelPages infoFileIn) 
	{
		this(paramGuiBase, infoFileIn, false);
	}
	
	public GuiInfoPanel(GuiScreen paramGuiBase, InfoPanelPages infoFileIn, boolean dontRead) 
	{
		super(paramGuiBase);
		infoFile = infoFileIn;
		
		if(infoFile.forceRead() && !dontRead)
			setupTimer();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
		
		infoFile.readJson();

		updateText();
		
		this.buttonList.add(Prev = new GuiButton(12, (this.width - 200 +15)/2 , posY + sizeY + 5 , 60, 20, "Prev"));
		this.buttonList.add(CloseThis = new GuiButton(14, (this.width - 75 +15)/2 , posY + sizeY + 5 , 60, 20,  "Close"));
		this.buttonList.add(Next = new GuiButton(13, (this.width + 50 +15)/2 , posY + sizeY + 5 , 60, 20, "Next"));

		updateButtons();
		
		hasLoaded = true;
		

		
		SaveData.get(mc.world).addSeenPanel(InfoPanelConfigHandler.onLogInLoadInfoPage);		
//		if(Settings.editMode)
//		{
//			this.buttonList.add(new GuiButton(12, (this.width - 200)/2 , posY + sizeY + 5 , "Use Editor"));
//			this.buttonList.add(new GuiButton(13, (this.width - 200)/2 , posY + sizeY + 5  + 22, "Save File"));
//			
//			if(editor == null)
//			{
//				editor = new TextEditor(this);
//			
//				editor.area.setText(((TextScrollWindow)this.scrollWindow).getRawData());
//			}
//			else
//			{
//				editor.setTextArea((TextScrollWindow)scrollWindow);
//				((TextScrollWindow)scrollWindow).setRawData(editor.area.getText());
//			}
//		}
		
    }
	
	private Timer timer;
	private int countDown;
	private int secondsLeft;
	
	public void setupTimer() {
		timer = new Timer();
		setCountDown(20);
		TimerReset();
	}
	
    public void TimerReset() {
        secondsLeft = countDown;
        // Decrease seconds left every 1 second.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondsLeft--;
                if(hasLoaded)
                	updateButtons();
                if (secondsLeft <= 0) {
                     timer.cancel();
                }
            }
        }, 0, 1000);
    }

    public void setCountDown(int seconds) {
        this.countDown = seconds;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }
	
//	private void EditorSaveFile()
//	{
//		Tools.CreateSaveFile(new File(ConfigHandler.configDir + "/"+ MPBSettings.url), ((TextScrollWindow)this.scrollWindow).getRawData());
//	}
	
	protected void updateButtons() {
		
		PageProperties page = infoFile.getCurrentPageProperty();
		
		if(infoFile.canSwitchPages() && infoFile.getPageCnt() > 1) {
			if(infoFile.hasNextPage()) {
				Next.enabled = true;  Next.visible = true;
			}else {
				Next.enabled = false; Next.visible = true;
			}
			
			if(infoFile.hasPrevPage()) {
				Prev.enabled = true;  Prev.visible = true;
			} else {
				Prev.enabled = false; Prev.visible = true;
			}
		}else {	
			Prev.enabled = false; Prev.visible = false;
			Next.enabled = false; Next.visible = false;
		}
		
    	if(this.getSecondsLeft() > 0) {
    		this.close.enabled = false;
    		this.CloseThis.enabled = false;
       		this.CloseThis.displayString = "Close "+ getSecondsLeft();
    	}
    	else {
    		this.close.enabled = true;
    		this.CloseThis.enabled = true;
       		this.CloseThis.displayString = "Close";
    	}
	}
	
	
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
        	if(this.getSecondsLeft() <= 0)
        		super.keyTyped(typedChar, keyCode);
        }
        else
        	super.keyTyped(typedChar, keyCode);
    }
	
	protected void updateText() {
		try	{
			textfile = infoFile.readPage();
			
			scrollWindow = new TextScrollWindow(Minecraft.getMinecraft(), textfile, sizeX, sizeY, posY, posY + sizeY, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT+2);
			this.scrollWindow.setSlotXBoundsFromLeft(posX);		 
			this.setTitle(infoFile.getCurrentPageProperty().getTitle());
			
		} catch (Exception e) {
			MPUtils.logger.log(Level.ERROR, "Issue trying to load '"+ (infoFile.getCurrentPageFile() != null ? infoFile.getCurrentPageFile().getName() : "'page not found'") +"' File not found");
			e.printStackTrace();
		}
	}
	
	protected void actionPerformed(GuiButton button)
	{
		if (button.id == 2)
		{
			this.mc.displayGuiScreen(this.parent);
		}else if(button.id == Next.id)
		{
			infoFile.nextPage();
		}
		else if(button.id == Prev.id)
		{
			infoFile.prevPage();
		}
		else if(button.id == CloseThis.id)
		{
			this.mc.displayGuiScreen(null);
		}
		
		updateButtons();
		updateText();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
	   	super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if(editor != null)	editor.dispose();
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
