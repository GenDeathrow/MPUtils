package com.gendeathrow.mputils.commands.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.oredict.OreDictionary;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.NBTConverter;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class MP_ItemDump extends MP_BaseCommand
{


	ArrayList options = new ArrayList<String>(Arrays.asList("nbt", "ore", "<>", "|parseexample %s|"));

	
	@Override
	public boolean validArgs(String[] args)
	{
		return true;
		//return args.length <= 1 + options.size();
	}
	
	
	@Override
	public String getUsageSuffix()
	{
		return " [Args:(nbt, ore, <>, |string|)]";
	}
	
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		if(args.length >= 1) return options; 
		return new ArrayList<String>();
	}
	
	
	protected String parseStackData(ICommandSender sender, String[] args, ItemStack stack)
	{

		
		String itemID = stack.getItem().getRegistryName() + (stack.getItemDamage() != 0 ? ":"+ stack.getItemDamage() : "");
		String ore = "";
		String nbt = "";
		
		String formatString = "";
		String format = "";
		
		String returnback = "";
		

		
		List<String> printout = new ArrayList<String>();
		printout.add(itemID);
		
		if(args.length > 1)
		{
			String completeCommand = "";
			
			for(String arg : args)
			{
				completeCommand += " "+ arg;
			}

			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			boolean flag4 = false;
			
			boolean skipFlag = false;
			
			for(String arg : args)
			{

				if(arg.toLowerCase().trim().equals("<>") && !flag1 && !skipFlag)
				{
					itemID = "<"+ itemID+">";
					flag1 = true;
				}
				else if(arg.toLowerCase().trim().equals("ore")  && !flag2  && !skipFlag)
				{
					ore += " Ores:[";
					boolean f = false;
					for(int id : OreDictionary.getOreIDs(stack))
					{
						ore += (!f ? " ore:" : " | ore:")+ OreDictionary.getOreName(id);
						printout.add("     <ore:"+ OreDictionary.getOreName(id) +">");
						f = true;
					}
					ore += "]";
					flag2 = true;
				}
				else if(arg.toLowerCase().trim().equals("nbt") && !flag3  && !skipFlag)
				{
					NBTTagCompound nbtdata = stack.getTagCompound();
				
					nbt += " NBT:";
					if(nbtdata != null)
					{
						printout.add("NBT Data Found");
						nbt += " "+ new GsonBuilder().create().toJson(NBTConverter.NBTtoJSON_Compound(nbtdata, new JsonObject()));
					}								
					else nbt += "{NBT Null}";
					flag3 = true;
				}
				else if((arg.toLowerCase().trim().startsWith("|") || skipFlag) && !flag4)
				{
					formatString += " "+arg;
					
					if(!arg.toLowerCase().trim().endsWith("|"))
					{
						skipFlag = true;
						continue;
					}
					else skipFlag = false;
					
					itemID = String.format(formatString.trim().substring(1, (formatString.trim().length()-1)), itemID);
					flag4 = true;
				}
				
			}
		}

		printout.add("---------------");
		for(String print : printout)
		{
			sender.addChatMessage(new TextComponentTranslation(print));
		}
		returnback =  itemID + ore + nbt + ConfigHandler.NEW_LINE;
		
		return returnback;
	}
	
	@Override
	public String getCommand() 
	{
		return null;
	}

	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
	{
		
	}

}
