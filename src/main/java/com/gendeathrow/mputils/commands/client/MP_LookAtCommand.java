package com.gendeathrow.mputils.commands.client;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.core.MPUtils;

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
				//Entity lookingAt = Minecraft.getMinecraft().objectMouseOver.entityHit;	
				Type type = Minecraft.getMinecraft().objectMouseOver.typeOfHit;
				
				if(type == Type.BLOCK)
				{
					IBlockState block = sender.getEntityWorld().getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
					
					String string = "Looking at: "+ TextFormatting.ITALIC.YELLOW + block.getBlock().getRegistryName() + TextFormatting.RESET +"  Meta: "+ TextFormatting.ITALIC.YELLOW + block.getBlock().getMetaFromState(block) + TextFormatting.RESET;
					
					
					try
					{
						StringSelection selection = new StringSelection(block.getBlock().getRegistryName()+ ":" + block.getBlock().getMetaFromState(block));
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
						string += TextFormatting.YELLOW +" --Copied to Clipboard--";
					}catch(Exception e)
					{
						MPUtils.logger.log(Level.ERROR, e);
					}
					
					sender.addChatMessage(new TextComponentTranslation(string));
				}
				
				if(type == Type.ENTITY)
				{
					Entity lookingAt = Minecraft.getMinecraft().objectMouseOver.entityHit; 
					sender.addChatMessage(new TextComponentTranslation("Looking at: "+ EntityList.getEntityString(lookingAt) +" ID: "+ EntityList.getEntityID(lookingAt)));
				}
			}
			
		}
	}

}
