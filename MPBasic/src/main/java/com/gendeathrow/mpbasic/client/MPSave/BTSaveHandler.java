package com.gendeathrow.mpbasic.client.MPSave;

import com.gendeathrow.mputils.api.client.gui.IMPSaveHandler;
import com.gendeathrow.mputils.client.settings.MPUtils_SaveHandler;
import com.gendeathrow.mputils.utils.MPInfo;

import net.minecraft.nbt.NBTTagCompound;

public class BTSaveHandler implements IMPSaveHandler
{
	
	public static String savedVersion = "0.0.0";

	
	public static void register()
	{
		MPUtils_SaveHandler.RegisterExtraData(new BTSaveHandler());
	}
	
	public String handlerNBTTag = "BasicTools";

	@Override
	public NBTTagCompound ReadNBT(NBTTagCompound tag) {
		
		if(tag.hasKey(handlerNBTTag)) {
			NBTTagCompound basicTools = (NBTTagCompound) tag.getTag(handlerNBTTag);
				savedVersion = basicTools.getString("lastVer");

		}
		
		return tag;
	}

	@Override
	public void SaveNBT(NBTTagCompound tag) {
		NBTTagCompound basicTools = new NBTTagCompound();
			basicTools.setString("lastVer", MPInfo.version);
		tag.setTag(handlerNBTTag, basicTools);
		
	}

}
