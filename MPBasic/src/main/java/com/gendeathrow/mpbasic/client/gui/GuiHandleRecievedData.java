package com.gendeathrow.mpbasic.client.gui;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import com.gendeathrow.mputils.utils.RenderAssist;
import com.google.gson.JsonObject;

public class GuiHandleRecievedData extends GuiScreen
{

	JsonObject inputPacket;
	GuiScreen parent;
	GuiButton close; 

	public GuiHandleRecievedData(GuiScreen parent, JsonObject json)
	{
			this.inputPacket = json;
			this.parent = parent;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public void initGui()
    {

		this.buttonList.add(close = new GuiButton(1, this.width/2, this.height/2 + 30, 50, 20, "Close"));
	}
	
	protected void actionPerformed(GuiButton button)
	{
		if (button == close)
		{
			this.mc.displayGuiScreen(this.parent);
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		
		String message = inputPacket.get("message").getAsString();
		
		int messageWidth = this.fontRendererObj.getStringWidth(message);
		
		int xpos = (this.width - messageWidth)/2 - 5;
		int ypos = this.height/2 - 5;
		RenderAssist.drawRect(xpos, ypos, xpos + messageWidth + 10, ypos + 20, Color.gray.getRGB());
		RenderAssist.drawUnfilledRect(xpos, ypos, xpos + messageWidth + 10, ypos + 20, Color.white.getRGB());
		
		this.drawCenteredString(this.fontRendererObj, message , this.width/2, this.height/2, Color.yellow.getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
