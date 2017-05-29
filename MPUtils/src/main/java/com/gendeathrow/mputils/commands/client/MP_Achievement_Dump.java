package com.gendeathrow.mputils.commands.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.utils.Tools;

public class MP_Achievement_Dump extends MP_BaseCommand
{
	
	String fileData;
	
	HashMap<String, ArrayList<Achievement>> dumpList = new HashMap<String, ArrayList<Achievement>>();

	@Override
	public String getCommand() 
	{
		return "dumpAchievements";
	}

	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
	{
		fileData = "";
		addLine("######################################################################################");
		addLine("#                                     MPUTILS                                        #");
		addLine("# -----------------------------------------------------------------------------------#");
		addLine("#  This tool is used to find dump all Achievements                                   #");
		addLine("#  Mod ID may not be correctly selected. Achievmenets don't contain a modid          #");
		addLine("######################################################################################");
		addLine("");addLine("");addLine("");
		
		dumpList.clear();


			
			
			int i=1;
			
			for(AchievementPage page : AchievementPage.getAchievementPages())
			{
				addLine("### MODID:"+ page.getName());		
				
				
				for(Achievement stat : page.getAchievements())
				{
						addLine("   "+ (i++) +". ID=" + stat.statId + ", Name=" + stat.getStatName().getUnformattedComponentText() + ", awardLocallyOnly=" + stat.isIndependent +", Parent: "+ (stat.parentAchievement != null ? stat.parentAchievement.getStatName().getUnformattedComponentText() : "N/A"));
						addLine("");
					addLine("");
				}
			}
			
			
			
			addLine("### MODID: minecraft");	
			for(Achievement stat : AchievementList.ACHIEVEMENTS)
			{
							
			
				String[] split = stat.statId.split("\\.");
			
				String modid;
				if(split.length == 2)
					modid = "minecraft";
				else continue;
	
			
				addLine("   "+ (i++) +". ID=" + stat.statId + ", Name=" + stat.getStatName().getUnformattedComponentText() + ", awardLocallyOnly=" + stat.isIndependent +", Parent: "+ (stat.parentAchievement != null ? stat.parentAchievement.getStatName().getUnformattedComponentText() : "N/A"));
				addLine("");
			}
		
		for(Entry<String, ArrayList<Achievement>> list : dumpList.entrySet())
		{
			sender.addChatMessage(new TextComponentTranslation("Found "+ AchievementList.ACHIEVEMENTS.size() +" Achievements"));
		}
		
		String reply = Tools.CreateSaveFile(new File(Minecraft.getMinecraft().mcDataDir, "mputils/achievementDump.txt"), fileData);

		sender.addChatMessage(new TextComponentTranslation(reply));
	}

	
	private void addLine(String newLine)
	{
		fileData += newLine + System.getProperty("line.separator");
	}
	
}
