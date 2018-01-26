package com.gendeathrow.mpbasic.world;

import java.util.ArrayList;

import com.gendeathrow.mpbasic.client.InfoPanelPages;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class SaveData extends WorldSavedData{
	  
	public static String name = "_mpbasic";
	public static boolean IS_GLOBAL = true;
	
	public static ArrayList<String> seenPanels = new ArrayList<String>();
	
	
	public SaveData(String in) {
		super(name);
	}
	
	
	/**
	 * If player has seen this page before. 
	 * 
	 * @param panel
	 * @return
	 */
	public boolean hasSeenPanel(InfoPanelPages panel) {
		return seenPanels.contains(panel.getPanelID());
		
	}
	
	/**
	 * Add seen page 
	 * 
	 * @param panel
	 * @return
	 */
	public void addSeenPanel(InfoPanelPages panel) {
		if(!seenPanels.contains(panel.getPanelID()))
			seenPanels.add(panel.getPanelID());
		
		this.setDirty(true);
	}

	public static SaveData get(World world) {

		MapStorage storage = IS_GLOBAL ? world.getMapStorage() : world.getPerWorldStorage();
		  SaveData instance = (SaveData) storage.getOrLoadData(SaveData.class, name);

		  if (instance == null) {
		    instance = new SaveData(name);
		    storage.setData(name, instance);
		  }
		  return instance;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		seenPanels.clear();
	
		if(nbt.hasKey("seenPanels")) {
			NBTTagList taglist = nbt.getTagList("seenPanels", 8);
			
			for(int i = 0; i < taglist.tagCount(); i++) {
				seenPanels.add(taglist.getStringTagAt(i));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		NBTTagList seenNbt = new NBTTagList();
		
		for(String pageID : seenPanels)
			seenNbt.appendTag(new NBTTagString(pageID));
		
		if(seenNbt.tagCount() > 0)
			compound.setTag("seenPanels", seenNbt);
		
		return compound;
	}



}
