package com.gendeathrow.mputils.commands.client.dumps;

import com.gendeathrow.mputils.commands.MP_BaseCommand;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class MP_ModDungeon extends MP_BaseCommand
{

	@Override
	public String getCommand() 
	{
		return "dumpMobDungeon";
	}

	@Override
	public void runCommand(CommandBase command, MinecraftServer server, ICommandSender sender, String[] args) 
	{

	}

	
	
}
