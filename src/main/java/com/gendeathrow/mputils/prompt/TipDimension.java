package com.gendeathrow.mputils.prompt;

import com.gendeathrow.mputils.prompt.TipList.TipType;
import com.google.gson.JsonObject;

public class TipDimension extends Tip
{
	public int dimId;
	
	public TipDimension(String statIdIn, String NameIn) 
	{
		super(statIdIn, NameIn, TipType.DIMENSION);
	}
	
	@Override
	public TipDimension ReadFromJson(JsonObject json)
    {
		super.ReadFromJson(json);
		
		if(json.has("dimid")) dimId = json.get("dimid").getAsInt();

		return this;
    }

}
