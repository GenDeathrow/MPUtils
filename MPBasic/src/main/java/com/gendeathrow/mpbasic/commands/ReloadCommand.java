package com.gendeathrow.mpbasic.commands;

import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;
import com.gendeathrow.mpbasic.configs.NotificationsConfigs;
import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.commands.common.MP_Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class ReloadCommand extends MP_BaseCommand{

	
	public static void register() {
		MP_Commands.registerCmd(new ReloadCommand());
	}
	
	@Override
	public String getCommand() {
		return "reload";
	}

	
	@Override
	public String getUsageSuffix()
	{
		return "/mpa reload";
	}
	
	
	@Override
	public void runCommand(CommandBase command, MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		NotificationsConfigs.reloadConfig();
		InfoPanelConfigHandler.readInfoPanelConfigs();
	}
}