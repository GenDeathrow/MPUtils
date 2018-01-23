package com.gendeathrow.mpbasic.client.MPSave;

import com.gendeathrow.mputils.api.client.gui.IMPSaveHandler;
import com.gendeathrow.mputils.client.settings.MPUtils_SaveHandler;
import com.gendeathrow.mputils.utils.MPInfo;

import net.minecraft.nbt.NBTTagCompound;

public class BTSaveHandler implements IMPSaveHandler
{
	
	public static String savedVersion = "0.0.0";
	
	public static String hasSeenStartInfoPanel = null;
	
	public static void register()
	{
		MPUtils_SaveHandler.RegisterExtraData(new BTSaveHandler());
	}
	
	public static void setSeenInfoPanel(String value)
	{
		hasSeenStartInfoPanel = value;
		MPUtils_SaveHandler.saveSettings();
	}
	
	public String infoNBTTag = "hasSeenStartInfoPanel";
	public String handlerNBTTag = "BasicTools";

	@Override
	public NBTTagCompound ReadNBT(NBTTagCompound tag) 
	{
		
		if(tag.hasKey(handlerNBTTag))
		{
			NBTTagCompound basicTools = (NBTTagCompound) tag.getTag(handlerNBTTag);
			
				savedVersion = basicTools.getString("lastVer");
				
				if(basicTools.hasKey(infoNBTTag)) {
					hasSeenStartInfoPanel= basicTools.getString(infoNBTTag);
				}
		}
		
		return tag;
	}

	@Override
	public void SaveNBT(NBTTagCompound tag) 
	{
		NBTTagCompound basicTools = new NBTTagCompound();
		
			basicTools.setString("lastVer", MPInfo.version);
			
			if(hasSeenStartInfoPanel != null)
				basicTools.setString(infoNBTTag, hasSeenStartInfoPanel);
				
		tag.setTag(handlerNBTTag, basicTools);
	}

}
