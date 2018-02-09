package com.gendeathrow.mpbasic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.gendeathrow.mpbasic.configs.NotificationsConfigs;
import com.gendeathrow.mpbasic.configs.NotificationsConfigs.NotificationObject;
import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.commands.common.MP_Commands;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.network.ScreenNotificationPacket;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class SendOnScreenNoticeCommand extends MP_BaseCommand
{

	public static void register() {
		MP_Commands.registerCmd(new SendOnScreenNoticeCommand());
	}
	
	@Override
	public String getCommand() 
	{
		return "notify";
	}
	
	@Override
	public String getUsageSuffix()
	{
		return "/mpa notify <playerName> <onscreen notification id>";
	}
	
	@Override
	public boolean validArgs(String[] args)
	{
		return args.length >= 3;
	}
	
	@Override
	public List<String> autoComplete(MinecraftServer server, ICommandSender sender, String[] args)
	{
		ArrayList<String> list = new ArrayList<String>();

		if(args.length == 2) {
			 return MP_Commands.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		else if(args.length == 3) {
			for( Entry<String, NotificationObject> page : NotificationsConfigs.LoadedNotifications.entrySet()) {
				list.add(page.getKey());
			}
			MP_Commands.getListOfStringsMatchingLastWord(args, list);
		}
		
		return list;
	}
	

	@Override
	public void runCommand(CommandBase command, MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
	
		
        if (args.length < 2) {
            throw new WrongUsageException(getUsageSuffix(), new Object[0]);
        }
        else {
            EntityPlayerMP entityplayer = MP_Commands.getPlayer(server, sender, args[1]);
            
        	if(NotificationsConfigs.LoadedNotifications.containsKey(args[2])) {
        		NotificationObject notify = NotificationsConfigs.LoadedNotifications.get(args[2]);
        		if(sender.getCommandSenderEntity() instanceof EntityPlayerMP)
        			MPUtils.network.sendTo(new ScreenNotificationPacket(notify.lines, notify.soundLocation), (EntityPlayerMP) sender.getCommandSenderEntity());
     		}
        		else
        			throw new WrongUsageException("Error with "+ args[2] + " || "+ getUsageSuffix(), new Object[0]);
       	}

	}

}
