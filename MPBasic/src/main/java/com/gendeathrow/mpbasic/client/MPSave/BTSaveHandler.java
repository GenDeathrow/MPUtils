package com.gendeathrow.mpbasic.client.MPSave;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import com.gendeathrow.mputils.api.client.gui.IMPSaveHandler;
import com.gendeathrow.mputils.client.settings.MPUtils_SaveHandler;
import com.gendeathrow.mputils.utils.MPInfo;

public class BTSaveHandler implements IMPSaveHandler
{
	
	public static String savedVersion;
	
	public static void register()
	{
		MPUtils_SaveHandler.RegisterExtraData(new BTSaveHandler());
	}


	@Override
	public NBTTagCompound ReadNBT(NBTTagCompound tag) 
	{
		
		if(tag.hasKey("BasicTools"))
		{
			NBTTagCompound basicTools = (NBTTagCompound) tag.getTag("BasicTools");
				savedVersion = basicTools.getString("lastVer");
		}
		else savedVersion = "0.0.0";

		return tag;
	}

	@Override
	public void SaveNBT(NBTTagCompound tag) 
	{
		NBTTagCompound basicTools = new NBTTagCompound();
		
			basicTools.setString("lastVer", MPInfo.version);
				
		tag.setTag("BasicTools", basicTools);
	}

}
