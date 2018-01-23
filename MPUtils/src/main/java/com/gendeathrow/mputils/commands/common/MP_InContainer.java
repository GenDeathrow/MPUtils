package com.gendeathrow.mputils.commands.common;

import java.util.ArrayList;

import com.gendeathrow.mputils.commands.client.MP_ItemDump;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.network.RequestTEPacket;
import com.gendeathrow.mputils.utils.Tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MP_InContainer extends MP_ItemDump
{
	public static IBlockState currentBlock;

	
	@Override
	public String getCommand() 
	{
		return "inContainer";
	}

	
	@Override
	public void runCommand(CommandBase command, ICommandSender sender,	String[] args) 
	{
		if(sender != null && sender instanceof EntityPlayer)
		{

			if(sender.getEntityWorld().isRemote)
			{
				String clipboard = "";
				
				Type type = Minecraft.getMinecraft().objectMouseOver.typeOfHit;
				
				double d0 = (double)Minecraft.getMinecraft().playerController.getBlockReachDistance();
				RayTraceResult hit =  Minecraft.getMinecraft().objectMouseOver;
				
				if((hit != null && type != Type.ENTITY) || (hit != null && type == Type.BLOCK))
				{
					BlockPos blockpos = hit.getBlockPos();
					IBlockState block = sender.getEntityWorld().getBlockState(blockpos);
						
					if(block != null)
					{
						TileEntity te = sender.getEntityWorld().getTileEntity(blockpos);
						IItemHandler inventory = getInventory(te, hit.sideHit);
						
						if(te == null || inventory == null) 
						{
							sender.sendMessage(new TextComponentTranslation(TextFormatting.YELLOW +" The block your looking at doesn't appear to have an Inventory"));
							return;
						}else
						{
							MPUtils.network.sendToServer(RequestTEPacket.requestTEItemDump(blockpos, args));
						}
						
						
						
					}

				}
			}
			
		}
	}
	
	
	public static IItemHandler getInventory(TileEntity te, EnumFacing facing)
	{
		IItemHandler inventory = null;
		
		if(te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing))
			inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		else if(te != null && te instanceof IItemHandler)
			inventory = (IItemHandler)te;
		
		return inventory;
	}
	
	
	public static void copyInventoryClipboard(TileEntity te, String[] args)
	{
		String clipboard = "";

		IItemHandler inventory = getInventory(te, EnumFacing.DOWN);
		
		if(inventory == null) return;
		
		for(int i=0; i < inventory.getSlots(); i++){
			if(inventory.getStackInSlot(i).isEmpty()) continue;
			clipboard += MP_ItemDump.parserItemstack(args, inventory.getStackInSlot(i),  new ArrayList<String>());
		}
		
		if(Tools.CopytoClipbard(clipboard))
		{
			Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation(TextFormatting.YELLOW +" --Copied to Clipboard--"));
		}
	}
}
