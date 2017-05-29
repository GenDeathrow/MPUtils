package com.gendeathrow.mputils.commands.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import com.gendeathrow.mputils.utils.Tools;

public class MP_ModItemDump extends MP_ItemDump
{
	ArrayList<String> modnames = new ArrayList<String>(); 

	
	public MP_ModItemDump()
	{
		super();
		
			modnames.clear();
		for (ModContainer modcontainer : Loader.instance().getModList())
		{				
			modnames.add(modcontainer.getModId());
		}
	}
	
	@Override
	public String getCommand() 
	{
		return "moddump";
	}

	@Override
	public String getUsageSuffix()
	{
		//return " [Args:(nbt, ore, <>, |string|)]";
		return "modid "+ super.getUsageSuffix();
	}
	
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		if(args.length == 2)
			return modnames;
		else if(args.length >= 2) 
			return options; 
		else return new ArrayList<String>();
	}
	
	@Override
	public void runCommand(CommandBase command, ICommandSender sender, String[] args) 
	{
		if(sender != null && sender instanceof EntityPlayer)
		{
			if(sender.getEntityWorld().isRemote)
			{
				String clipboard = "";
				
				String modid = args[1];
				
				
				System.out.println(modid +"---------------");
				EntityPlayer player = (EntityPlayer) sender;
				
				int total = Block.REGISTRY.getKeys().size() + Item.REGISTRY.getKeys().size();
				int cnt = 0;
				this.isSilent = true;
//				for(Block block : Block.REGISTRY)
//				{
//					cnt++;
//					sendUpdate(sender, cnt, total);
//					
//					//System.out.println(Block.REGISTRY.getNameForObject(block).getResourceDomain());
//					if(Block.REGISTRY.getNameForObject(block).getResourceDomain() != modid)
//						continue;
//					
//					ItemStack stack = new ItemStack(block);
//					NonNullList<ItemStack> list = NonNullList.create();
//					block.getSubBlocks(stack.getItem(),(CreativeTabs)null,list);
//
//					sendUpdate(sender, cnt, total);
//					if(stack == null)
//					{
//						continue;
//					}
//					if(list.size() > 0)
//					{
//						clipboard += this.parseStackData(sender, args, stack);
//						for(ItemStack itemstack : list)
//							clipboard += this.parseStackData(sender, args, itemstack);
//					}	
//					else
//						clipboard += this.parseStackData(sender, args, stack);
//				}
				
				
				
				for(Item item : Item.REGISTRY)
				{
					cnt++;
					sendUpdate(sender, cnt, total);
					
					if(!Item.REGISTRY.getNameForObject(item).getResourceDomain().equals(modid.trim().toLowerCase()))
						continue;
					
					ItemStack stack = new ItemStack(item);
					
					NonNullList<ItemStack> list = NonNullList.create();
					item.getSubItems(stack.getItem(),(CreativeTabs)null,list);
					
					if(stack == null)
					{
						continue;
					}
					
					if(list.size() > 0)
					{
						//clipboard += this.parseStackData(sender, args, stack);
						for(ItemStack itemstack : list)
							clipboard += this.parseStackData(sender, args, itemstack);
					}	
					else
						clipboard += this.parseStackData(sender, args, stack);
				}
				this.isSilent = false;
				if(Tools.CopytoClipbard(clipboard))
				{
					player.sendMessage(new TextComponentTranslation(TextFormatting.YELLOW +" --Copied to Clipboard--"));
				}
			}
		}
		
	}
	
	
	int lastCheck = 0;
	private void sendUpdate(ICommandSender sender, int cnt, int max)
	{
		int perc = (int)((cnt * 100.0f) / max);
		if(perc % 10 == 0 && perc != lastCheck)
		{
			lastCheck = perc;
			sender.sendMessage(new TextComponentString(perc+"%"));
		}
	}
}
