package com.gendeathrow.mputils.client.keybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.lwjgl.input.Keyboard;

import com.gendeathrow.mputils.client.gui.Gui_QuickMenu;

public class KeyBinds 
{
	public static KeyBinding menu;
	
	public static void Init()
	{
		menu = new KeyBinding(I18n.format("mp.quickCommands.title", new Object[0]), Keyboard.KEY_DECIMAL, "MPUtils");
		
		ClientRegistry.registerKeyBinding(menu);
	}
	
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		if(menu.isPressed())
		{
			Minecraft.getMinecraft().displayGuiScreen(new Gui_QuickMenu());
			//Minecraft.getMinecraft().displayGuiScreen(new StringFormattor());
		}
	}
	
}
