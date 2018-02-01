package com.gendeathrow.mpbasic.api;

import com.gendeathrow.mpbasic.common.infopanel.InfoPanelPages;

import net.minecraft.nbt.NBTTagCompound;

public interface IInfoPanelData {

	public abstract void addBookPanel(InfoPanelPages panel);
	
	public abstract void addBookPanel(String panelID);
	
	public abstract boolean hasBeenGivinBook(String panelID);
	
	public abstract void ReadNBT(NBTTagCompound tag);
	public abstract NBTTagCompound WriteNBT(NBTTagCompound tag);
}
