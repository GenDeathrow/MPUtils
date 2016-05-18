package com.gendeathrow.mputils.prompt;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.prompt.TipList.TipType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Tip extends TipBase
{
	
	public String desc = "null";
	
	public String longDesc = "";
	
	public Tip(String statIdIn, String string, TipType typeIn) 
	{
		super(statIdIn, string, typeIn);
	}
	
	public Tip(String statIdIn, String string, TipType typeIn, String desc) 
	{
		super(statIdIn, string, typeIn);
	}

	public String getDescription() 
	{
		return desc;
	}

	public String getFullDescription() 
	{
		return desc;
	}
	
	public Tip ReadFromJson(JsonObject json)
    {
    	this.desc = json.get("desc").getAsString();
    	
    	if(json.has("longdesc")) this.longDesc = json.get("longdesc").getAsString();
    	else this.longDesc = this.desc;
    	
    	int metaID = json.has("meta") ? json.get("meta").getAsInt() : 0;
    	
    	this.useOreDic = json.has("useOreDictonary") ? json.get("useOreDictonary").getAsBoolean() : false;
    	
    	if(useOreDic && json.has("customOreDictonary")) this.oreDicName = json.get("customOreDictonary").getAsString();
    	
    	if(json.has("item"))
    	{
    		Item item = Item.getByNameOrId(json.get("item").getAsString());
    		
    		
    		// COLOR FORMATING CODE \u00A7
    		if(item == null) 
    		{
    			MPUtils.logger.log(Level.ERROR, "Cant find Item "+ (json.get("block").getAsString()) +" , Not Loading this Tip: "+ this.getStatName());
    			return null;
    		}
    		
   			this.theItemStack = new ItemStack(item);
   			this.theItemStack.setItemDamage(metaID);
   			

    	}
    	else if(json.has("block"))
    	{
    		Block block = Block.getBlockFromName(json.get("block").getAsString());
    		
    		if(block == null) 
    		{
    			MPUtils.logger.log(Level.ERROR, "Cant find Block "+ (json.get("block").getAsString()) +" , Not Loading this Tip: "+ this.getStatName());
    			return null;
    		}
    		
    		this.theItemStack = new ItemStack(Item.getItemFromBlock(block));
    		this.theItemStack.setItemDamage(metaID);
    	}
    	
    	if(json.has("blockImg"))
    	{
    		Block block = Block.getBlockFromName(json.get("block").getAsString());
    		
    		if(block == null) 
    		{
    			MPUtils.logger.log(Level.ERROR, "Cant find Block "+ (json.get("block").getAsString()) +" , Not Loading this Tip: "+ this.getStatName());
    			return null;
    		}
    		
    		this.theItemStack = new ItemStack(Item.getItemFromBlock(block));
    		this.theItemStack.setItemDamage(metaID);
    	}
    	
    	return this;
    }
}
