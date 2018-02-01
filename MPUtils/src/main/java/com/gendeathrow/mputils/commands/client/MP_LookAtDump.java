package com.gendeathrow.mputils.commands.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.Tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;

public class MP_LookAtDump extends MP_BaseCommand
{

	ArrayList options = new ArrayList<String>(Arrays.asList("nbt", "class", "extendedClass"));
	
	@Override
	public String getCommand() 
	{
		return null;
	}

	@Override
	public String getUsageSuffix()
	{
		return " [Args:(nbt, class, extendedClass)]";
	}
	
	@Override
	public boolean validArgs(String[] args)
	{
		return true;
	}
	
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		if(args.length >= 1) return options; 
		return new ArrayList<String>();
	}
	
	@Override
	public void runCommand(CommandBase command, MinecraftServer server, ICommandSender sender, String[] args) 
	{
		
	}
	

	
	protected String parseStackData(ICommandSender sender, String[] args, Entity lookingAt)
	{
		boolean nbtFlag = false;
		boolean classFlag = false;
		boolean extendedFlag = false;
		
		for(String arg : args)
		{
			
			if(arg.toLowerCase().trim().equals("extendedclass"))
				extendedFlag = true;
			else if(arg.toLowerCase().trim().equals("class"))
				classFlag = true;
			else if(arg.toLowerCase().trim().equals("nbt"))
				nbtFlag = true;
		}
		
		
		
		
		ModContainer modContainer = FMLCommonHandler.instance().findContainerFor(lookingAt);
		String EntityString = EntityList.getEntityString(lookingAt); 
		Class clazz = lookingAt.getClass();

		 
		String formattedString  = TextFormatting.ITALIC.YELLOW +"Localized Name: " + TextFormatting.RESET + lookingAt.getDisplayName().getFormattedText() + "\n"
								 +TextFormatting.ITALIC.YELLOW +"StringID: "  + TextFormatting.RESET + EntityString  + "\n"
								 +TextFormatting.YELLOW +"IntID: "  + TextFormatting.RESET + EntityList.getID(lookingAt.getClass()) + "\n"
								 +TextFormatting.ITALIC.YELLOW +"ModID: " + TextFormatting.RESET + (modContainer == null ? "minecraft" : modContainer.getModId()) + "\n";

		String clipboardstring = "Localized Name: "+ lookingAt.getDisplayName().getUnformattedText() + ConfigHandler.NEW_LINE;
		clipboardstring +=" StringID: "+ EntityString + ConfigHandler.NEW_LINE;
		clipboardstring +=" IntID: "+ EntityList.getID(lookingAt.getClass()) + ConfigHandler.NEW_LINE;
		clipboardstring +=" ModID: "+ (modContainer == null ? "minecraft" : modContainer.getModId()) + ConfigHandler.NEW_LINE;

		

				
		if(classFlag)
		{
			clipboardstring += " Class: "+ lookingAt.getClass().getCanonicalName() + ConfigHandler.NEW_LINE;
			formattedString += TextFormatting.ITALIC.YELLOW +"Class: " + TextFormatting.RESET + clazz.getCanonicalName()+ "\n";
		}
		
		List<Class> extendedClasses = Tools.getAllSuperclasses(clazz);

		
		if(extendedFlag)
		{
			clipboardstring +=" ExtendedClasses: [ "+ ConfigHandler.NEW_LINE;
	
	
			for(Class extendClass : extendedClasses)
			{
				clipboardstring += extendClass.getCanonicalName() + ConfigHandler.NEW_LINE;
			}
			clipboardstring += "]" + ConfigHandler.NEW_LINE;
			
			formattedString += TextFormatting.YELLOW + "Extends: "+ TextFormatting.RESET + extendedClasses.size() +"x Classes (on clipboard)" + "\n";
		}
		


		if(nbtFlag)
		{
			clipboardstring += "NBTTag: "+ lookingAt.writeToNBT(new NBTTagCompound()).toString();
			formattedString += TextFormatting.YELLOW + "NBT: "+ TextFormatting.RESET + "Found NBT Data";
		}


		
		sender.sendMessage(new TextComponentTranslation(formattedString));

		Tools.CopytoClipbard(clipboardstring);

		return null;
		
	}

	protected String parseStackData(ICommandSender sender, String[] args, World world, IBlockState block, BlockPos blockpos)
	{
		
		boolean nbtFlag = false;
		boolean classFlag = false;
		boolean extendedFlag = false;
		
		for(String arg : args)
		{
			
			if(arg.toLowerCase().trim().equals("extendedclass"))
				extendedFlag = true;
			else if(arg.toLowerCase().trim().equals("class"))
				classFlag = true;
			else if(arg.toLowerCase().trim().equals("nbt"))
				nbtFlag = true;
		}
		ModContainer modContainer = FMLCommonHandler.instance().findContainerFor(block);
		ResourceLocation registryName = block.getBlock().getRegistryName(); 
		Class clazz = block.getBlock().getClass();
		
		List<Class> extendedClasses = Tools.getAllSuperclasses(clazz);
		
		String clipboardstring = block.getBlock().getRegistryName() + (block.getBlock().getMetaFromState(block) != 0 ? ":"+ block.getBlock().getMetaFromState(block) : "" + ConfigHandler.NEW_LINE);
			
		String formattedString = TextFormatting.ITALIC.YELLOW 
				 +"Registry Name: "+ TextFormatting.RESET + registryName + "\n" + TextFormatting.ITALIC.YELLOW 
				 +"  Meta: "+ TextFormatting.RESET + block.getBlock().getMetaFromState(block) + "\n"  + TextFormatting.ITALIC.YELLOW
				 +"  ModID: "+ TextFormatting.RESET + (modContainer == null ? "minecraft" : modContainer.getModId()) + "\n";
				

		
		if(classFlag)
		{
			clipboardstring += "Class: "+ block.getBlock().getClass().getCanonicalName() + ConfigHandler.NEW_LINE;
			formattedString +=  TextFormatting.ITALIC.YELLOW + "  Class: "+ TextFormatting.RESET + clazz.getCanonicalName() + "\n" + TextFormatting.ITALIC.YELLOW;
		}
			
		if(extendedFlag)
		{
			clipboardstring += "Extended Classes: [" + ConfigHandler.NEW_LINE;
			for(Class extendClass : extendedClasses)
			{	
				clipboardstring += extendClass.getCanonicalName() + ConfigHandler.NEW_LINE;
			}
			clipboardstring += "]" + ConfigHandler.NEW_LINE;
			
			formattedString +=  TextFormatting.ITALIC.YELLOW +"  Extends: "+ TextFormatting.RESET + extendedClasses.size() +"x Classes (on clipboard) " + "\n";
		}
		

		
		if(nbtFlag)
		{
			TileEntity te = world.getTileEntity(blockpos);
			if(te != null)
			{
				String nbt = te.writeToNBT(new NBTTagCompound()).toString();
				
				clipboardstring += "NBTTag: "+ nbt;
				
				formattedString += TextFormatting.ITALIC.YELLOW +"  NBT: "+ TextFormatting.RESET + "Found NBT Data" + "\n";
			}
		}
		

		 			

		Tools.CopytoClipbard(clipboardstring);

		sender.sendMessage(new TextComponentTranslation(formattedString));
		
		return null;
		
	}
}
