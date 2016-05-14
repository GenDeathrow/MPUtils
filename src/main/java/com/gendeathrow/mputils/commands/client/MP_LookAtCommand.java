package com.gendeathrow.mputils.commands.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;

public class MP_LookAtCommand extends MP_ClientBaseCommand
{

	public static IBlockState currentBlock;
	
//	@Override
//	public String getCommand() 
//	{
//		return "lookingAt";
//	}
//
//	@Override
//	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
//	{
//		System.out.println("runCommand");
//		if(!sender.getEntityWorld().isRemote) return;
//		System.out.println("Remote");
//		
//		if(sender instanceof EntityPlayer)
//		{
//			EntityPlayer player = (EntityPlayer)sender;
//			
//			if(currentBlock == null) 
//			{
//				player.addChatMessage(new TextComponentTranslation("No block found."));
//			}
//			else 
//			{
//				player.addChatMessage(new TextComponentTranslation("Looking at: "+ Block.getIdFromBlock(currentBlock.getBlock())));
//				currentBlock = null;
//			}
//	
//			
//		}
//	}


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
					
					sender.addChatMessage(new TextComponentTranslation("Looking at: "+ block.getBlock().getRegistryName()  +"  Meta:"+ block.getBlock().getMetaFromState(block)));
				}
				
				if(type == Type.ENTITY)
				{
					Entity lookingAt = Minecraft.getMinecraft().objectMouseOver.entityHit; 
					sender.addChatMessage(new TextComponentTranslation("Looking at: "+ EntityList.getEntityString(lookingAt) +" ID:"+ EntityList.getEntityID(lookingAt)));
				}
			}
			
		}
	}

}
