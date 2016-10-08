package com.gendeathrow.mputils.commands.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
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
				
				RayTraceResult hit = sender.getCommandSenderEntity().rayTrace(d0, 1.0f);
				if(hit != null)
				{
					BlockPos blockpos = hit.getBlockPos();


					IBlockState block = sender.getEntityWorld().getBlockState(blockpos);
						
					if(block != null)
					{
						int blockMeta2 = block.getBlock().getMetaFromState(block);
	
						String string2 = "Looking at: "+ block.getBlock().getRegistryName();
					
						if(blockMeta2 != 0)  string2 += "  Meta: "+ blockMeta2;
				
						Tools.CopytoClipbard(string2);
						sender.addChatMessage(new TextComponentTranslation(string2));
					}
				}
				else if(type == Type.BLOCK)
				{
					IBlockState block = sender.getEntityWorld().getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
					
					String string = "Looking at: "+ TextFormatting.ITALIC.YELLOW + block.getBlock().getRegistryName() + TextFormatting.RESET +"  Meta: "+ TextFormatting.ITALIC.YELLOW + block.getBlock().getMetaFromState(block) + TextFormatting.RESET;
					
					Tools.CopytoClipbard(block.getBlock().getRegistryName()+ (block.getBlock().getMetaFromState(block) != 0 ? ":"+ block.getBlock().getMetaFromState(block) : ""));

					
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
