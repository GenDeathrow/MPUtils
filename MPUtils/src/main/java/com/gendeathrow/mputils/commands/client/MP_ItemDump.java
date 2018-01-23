package com.gendeathrow.mputils.commands.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.MTNBTConverter;
import com.gendeathrow.mputils.utils.Tools;
import com.google.gson.GsonBuilder;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class MP_ItemDump extends MP_BaseCommand
{


	//ArrayList options = new ArrayList<String>(Arrays.asList("nbt", "ore", "<>", "|parseexample %s|"));
	ArrayList options = new ArrayList<String>(Arrays.asList("ore", "nbt", "setPretty", "class", "extendedClass", "<>", "onlyhasrecipes"));

	
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
		return " [Args:(nbt, ore, setPretty, class, extendedClass, <>, onlyhasrecipes)]";
	}
	
	@Override
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		if(args.length >= 1) return options; 
		return new ArrayList<String>();
	}
	
	boolean isSilent = false;
	boolean useParser = false;
	
	public String parseStackData(ICommandSender sender, String[] args, ItemStack stack)
	{				

		if(stack.isEmpty() || stack.getItem() == Items.AIR)
			return "";
		
		List<String> printout = new ArrayList<String>();

		String returnback = parserItemstack(args, stack, printout);
		
		if(!isSilent)
		{
			for(String print : printout)
			{
				sender.sendMessage(new TextComponentTranslation(print));
			}
		}
		
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

	public static String parserItemstack(String[] args, ItemStack stack, List<String> printout)
	{
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
		
		boolean recipeFlag = false;
		boolean hasRecipe = false;
		
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
					NBTBase nbtdata = stack.getTagCompound();
				

					if(nbtdata != null)
					{
						nbt += ".withTag(";
						
						GsonBuilder gson = new GsonBuilder();
						gson.setPrettyPrinting();

						printout.add("NBT Data Found");
						
						if(Loader.isModLoaded("crafttweaker"))
							nbt += MTNBTConverter.from(nbtdata, false).toString() +")";
						else
							nbt += nbtdata.toString() +")";
					}								

					nbtflag = true;
				}else if(arg.toLowerCase().trim().equals("onlyhasrecipes") && !recipeFlag)
				{
					
					recipeFlag = true;
					
					 Iterator<IRecipe> recipeList = CraftingManager.REGISTRY.iterator();
					while(recipeList.hasNext())
					{
						IRecipe recipe = recipeList.next();
						
						if(recipe.getRecipeOutput().getItem() == stack.getItem() && recipe.getRecipeOutput().getItemDamage() == stack.getItemDamage())
						{
							hasRecipe = true;
							break;
						}
					}
					
					
				}
				
			}
		}

		if(recipeFlag && !hasRecipe)
		{
			return "";
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
}
