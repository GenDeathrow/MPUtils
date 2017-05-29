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
import com.gendeathrow.mputils.utils.Tools;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class MP_ItemDump extends MP_BaseCommand
{


	//ArrayList options = new ArrayList<String>(Arrays.asList("nbt", "ore", "<>", "|parseexample %s|"));
	ArrayList options = new ArrayList<String>(Arrays.asList("ore", "nbt", "setPretty", "class", "extendedClass", "<>"));

	
	@Override
	public boolean validArgs(String[] args)
	{
		return true;
		//return args.length <= 1 + options.size();
	}
	
	
	@Override
	public String getUsageSuffix()
	{
		//return " [Args:(nbt, ore, <>, |string|)]";
		return " [Args:(nbt, ore, setPretty, class, extendedClass, <>)]";
	}
	
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		if(args.length >= 1) return options; 
		return new ArrayList<String>();
	}
	
	boolean isSilent = false;
	boolean useParser = false;
	
	protected String parseStackData(ICommandSender sender, String[] args, ItemStack stack)
	{				

		if(stack.isEmpty())
			return "";
		
		String itemID = stack.getItem().getRegistryName() + (stack.getItemDamage() != 0 ? ":"+ stack.getItemDamage() : "");
		String ore = "";
		String clazz = "";
		String extend = "";
		String nbt = "";
		String nbtPretty = "";
		
		boolean isPretty = false;
		
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

			boolean carretFlag = false;
			boolean oreFlag = false;
			boolean nbtflag = false;
			boolean classFlag = false;
			boolean extendFlag = false;
			boolean parseFlag = false;
			boolean skipFlag = false;
			
			// Check if pretty printing will happen
			for(String arg : args)
			{
				if(arg.toLowerCase().trim().equals("setpretty"))
				{
					isPretty = true;
				}
			}
			
			
			
			for(String arg : args)
			{

				if(arg.toLowerCase().trim().equals("<>") && !carretFlag && !skipFlag)
				{
					itemID = "<"+ itemID+">";
					carretFlag = true;
				}
				else if(arg.toLowerCase().trim().equalsIgnoreCase("ore")  && !oreFlag  && !skipFlag)
				{
					ore += " Ores:[";
					boolean f = false;
					for(int id : OreDictionary.getOreIDs(stack))
					{
						ore += (!f ? " ore:" : " | ore:")+ OreDictionary.getOreName(id) + (isPretty ? ConfigHandler.NEW_LINE : ",");
						printout.add("     <ore:"+ OreDictionary.getOreName(id) +">");
						f = true;
					}
					ore += "]";
					oreFlag = true;
				}else if(arg.toLowerCase().trim().equals("extendedclass") && !extendFlag && !skipFlag)
				{
					List<Class> extendedClasses = Tools.getAllSuperclasses(stack.getItem().getClass());
					extend += " Extended Classes:[";
					for(Class extendClass : extendedClasses)
					{
						extend += extendClass.getCanonicalName() + (isPretty ? ConfigHandler.NEW_LINE : ",");
					}
					extend += "]";
					
					printout.add("Extends "+ extendedClasses.size() +"x classes (more info on clipboard)");
					extendFlag = true;
				}
				else if(arg.toLowerCase().trim().equals("class")  && !classFlag  && !skipFlag)
				{
					clazz += " Class: "; 
					clazz += stack.getItem().getClass().getCanonicalName();
					printout.add("Class: "+ stack.getItem().getClass().getCanonicalName());
					classFlag = true;
				}
				else if((arg.toLowerCase().trim().equals("nbt")) && !nbtflag  && !skipFlag)
				{
					NBTTagCompound nbtdata = stack.getTagCompound();
				

					if(nbtdata != null)
					{
						nbt += ".withTag(";
						
						GsonBuilder gson = new GsonBuilder();
						gson.setPrettyPrinting();
						
						printout.add("NBT Data Found");
						nbt += nbtdata.toString() +")";
						//nbtPretty += gson.create().toJson(nbtdata.toString());
					}								

					nbtflag = true;
				}
				
				
				

//				else if((arg.toLowerCase().trim().startsWith("|") || skipFlag) && !flag4)
//				{
//					formatString += " "+arg;
//					
//					if(!arg.toLowerCase().trim().endsWith("|"))
//					{
//						skipFlag = true;
//						continue;
//					}
//					else skipFlag = false;
//					
//					itemID = String.format(formatString.trim().substring(1, (formatString.trim().length()-1)), itemID);
//					parseFlag = true;
//				}
				
			}
		}

		if(!isSilent)
		{
			printout.add("---------------");
			for(String print : printout)
			{
				sender.sendMessage(new TextComponentTranslation(print));
			}
		}
		
		if(isPretty)
		{
		
			returnback = "-------------------------------------"+ ConfigHandler.NEW_LINE;
			returnback += "ItemID: "+ itemID + ConfigHandler.NEW_LINE;
			returnback += ore + ConfigHandler.NEW_LINE;
			returnback += clazz + ConfigHandler.NEW_LINE;
			returnback += extend+ ConfigHandler.NEW_LINE;
			returnback += "NBTData: "+ nbt +ConfigHandler.NEW_LINE;
		}
		else
			returnback =  itemID + nbt + ore + clazz + extend + ConfigHandler.NEW_LINE;
		
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

	
	public class Parser
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		public Parser()
		{
			
		}
		
		public void addItem(ItemStack stack){
			this.items.add(stack);
		}
	}
}
