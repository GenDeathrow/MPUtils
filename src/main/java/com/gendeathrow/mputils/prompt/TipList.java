package com.gendeathrow.mputils.prompt;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gendeathrow.mputils.core.MPUtils;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TipList 
{
	public static final List<Tip> TIPS = Lists.<Tip>newArrayList();
	
	public static final HashMap<String, Tip> idToTips = new HashMap<String, Tip>();

	public static HashMap<ItemStack, Tip> blockLookTipList = new HashMap<ItemStack, Tip>();
	
	public static HashMap<Entity, Tip> entityLookTipList = new HashMap<Entity, Tip>();
	
	public static HashMap<ItemStack, Tip> toolTipList = new HashMap<ItemStack, Tip>();
	
	public static HashMap<Integer, TipDimension> dimensionList = new HashMap<Integer, TipDimension>();
	
	public static void clearTips()
	{
		idToTips.clear();
		blockLookTipList.clear();
		entityLookTipList.clear();
		toolTipList.clear();
		dimensionList.clear();
	}
	
	public static void ReadJsonTips(JsonObject jsonObject) 
	{
		clearTips();
		
		if(jsonObject.has(TipType.DIMENSION.toString()))
		{
			for(JsonElement element : jsonObject.get(TipType.DIMENSION.toString()).getAsJsonArray())
			{
				JsonObject data = element.getAsJsonObject();
				TipDimension tip = new TipDimension(data.get("tipID").getAsString(), data.get("tipName").getAsString()).ReadFromJson(data);
				
				if (tip == null) continue;
			    TIPS.add(tip);
			    idToTips.put(tip.tipId, tip);
			    dimensionList.put(data.get("dimid").getAsInt(), tip);
			}
			MPUtils.logger.log(Level.INFO, "Dimension Tips Loaded: "+ dimensionList.size());
		}
		
		if(jsonObject.has(TipType.BLOCK_LOOKING_AT.toString()))
		{
			for(JsonElement element : jsonObject.get(TipType.BLOCK_LOOKING_AT.toString()).getAsJsonArray())
			{
				JsonObject data = element.getAsJsonObject();
				Tip tip = new Tip(data.get("tipID").getAsString(), data.get("tipName").getAsString(), TipType.BLOCK_LOOKING_AT).ReadFromJson(data);
				
				if (tip == null) continue;
			    TIPS.add(tip);
			    idToTips.put(tip.tipId, tip);
			    blockLookTipList.put(tip.getItemStack(), tip);
			}
			
			MPUtils.logger.log(Level.INFO, "Block Tips Loaded: "+ blockLookTipList.size());
		}
		
		if(jsonObject.has(TipType.TOOLTIP.toString()))
		{
			for(JsonElement element : jsonObject.get(TipType.TOOLTIP.toString()).getAsJsonArray())
			{
				JsonObject data = element.getAsJsonObject();
				Tip tip = new Tip(data.get("tipID").getAsString(), data.get("tipName").getAsString(), TipType.TOOLTIP).ReadFromJson(data);
				
				if (tip == null) continue;
			    TIPS.add(tip);
			    idToTips.put(tip.tipId, tip);
			    toolTipList.put(tip.getItemStack(), tip);
			}
			MPUtils.logger.log(Level.INFO, "ToolTips Loaded: "+ toolTipList.size());
		}
		
		if(jsonObject.has(TipType.INFO.toString()))
		{
			for(JsonElement element : jsonObject.get(TipType.INFO.toString()).getAsJsonArray())
			{
				JsonObject data = element.getAsJsonObject();
				Tip tip = new Tip(data.get("tipID").getAsString(), data.get("tipName").getAsString(), TipType.INFO).ReadFromJson(data);
				
				if (tip == null) continue;
			    TIPS.add(tip);
			    idToTips.put(tip.tipId, tip);
			}
			MPUtils.logger.log(Level.INFO, "General Tips Loaded: "+ (TIPS.size() - (blockLookTipList.size() + toolTipList.size() + dimensionList.size())) );
		}
		
		
		MPUtils.logger.log(Level.INFO, "Total Tips Loaded: "+ TIPS.size());

		
//		for(JsonElement element : jsonArray)
//		{
//			JsonObject data = element.getAsJsonObject();
//			Tip tip = new Tip(data.get("tipID").getAsString(), data.get("tipName").getAsString(), TipType.getType(data.get("type").getAsString())).ReadFromJson(data);
//			if (tip == null) return;
//		    TIPS.add(tip);
//		    idToTips.put(tip.tipId, tip);
//		    
//		    if(tip.type == TipType.BLOCK_LOOKINGAT) blockLookTipList.put(tip.getItemStack(), tip);
//		    else if(tip.type == TipType.TOOLTIP) toolTipList.put(tip.getItemStack(), tip);
//		    else if(tip.type == TipType.DIMENSION) dimensionList.put(tip.dimId, tip);
//		}
	}
	
	public boolean isBlock(Tip tip)
	{
		if(Block.getBlockFromItem(tip.getItemStack().getItem()) != null)
		{
			return true;
		}
		
		return false;
	}

	public boolean checkTips(TipType type,Item item)
	{
		return false;
	}
	
	@SuppressWarnings("unused")
	public static Tip checkTipLookingAt(IBlockState block)
	{
		
		ItemStack stack = new ItemStack(block.getBlock());
		
		if(stack == null) return null;
		
		for(Entry<ItemStack, Tip> tip : blockLookTipList.entrySet())
		{
			
			boolean flag = checkOreDiconary(tip.getValue(), stack);
			
			Item item = tip.getValue().getItemStack().getItem();		
			IBlockState tipBlock = Block.getBlockFromItem(item).getDefaultState();
		
			
			if(tipBlock != null && (tipBlock.equals(block) || flag))
			{
				if(MPUtils.tipHandler.tipNotification.isInQueue(tip.getValue())) return null;
				return tip.getValue();
			}

		}
		return null;
	}
	
	public static Tip checkToolTip(ItemStack stack)
	{

		for(Entry<ItemStack, Tip> tip : toolTipList.entrySet())
		{
			
			boolean flag = checkOreDiconary(tip.getValue(), stack);
			
			Item item = tip.getValue().getItemStack().getItem();		
			
			if(stack.getItem() != null && (stack.getItem().equals(item) || flag))
			{
				return tip.getValue();
			}

		}
		return null;
	}
	
	
	public static boolean checkOreDiconary(Tip tip, ItemStack stack)
	{
	
		if(tip.useOreDic)
		{
			boolean flag = false;
			
			if(tip.getItemStack().getItem() == null || stack.getItem() == null) return false;
			
			int[] tipIdList = OreDictionary.getOreIDs(tip.getItemStack());
			
			int[] blockIdList = OreDictionary.getOreIDs(stack);

			int customID = OreDictionary.getOreID(tip.oreDicName);
			
			//System.out.println(OreDictionary.containsMatch(false, Lists.<ItemStack>newArrayList(tip.getItemStack()), stack));
			
			for(int id : tipIdList)
			{
				if(flag) break;
				
				if(id == customID) { return true; }
			
				for(int check : blockIdList)
				{
					if(id == check) 
					{
						return true;
					}
				
				}
			}
		}
		return false;
	}
	
	public static Tip getAndCheckTips(TipType type, IBlockState block)
	{
		for(Tip tip : TIPS)
		{
			if(tip.type == type)
			{
			
				Item item = tip.getItemStack().getItem();
				IBlockState tipBlock = Block.getBlockFromItem(item).getDefaultState();
				 
				
				boolean flag = checkOreDiconary(tip, new ItemStack(block.getBlock()));
	
				
				if(tipBlock != null && (tipBlock.equals(block) || flag))
				{
					if(MPUtils.tipHandler.tipNotification.isInQueue(tip)) return null;
					return tip;
				}
			}
		}
		
		return null;
	}
 	
 	public enum TipType
	{
		TOOLTIP,
		BLOCK_LOOKING_AT,
		ENTITY_LOOKING_AT,
		DIMENSION,
		INFO;
		
		public static TipType getType(String type)
		{
			if(type == null) return TipType.INFO;
			
			for(TipType tip : TipType.values())
			{
				if(tip.toString().toLowerCase().equals(type.toLowerCase()))
				{
					return tip;
				}

			}
			
			return TipType.INFO;
		}
	}

	public static Tip checkdimension(int dimension) 
	{
		if(dimensionList.containsKey(dimension))
		{
			return dimensionList.get(dimension);
		}
		return null;
	}
}
