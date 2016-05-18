package com.gendeathrow.mputils.commands.client;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.prompt.TipList;

public class MP_ReloadTips extends MP_BaseCommand
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
			
			sender.addChatMessage(new TextComponentTranslation(TextFormatting.YELLOW +""+TipList.dimensionList.size() +""+ TextFormatting.RESET + " Dimensions Loaded"));
			sender.addChatMessage(new TextComponentTranslation(TextFormatting.YELLOW +""+TipList.blockLookTipList.size() +""+ TextFormatting.RESET + " Looking At Blocks Loaded"));
			sender.addChatMessage(new TextComponentTranslation(TextFormatting.YELLOW +""+TipList.toolTipList.size() +""+ TextFormatting.RESET + " ToolTips Loaded"));
			if(TipList.getErroredLoads() > 0)
			{
				sender.addChatMessage(new TextComponentTranslation(TextFormatting.RED +""+ TipList.getErroredLoads() + " Errors Found; Check Logs!"));
			}
			sender.addChatMessage(new TextComponentTranslation(TextFormatting.YELLOW +"Tips.json Reloaded!"));
		}
		
	}

}
