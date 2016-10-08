package com.gendeathrow.tipalert.prompt.Tips;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

import com.gendeathrow.tipalert.core.TipAlert;
import com.gendeathrow.tipalert.prompt.TipList;
import com.gendeathrow.tipalert.prompt.TipList.TipType;
import com.google.gson.JsonObject;

public class Tip extends TipBase
{
	public String longDesc = "";
	
	public Tip(String statIdIn, String nameIn, TipType typeIn) 
	{
		super(statIdIn, nameIn, typeIn);
	}
	
	public Tip(String statIdIn, String nameIn, TipType typeIn, String desc) 
	{
		super(statIdIn, nameIn, typeIn);
		this.setInfo(statIdIn, desc);
	}

	public String getFullDescription() 
	{
		return longDesc;
	}
	
	public Tip ReadFromJson(JsonObject json)
    {
    	this.setDesc(json.get("desc").getAsString());
    	
    	if(json.has("longdesc")) this.longDesc = json.get("longdesc").getAsString();
    	else this.longDesc = this.getDescription();
    	
    	int metaID = json.has("meta") ? json.get("meta").getAsInt() : 0;
    	
    	this.useOreDic = json.has("useOreDictonary") ? json.get("useOreDictonary").getAsBoolean() : false;
    	
    	if(useOreDic && json.has("customOreDictonary")) this.oreDicName = json.get("customOreDictonary").getAsString();
    	
    	if(json.has("item"))
    	{
    		Item item = Item.getByNameOrId(json.get("item").getAsString());
    		
    		if(item == null) 
    		{
    			TipAlert.logger.log(Level.ERROR, "Cant find Item "+ (json.get("block").getAsString()) +" , Not Loading this Tip: "+ this.getTitle());
    			return null;
    		}
    		
    				//.getByNameOrId(json.get("item").getAsString());
   			this.theItemStack = new ItemStack(item);
   			this.theItemStack.setItemDamage(metaID);
    	}
    	else if(json.has("block"))
    	{
    		Block block = Block.getBlockFromName(json.get("block").getAsString());
    		
    		if(block == null) 
    		{
    			TipAlert.logger.log(Level.ERROR, "Cant find Block "+ (json.get("block").getAsString()) +" , Not Loading this Tip: "+ this.getTitle());
    			return null;
    		}
    		
    		this.theItemStack = new ItemStack(Item.getItemFromBlock(block));
    		this.theItemStack.setItemDamage(metaID);
    	}
    	
    	if(this.theItemStack != null) this.setTextOffset(22);
    	
    	return this;
    }
}
