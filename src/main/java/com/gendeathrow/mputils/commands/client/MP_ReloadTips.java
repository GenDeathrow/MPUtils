package com.gendeathrow.mputils.commands.client;

import com.gendeathrow.mputils.configs.ConfigHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class MP_ReloadTips extends MP_ClientBaseCommand
{

	@Override
	public String getCommand() 
	{
		return "reloadTips";
	}

	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
	{
		if(sender != null && sender instanceof EntityPlayer)
		{
			ConfigHandler.loadTips();
		}
		
	}

}
