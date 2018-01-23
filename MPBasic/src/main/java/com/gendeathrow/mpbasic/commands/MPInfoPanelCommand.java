package com.gendeathrow.mpbasic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.gendeathrow.mpbasic.client.InfoPanelPages;
import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;
import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.commands.common.MP_Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class MPInfoPanelCommand extends MP_BaseCommand{

	
	public static void register() {
		MP_Commands.registerCmd(new MPInfoPanelCommand());
	}
	
	@Override
	public String getCommand() {
		return "infopanel";
	}

	
	@Override
	public boolean validArgs(String[] args)
	{
		return args.length >= 1;
	}
	
	
	@Override
	public String getUsageSuffix()
	{
		return " <infopanel_file_name>";
	}
	
	@Override
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for( Entry<String, InfoPanelPages> page : InfoPanelConfigHandler.PAGES.entrySet()) {
			list.add(page.getKey());
		}
			
		if(args.length >= 1) return list; 
		return new ArrayList<String>();
	}
	
	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) {
		
		String pageKey = args[1];
		 
		if(InfoPanelConfigHandler.PAGES.containsKey(args[1])) {
			if(sender.getCommandSenderEntity() instanceof EntityPlayer)
				InfoPanelConfigHandler.giveBook((EntityPlayer) sender.getCommandSenderEntity(), args[1]);
		}
		else if(args[1].toLowerCase() == "giveallbooks") {
			if(sender.getCommandSenderEntity() instanceof EntityPlayer)
				InfoPanelConfigHandler.giveAllBooks((EntityPlayer) sender.getCommandSenderEntity());
		}



	}

}
