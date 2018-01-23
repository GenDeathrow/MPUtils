package com.gendeathrow.mputils.commands.client;

import com.gendeathrow.mputils.utils.Tools;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class MP_Hotbar extends MP_ItemDump
{

	@Override
	public String getCommand() 
	{
		return "hotbar";
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
				
				for(int slot = 0; slot < 9; slot++)
				{
					ItemStack stack = player.inventory.mainInventory.get(slot);
					
					if(stack == null)
					{
						continue;
					}
					
					clipboard += this.parseStackData(sender, args, stack);
					
					//clipboard += stack.getItem().getRegistryName() + ":"+ stack.getMetadata() + ConfigHandler.NEW_LINE;
				}
				
				if(Tools.CopytoClipbard(clipboard))
				{
					player.sendMessage(new TextComponentTranslation(TextFormatting.YELLOW +" --Copied to Clipboard--"));
				}
			}
		}
		
	}

}
