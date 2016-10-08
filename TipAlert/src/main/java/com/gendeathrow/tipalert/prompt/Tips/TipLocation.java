package com.gendeathrow.tipalert.prompt.Tips;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.EntityList;
import net.minecraft.util.math.BlockPos;

import com.gendeathrow.tipalert.core.TipAlert;
import com.gendeathrow.tipalert.prompt.TipList.TipType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TipLocation extends Tip
{
	private BlockPos posMin;
	private BlockPos posMax;
	
	public TipLocation(String statIdIn, String nameIn, TipType typeIn) 
	{
		super(statIdIn, nameIn, typeIn);
	}
	
	public TipLocation(String statIdIn, String nameIn, TipType typeIn, String desc) 
	{
		super(statIdIn, nameIn, typeIn);
		this.setInfo(statIdIn, desc);
	}
	
	/**
	 * 
	 * [sx,sy,sz,ex,ey,ez]
	 *   '*' wildcard 
	 *   
	 * 
	 * 
	 * 
	 */
	public Tip ReadFromJson(JsonObject json)
    {
    	this.setDesc(json.get("desc").getAsString());
    	
    	if(json.has("longdesc")) this.longDesc = json.get("longdesc").getAsString();
    	else this.longDesc = this.getDescription();
    	
    	if(json.has("minPos"))
    	{
    		JsonArray minpos = json.get("minPos").getAsJsonArray();
    		
    		parseLocation(minpos);
    		
    				
    				posMin = new BlockPos(zLevel, zLevel, zLevel);
    	}

    	return this;
    }
	
	
	public int parseLocation(JsonArray list)
	{
		int x = parseInt(list.get(0));
		int y = parseInt(list.get(1));
		int z = parseInt(list.get(2));
		
				
		return 0;
	}
	
	private int parseInt(JsonElement jsonElement)
	{
		int pos = 0;
		
		String element = jsonElement.getAsString();
		
		if(element == "*" ||element == "-1")
		{
			return 0;
		}
		
		return jsonElement.getAsInt(); 
	}
	
	
}
