package com.gendeathrow.tipalert.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.commands.client.MP_ClientCommands;
import com.gendeathrow.tipalert.config.TipConfigHandler;

public class MP_ReloadTips extends MP_BaseCommand
{

	public static void init()
	{
		MP_ClientCommands.registerCmd(new MP_ReloadTips());	
	}
	
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
			TipConfigHandler.loadTips();
		}
	}

}
