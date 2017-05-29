package com.gendeathrow.mputils.core;

import com.gendeathrow.mputils.api.client.gui.readers.GuiReader;
import com.gendeathrow.mputils.inventory.container.ContainerNBTBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		switch (ID)
		{
			case 0:
				return new ContainerNBTBase(player);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) 
	{
		switch (ID)
		{
			case 0:
				return new GuiReader(player);
		}
		
		return null;
	}

}
