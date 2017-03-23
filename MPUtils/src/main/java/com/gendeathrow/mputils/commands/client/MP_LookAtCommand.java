package com.gendeathrow.mputils.commands.client;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.Tools;

public class MP_LookAtCommand extends MP_BaseCommand
{

	public static IBlockState currentBlock;

	@Override
	public String getCommand() 
	{
		return "lookingAt";
	}

	
	@Override
	public void runCommand(CommandBase command, ICommandSender sender,	String[] args) 
	{
		if(sender != null && sender instanceof EntityPlayer)
		{
			if(sender.getEntityWorld().isRemote)
			{
				Type type = Minecraft.getMinecraft().objectMouseOver.typeOfHit;
				
				double d0 = (double)Minecraft.getMinecraft().playerController.getBlockReachDistance();
				
				//RayTraceResult hit = sender.getCommandSenderEntity().rayTrace(d0, 1.0f);
				RayTraceResult hit =  Minecraft.getMinecraft().objectMouseOver;
				
				if((hit != null && type != Type.ENTITY) || (hit != null && type == Type.BLOCK))
				{
					BlockPos blockpos = hit.getBlockPos();

					IBlockState block = sender.getEntityWorld().getBlockState(blockpos);
						
					if(block != null)
					{
						//IBlockState block = sender.getEntityWorld().getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
						
						ModContainer modContainer = FMLCommonHandler.instance().findContainerFor(block);
						ResourceLocation registryName = block.getBlock().getRegistryName(); 
						Class clazz = block.getBlock().getClass();
						
						List<Class> extendedClasses = Tools.getAllSuperclasses(clazz);
						
						String clipboardstring = block.getBlock().getRegistryName() + (block.getBlock().getMetaFromState(block) != 0 ? ":"+ block.getBlock().getMetaFromState(block) : "" + ConfigHandler.NEW_LINE
								+ "Class: "+ block.getBlock().getClass().getCanonicalName()) + ConfigHandler.NEW_LINE
								+ "Extended Classes: [" + ConfigHandler.NEW_LINE;
						for(Class extendClass : extendedClasses)
						{
							clipboardstring += extendClass.getCanonicalName() + ConfigHandler.NEW_LINE;
						}
						clipboardstring += "]" + ConfigHandler.NEW_LINE;

						
						String formattedString = TextFormatting.ITALIC.YELLOW 
									 +"Registry Name: "+ TextFormatting.RESET + registryName + "\n" + TextFormatting.ITALIC.YELLOW 
									 +"  Meta: "+ TextFormatting.RESET + block.getBlock().getMetaFromState(block) + "\n"  + TextFormatting.ITALIC.YELLOW
									 +"  ModID: "+ TextFormatting.RESET + (modContainer == null ? "minecraft" : modContainer.getModId()) + "\n" + TextFormatting.ITALIC.YELLOW
									 +"  Class: "+ TextFormatting.RESET + clazz.getCanonicalName() + "\n" + TextFormatting.ITALIC.YELLOW
									 +"  Extends: "+ TextFormatting.RESET + extendedClasses.size() +"x Classes (on clipboard)";

						Tools.CopytoClipbard(clipboardstring);

						sender.addChatMessage(new TextComponentTranslation(formattedString));
					}
				}
//				else if(type == Type.BLOCK)
//				{
//					IBlockState block = sender.getEntityWorld().getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
//					
//					ModContainer modContainer = FMLCommonHandler.instance().findContainerFor(block);
//					ResourceLocation registryName = block.getBlock().getRegistryName(); 
//					Class clazz = block.getBlock().getClass();
//					
//					List<Class> extendedClasses = Tools.getAllSuperclasses(clazz);
//					
//					String clipboardstring = block.getBlock().getRegistryName() + (block.getBlock().getMetaFromState(block) != 0 ? ":"+ block.getBlock().getMetaFromState(block) : "" + ConfigHandler.NEW_LINE
//							+ "Class: "+ block.getBlock().getClass().getCanonicalName()) + ConfigHandler.NEW_LINE
//							+ "Extended Classes: [";
//					for(Class extendClass : extendedClasses)
//					{
//						clipboardstring += extendClass.getCanonicalName() + ConfigHandler.NEW_LINE;
//					}
//					clipboardstring += "]" + ConfigHandler.NEW_LINE;
//
//					
//					String formattedString = TextFormatting.ITALIC.YELLOW 
//								 +"Registry Name: "+ TextFormatting.RESET + registryName + TextFormatting.ITALIC.YELLOW + ConfigHandler.NEW_LINE
//								 +"  Meta: "+ TextFormatting.RESET + block.getBlock().getMetaFromState(block) + TextFormatting.ITALIC.YELLOW + ConfigHandler.NEW_LINE 
//								 +"  ModID: "+ TextFormatting.RESET + (modContainer == null ? "minecraft" : modContainer.getModId()) + TextFormatting.ITALIC.YELLOW + ConfigHandler.NEW_LINE
//								 +"  Class: "+ TextFormatting.RESET + clazz.getCanonicalName() + TextFormatting.ITALIC.YELLOW + ConfigHandler.NEW_LINE
//								 +"  Extends: "+ TextFormatting.RESET + extendedClasses.size() +"x Classes (on clipboard)";
//
//					Tools.CopytoClipbard(clipboardstring);
//
//					sender.addChatMessage(new TextComponentTranslation(formattedString));
//				}
				
				else if(type == Type.ENTITY)
				{
					Entity lookingAt = Minecraft.getMinecraft().objectMouseOver.entityHit;
					ModContainer modContainer = FMLCommonHandler.instance().findContainerFor(lookingAt);
					String EntityString = EntityList.getEntityString(lookingAt); 
					Class clazz = lookingAt.getClass();
			
					 
					sender.addChatMessage(new TextComponentTranslation(TextFormatting.ITALIC.YELLOW +"Localized Name: " + TextFormatting.RESET + lookingAt.getDisplayName().getFormattedText()));
					sender.addChatMessage(new TextComponentTranslation(TextFormatting.ITALIC.YELLOW +"StringID: "  + TextFormatting.RESET + EntityString + TextFormatting.YELLOW +" IntID: "  + TextFormatting.RESET + EntityList.getEntityID(lookingAt)));
					sender.addChatMessage(new TextComponentTranslation(TextFormatting.ITALIC.YELLOW +"ModID: " + TextFormatting.RESET + (modContainer == null ? "minecraft" : modContainer.getModId())));
					sender.addChatMessage(new TextComponentTranslation(TextFormatting.ITALIC.YELLOW +"Class: " + TextFormatting.RESET + clazz.getCanonicalName()));

					String clipboardstring = "Localized Name: "+ lookingAt.getDisplayName().getUnformattedText() + ConfigHandler.NEW_LINE
							+" StringID: "+ EntityString + ConfigHandler.NEW_LINE
							+" IntID: "+ EntityList.getEntityID(lookingAt) + ConfigHandler.NEW_LINE
							+" ModID: "+ (modContainer == null ? "minecraft" : modContainer.getModId()) + ConfigHandler.NEW_LINE
							+" Class: "+ lookingAt.getClass().getCanonicalName() + ConfigHandler.NEW_LINE
							+" ExtendedClasses: [ "+ ConfigHandler.NEW_LINE;
					
					List<Class> extendedClasses = Tools.getAllSuperclasses(clazz);
					
					for(Class extendClass : extendedClasses)
					{
						clipboardstring += extendClass.getCanonicalName() + ConfigHandler.NEW_LINE;
					}
					clipboardstring += "]" + ConfigHandler.NEW_LINE;
					
					sender.addChatMessage(new TextComponentTranslation(TextFormatting.YELLOW +"Extends: "+ TextFormatting.RESET + extendedClasses.size() +"x Classes (on clipboard)"));

					Tools.CopytoClipbard(clipboardstring);
				}
			}
			
		}
	}

}
