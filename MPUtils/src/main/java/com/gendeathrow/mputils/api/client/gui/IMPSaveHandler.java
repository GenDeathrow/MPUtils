package com.gendeathrow.mputils.api.client.gui;

import net.minecraft.nbt.NBTTagCompound;

public interface IMPSaveHandler 
{

	
	public abstract void SaveNBT(NBTTagCompound tag);
	
	public abstract NBTTagCompound ReadNBT(NBTTagCompound tag);
}
