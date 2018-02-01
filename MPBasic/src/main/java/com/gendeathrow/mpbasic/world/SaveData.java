package com.gendeathrow.mpbasic.world;

import java.util.ArrayList;
import java.util.HashMap;

import com.gendeathrow.mpbasic.common.infopanel.InfoPanelPages;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class SaveData extends WorldSavedData{
	  
	public static String name = "mpbasic";
	public static boolean IS_GLOBAL = true;
	
	public ArrayList<String> seenPanels = new ArrayList<String>();
	
	public HashMap<EntityPlayer, String> seenPanels2 = new HashMap<EntityPlayer, String>();
	
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
		System.out.println(panel.getPanelID());
		this.setDirty(true);
	}
	
	public void clearPanels() {
		seenPanels.clear();
	}

	public static SaveData get(World world) {

		MapStorage storage = IS_GLOBAL ? world.getMapStorage() : world.getPerWorldStorage();
		  SaveData instance = (SaveData) storage.getOrLoadData(SaveData.class, name);
System.out.println("geworldt");
		  if (instance == null) {
			  System.out.println("null world");
		    instance = new SaveData(name);
		    storage.setData(name, instance);
		  }
		  return instance;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		//clearPanels();
		System.out.println("read");	
		if(nbt.hasKey("seenPanels")) {
			NBTTagList taglist = nbt.getTagList("seenPanels", NBT.TAG_STRING);
			
			for(int i = 0; i < taglist.tagCount(); i++) {
				System.out.println(">> "+taglist.getStringTagAt(i));	
				seenPanels.add(taglist.getStringTagAt(i));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		System.out.println("save ");
		NBTTagList seenNbt = new NBTTagList();
		
		for(String pageID : seenPanels)
			seenNbt.appendTag(new NBTTagString(pageID));
		
		if(seenNbt.tagCount() > 0)
			compound.setTag("seenPanels", seenNbt);
		
		return compound;
	}



}
