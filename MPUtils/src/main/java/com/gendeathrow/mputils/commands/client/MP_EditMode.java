package com.gendeathrow.mputils.commands.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentTranslation;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.Settings;

public class MP_EditMode extends MP_BaseCommand
{

	@Override
	public String getCommand() 
	{
		return "EditMode";
	}

	@Override
	public boolean validArgs(String[] args)
	{
		return args.length <= 2 ? true : false;
	}
	
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		if(args.length >= 2) return new ArrayList<String>(Arrays.asList("true","false")); 
		return new ArrayList<String>();
	}
	
	
	@Override
	public String getUsageSuffix()
	{
		return "{true,false}"; 	
	}
	
	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
	{
		if(args.length > 1) 
		{
			Settings.editMode = "true".equals(args[1]) ? true : false;
			if(Settings.editMode) sender.addChatMessage(new TextComponentTranslation("You have entered MPUtils Edit Mode. This will open up features for MPutils and Addons if Any"));
		}
	}

}
