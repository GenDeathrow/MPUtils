package com.gendeathrow.mpbasic.configs;

import java.util.Set;

import com.gendeathrow.mpbasic.client.gui.config.GuiBTConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class BTConfigGuiFactory implements IModGuiFactory
{

	@Override
	public void initialize(Minecraft minecraftInstance) 
	{
		
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() 
	{
		return null;
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiBTConfig(parentScreen);
	}

}
