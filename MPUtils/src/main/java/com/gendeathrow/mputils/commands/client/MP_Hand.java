package com.gendeathrow.mputils.commands.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

import com.gendeathrow.mputils.commands.MP_BaseCommand;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.NBTConverter;
import com.gendeathrow.mputils.utils.Tools;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class MP_Hand extends MP_ItemDump
{
	
	@Override
	public String getCommand() 
	{
		return "hand";
	}
	
	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
	{
		if(sender != null && sender instanceof EntityPlayer)
		{
			if(sender.getEntityWorld().isRemote)
			{
				String clipboard = "";
				
				EntityPlayer player = (EntityPlayer) sender;
				ItemStack stack;
				
				if(player.getHeldItemMainhand() != null)
				{
					stack = player.getHeldItemMainhand();
					
					clipboard += this.parseStackData(sender, args, stack);
				}
				
				if(Tools.CopytoClipbard(clipboard))
				{
					player.addChatMessage(new TextComponentTranslation(TextFormatting.YELLOW +" --Copied to Clipboard--"));
				}
			
			}
		}
		
	}
	


}
