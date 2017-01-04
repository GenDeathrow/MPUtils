package com.gendeathrow.mputils.commands.client.dumps;

import java.util.ArrayList;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.DungeonHooks.DungeonMob;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.gendeathrow.mputils.commands.MP_BaseCommand;

public class MP_ModDungeon extends MP_BaseCommand
{

	@Override
	public String getCommand() 
	{
		return "dumpMobDungeon";
	}

	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
	{

	}

	
	
}
