package com.gendeathrow.mpbasic.client.gui;

import java.io.File;
import java.io.IOException;

import com.gendeathrow.mputils.utils.MPFileUtils;
import com.gendeathrow.mputils.utils.MPInfo;
import com.gendeathrow.mputils.utils.RenderAssist;
import com.gendeathrow.mputils.utils.Tools;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.common.Loader;

public class GuiBugReporter extends GuiScreen
{
	public static long timeStamp = 0;
	
	GuiScreen parent;
	
//	GuiButton copyClip;
	GuiButton cancel;
	GuiButton send;
	GuiCheckBox sendCrashLog;
	
	GuiTextField title;
	GuiTextArea desc;
	public static String descText = "";
	public static String titleText = "";
	
	
//	GuiButton viewLatestError;
//	GuiButton copyLatestError; // pasg
	
	public GuiBugReporter(GuiScreen parentIn)
	{
		this.parent = parentIn;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public void initGui()
    {


		desc = new GuiTextArea(this.fontRenderer, (this.width-250)/2 , (this.height-200)/2, 250, 200);

		title = new GuiTextField(0, fontRenderer, desc.xPosition, desc.yPosition - 20, 250, 15);
		
		title.setText(titleText);
		
		desc.setText(descText);

		desc.setFocused(true);
		
		this.buttonList.add(cancel = new GuiButton(1, this.width/2 - 50 + 5, this.desc.yPosition+this.desc.height + 5, 50, 20, "Cancel"));
		this.buttonList.add(send = new GuiButton(2, this.width/2 + 5, this.desc.yPosition+this.desc.height + 5, 50, 20, "Send"));
		this.buttonList.add(sendCrashLog = new GuiCheckBox(3, this.desc.xPosition+this.desc.width + 5, this.desc.yPosition+2, "Send Latest Crashlog", false));
		
		
		if(this.desc.getText().isEmpty() || this.title.getText().isEmpty())
		{
			send.enabled = false;
		}
		
	
    }
	
    public void updateScreen()
    {
        this.desc.updateCursorCounter();
        this.title.updateCursorCounter();
        
		if(this.desc.getText().isEmpty() || this.title.getText().isEmpty())
		{
			send.enabled = false;
		}else send.enabled = true;
		
		
    	super.updateScreen();
    }

    protected void keyTyped(char p_73869_1_, int p_73869_2_) throws IOException
    {
    
    	this.desc.keyTyped(p_73869_1_, p_73869_2_);
    	this.title.textboxKeyTyped(p_73869_1_, p_73869_2_);
    	
    	descText = this.desc.getText();
    	titleText = this.title.getText();
    	super.keyTyped(p_73869_1_, p_73869_2_);
    }
    
    /**
     * Called when the mouse is clicked.
     * @throws IOException 
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {

        this.desc.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        
    	this.title.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    	
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }
    
	protected void actionPerformed(GuiButton button)
	{
		if (button == cancel)
		{
			this.mc.displayGuiScreen(this.parent);
		}
		else if (button == send)
		{
			if((Minecraft.getSystemTime() - this.timeStamp) <= 60000) return;
			this.sendData();
			this.timeStamp = Minecraft.getSystemTime();
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{

		this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		

		
		RenderAssist.drawRect(2, 2, this.width - 2, this.height - 2, java.awt.Color.black.getRGB());
		
		
		drawString(this.fontRenderer, "OS: "+ System.getProperty("os.name"), this.title.x + 2, this.title.y - 50, java.awt.Color.yellow.getRGB());
		
		drawString(this.fontRenderer, "MC: "+ MinecraftForge.MC_VERSION, this.title.x + 2, this.title.y - 20, java.awt.Color.yellow.getRGB());
		drawString(this.fontRenderer, "MP: 1.0.0", this.title.x + 2, this.title.y - 35, java.awt.Color.yellow.getRGB());
		
		drawString(this.fontRenderer, ForgeVersion.getVersion(), this.title.x + (this.title.width/2)+ 2, this.title.y - 20, java.awt.Color.yellow.getRGB());
		drawString(this.fontRenderer, "Loaded Mods: "+ Loader.instance().getActiveModList().size(), this.title.x + (this.title.width/2)+ 2, this.title.y - 35, java.awt.Color.yellow.getRGB());
		this.desc.drawTextBox();
		this.title.drawTextBox();
		//System.out.println(mouseX);
		if(this.desc.getText().isEmpty())
		{
			//this.fontRendererObj.drawString(, p_78276_2_, p_78276_3_, java.awt.Color.white.getRGB())
			this.drawCenteredString(this.fontRenderer, "Enter Description", this.desc.xPosition + (this.desc.width/2), this.desc.yPosition+(this.desc.height/2), java.awt.Color.DARK_GRAY.getRGB());
		}
		
		if(this.title.getText().isEmpty())
		{
			//this.fontRendererObj.drawString(, p_78276_2_, p_78276_3_, java.awt.Color.white.getRGB())
			this.drawCenteredString(this.fontRenderer, "Enter Title", this.title.x + (this.title.width/2), this.title.y+(this.title.height/2), java.awt.Color.DARK_GRAY.getRGB());
		}
		
		this.drawCenteredString(this.fontRenderer, "Issue Tracker", this.width/2, 10, java.awt.Color.yellow.getRGB());

		
		if((Minecraft.getSystemTime() - this.timeStamp) <= 60000)
		{
			this.drawCenteredString(this.fontRenderer, "Must wait to send another Bug Report", this.width/2, this.height/2, java.awt.Color.red.getRGB());
		}
				
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void sendData()
	{
		JsonObject postData = new JsonObject();
		
		postData.addProperty("title", this.title.getText());
		postData.addProperty("desc", this.desc.getText());
		postData.addProperty("ign", Minecraft.getMinecraft().getSession().getUsername());
		postData.addProperty("forgeVersion", ForgeVersion.getVersion().substring(16));
		postData.addProperty("mcVersion", MinecraftForge.MC_VERSION);
		postData.addProperty("mpVersion", MPInfo.version);
		postData.addProperty("os", System.getProperty("os.name"));
		postData.addProperty("modsLoaded",  Loader.instance().getActiveModList().size());
		
		if(this.sendCrashLog.isChecked())
		{
			try 
			{
				postData.addProperty("crashLogFile", getCrashLog().getName());
				postData.addProperty("crashLog", MPFileUtils.readFile(getCrashLog().getPath()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try 
		{
			handleRecievedData(Tools.sendJsonHttpPost("https://script.google.com/macros/s/AKfycbxHmeu9X6gDj-uWjfQwdE4-q1QRKxQIUxYnANZOsamWrUN2tAU/exec", postData));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleRecievedData(String recieved)
	{
		try
		{
			JsonParser jsonParser = new JsonParser();
			
			JsonElement element = jsonParser.parse(recieved);
			
			this.mc.displayGuiScreen(new GuiHandleRecievedData(parent, element.getAsJsonObject()));
			
		}catch(Exception e)
		{
			
		}
		
	}
	
	private File getCrashLog()
	{
		return Tools.lastFileModified("crash-reports");
	}
	
	
}
