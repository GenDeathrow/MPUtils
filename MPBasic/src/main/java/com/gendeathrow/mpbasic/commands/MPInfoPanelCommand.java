package com.gendeathrow.mpbasic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.gendeathrow.mpbasic.api.IInfoPanelData;
import com.gendeathrow.mpbasic.common.infopanel.CapabilityInfoPanel;
import com.gendeathrow.mpbasic.common.infopanel.InfoPanelPages;
import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;
import com.gendeathrow.mpbasic.core.MPBasic;
import com.gendeathrow.mpbasic.network.InfoPanelUpdate;
import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.commands.common.MP_Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

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
		return "/mpa infopanel <playerName> <giveBook/openGui> <infopanel_file_name>";
	}
	
	@Override
	public List<String> autoComplete(MinecraftServer server, ICommandSender sender, String[] args)
	{
		ArrayList<String> list = new ArrayList<String>();

		if(args.length == 2) {
			 return MP_Commands.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		else if(args.length == 3) {
			return MP_Commands.getListOfStringsMatchingLastWord(args, "giveBook","openGui");
		}
		else if(args.length == 4) {
			for( Entry<String, InfoPanelPages> page : InfoPanelConfigHandler.PAGES.entrySet()) {
				list.add(page.getKey());
			}
			
			MP_Commands.getListOfStringsMatchingLastWord(args, list);
		}
		
		return list;
	}
	
	@Override
	public void runCommand(CommandBase command, MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
        if (args.length < 3) {
            throw new WrongUsageException(getUsageSuffix(), new Object[0]);
        }
        else {
            EntityPlayerMP entityplayer = MP_Commands.getPlayer(server, sender, args[1]);
            
        	if(InfoPanelConfigHandler.PAGES.containsKey(args[3])) {
        		if(args[2].equalsIgnoreCase("givebook"))
        			InfoPanelConfigHandler.giveBook(entityplayer, args[3]);
        		else if (args[2].equalsIgnoreCase("openGui")) {

        			boolean hasSeen = false;
                    IInfoPanelData cap = CapabilityInfoPanel.getInfoPanelData(entityplayer);
                    if(cap != null)
                    	hasSeen = cap.hasBeenGivinBook(args[3]);
            		
                    MPBasic.network.sendTo(new InfoPanelUpdate(args[3], hasSeen), entityplayer);
        		}
        		else
        			throw new WrongUsageException("Error with "+ args[2] + " || "+ getUsageSuffix(), new Object[0]);
        	}

        }


	}

}
