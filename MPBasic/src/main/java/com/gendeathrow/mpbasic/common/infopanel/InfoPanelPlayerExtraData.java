package com.gendeathrow.mpbasic.common.infopanel;

import java.util.HashSet;

import com.gendeathrow.mpbasic.api.IInfoPanelData;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

public class InfoPanelPlayerExtraData implements IInfoPanelData{
	
	public HashSet<String> booksGiven = new HashSet<String>();
	
	public InfoPanelPlayerExtraData(){}
	
	public InfoPanelPlayerExtraData(Object object) {}

	public void addBookPanel(String panelID) {
		if(!booksGiven.contains(panelID))
			booksGiven.add(panelID);
	}
	
	public void addBookPanel(InfoPanelPages panel) {
		if(!booksGiven.contains(panel.getPanelID()))
			booksGiven.add(panel.getPanelID());
	}
	
	@Override
	public boolean hasBeenGivinBook(String panelID) {
		return booksGiven.contains(panelID);
	}

	@Override
	public void ReadNBT(NBTTagCompound tag) {
		
		NBTTagList taglist = tag.getTagList("booksGiven", NBT.TAG_STRING);
		
		for(int i = 0; i < taglist.tagCount(); i++) {
			booksGiven.add(taglist.getStringTagAt(i));
		}
	}

	@Override
	public NBTTagCompound WriteNBT(NBTTagCompound tag) {
		
		NBTTagList seenNbt = new NBTTagList();
		
		for(String pageID : booksGiven)
			seenNbt.appendTag(new NBTTagString(pageID));
		
		if(seenNbt.tagCount() > 0)
			tag.setTag("booksGiven", seenNbt);
		
		return tag;
	}





}
