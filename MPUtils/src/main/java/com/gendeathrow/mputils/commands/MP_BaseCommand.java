package com.gendeathrow.mputils.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;


public abstract class MP_BaseCommand 
{
	public abstract String getCommand();
	
	public String getUsageSuffix()
	{
		return "";
	}
	
	/**
	 * Are the passed arguments valid?<br>
	 * NOTE: Argument 1 is always the returned value of getCommand()
	 */
	public boolean validArgs(String[] args)
	{
		return args.length == 1;
	}
	
	public List<String> autoComplete(MinecraftServer server, ICommandSender sender, String[] args)
	{
		return new ArrayList<String>();
	}
	
	public abstract void runCommand(CommandBase command, MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
	
	public final WrongUsageException getException(CommandBase command)
	{
		String message = command.getName() + " " + getCommand();
		
		if(getUsageSuffix().length() > 0)
		{
			message += " " + getUsageSuffix();
		}
		
		return new WrongUsageException(message);
	}
}
