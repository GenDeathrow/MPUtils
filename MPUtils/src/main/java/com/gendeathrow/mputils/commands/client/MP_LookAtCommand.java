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

public class MP_LookAtCommand extends MP_LookAtDump
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
						this.parseStackData(sender, args,sender.getEntityWorld(),  block, blockpos);
				}
				else if(type == Type.ENTITY)
				{
					Entity lookingAt = Minecraft.getMinecraft().objectMouseOver.entityHit;
					this.parseStackData(sender, args, lookingAt);
				}
			}
			
		}
	}

}
