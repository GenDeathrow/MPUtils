package com.gendeathrow.mpbasic.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Nullable;

import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;
import com.gendeathrow.mpbasic.core.MPBasic;
import com.gendeathrow.mputils.utils.JsonHandler;
import com.gendeathrow.mputils.utils.MPFileUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class InfoPanelPages {

	protected String panelId;
	protected int currentPage = 0;
	protected File file;
	boolean canChangePages = true;
	boolean isLoaded = false;
	protected ArrayList<PageProperties> pages = new ArrayList<PageProperties>();
	protected boolean mustRead = false;
	
	public InfoPanelPages(String name, File fileIn) {
		file = fileIn;
		panelId = name;
		readJson();
	}
	
	public String getPanelID() {
		return this.panelId;
	}
	
	public boolean canSwitchPages() {
		return this.canChangePages;
	}
	
	public boolean forceRead() {
		return mustRead;
	}
	
	public int getPageCnt() {
		return pages.size();
	}
	
	@Nullable
	public File getCurrentPageFile() {
		if(!Valadate())
			return null;
		
		return pages.get(currentPage).pageFile;
	}
	
	@Nullable
	public PageProperties getCurrentPageProperty() {
		if(!Valadate())
			return null;
		
		return pages.get(currentPage);
	}
	
	public boolean Valadate() {
		return currentPage < pages.size(); 
	}
	
	public String readPage() {
		try {
			return pages.get(currentPage).readPage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "Error reading file";
	}
	
	public void nextPage() {
		if(hasNextPage())
			currentPage++;
	}
	
	public boolean hasNextPage() {
		return (currentPage + 1)  < (pages.size());
	}
	
	public void prevPage() {
		if(hasPrevPage())
			currentPage--;
	}
	
	public boolean hasPrevPage() {
		return (currentPage - 1)  >= 0;
	}
	
	public void readJson() {
		
		pages.clear();
		
		JsonObject json = JsonHandler.ReadJsonFile(file);
		
		if(json.has(InfoPanelConfigHandler.NBTONLOGIN)) {
			if(json.get(InfoPanelConfigHandler.NBTONLOGIN).getAsBoolean())
				InfoPanelConfigHandler.setOnLoginPage(this);
		}
		
		if(json.has(InfoPanelConfigHandler.NBTCANCHANGEPAGES))
			canChangePages = json.get(InfoPanelConfigHandler.NBTCANCHANGEPAGES).getAsBoolean();
		
		if(json.has(InfoPanelConfigHandler.NBTMUSTREAD))
			mustRead = json.get(InfoPanelConfigHandler.NBTMUSTREAD).getAsBoolean();
		
		JsonArray pageArray = json.get(InfoPanelConfigHandler.NBTPAGES).getAsJsonArray();
		
		for(JsonElement pagejson : pageArray) {
			
			PageProperties pageProperty = createPageProperty(((JsonObject)pagejson));

			if(pageProperty == null) continue;
			
			pages.add(pageProperty);
		}
		
	}
	
	/**
	 * Creates a new Page Property from a json Object read from a file.
	 * 
	 * @param jsonIn
	 * @return
	 */
	public PageProperties createPageProperty(JsonObject jsonIn) {
		
		int id = -1;
		String titleIn = "";
		File fileIn; 
		boolean mustreadIn = false;
		int nextPageID = -1;
		
		if(jsonIn.has(InfoPanelConfigHandler.NBTTITLE))
			titleIn = jsonIn.get(InfoPanelConfigHandler.NBTTITLE).getAsString();
		
		if(jsonIn.has(InfoPanelConfigHandler.NBTFILE)) {
			String textFile = jsonIn.get(InfoPanelConfigHandler.NBTFILE).getAsString();
			fileIn = new File(file.getParent(), textFile + ".txt");
			
			if(!fileIn.exists()) {
				MPBasic.logger.error("Failed to create Page : File '"+new File(file.getParent(), textFile).getPath()+"' for page "+ id +" in "+ file);
				return null;
			}
		}
		else {
			MPBasic.logger.error("Failed to create Page : Missing file name for page "+ id +" in "+ file);
			return null;
		}
		
		if(jsonIn.has(InfoPanelConfigHandler.NBTMUSTREAD))
			mustreadIn = jsonIn.get(InfoPanelConfigHandler.NBTMUSTREAD).getAsBoolean();
			
		return new PageProperties(id, titleIn, fileIn, mustreadIn, nextPageID);
	}
	

	
	public static class PageProperties{
		private int pageID;
		private String title;
		private File pageFile;
		
		/** Has to scroll down before moving on. **/
		private boolean mustRead = false;
		
		/** Moves on to next page on click. (Used if canChangePages false)**/
		private int nextPage = 0;
		
		public PageProperties(int idIn, String titleIn, File fileIn, boolean mustReadIn, int nextPageID){
			
			pageID = idIn;
			title = titleIn;
			pageFile = fileIn;
			mustRead = mustReadIn;
			nextPage = nextPageID;
		}
		
		public int getPageID() {
			return pageID;
		}
		
		public String getTitle() {
			return title;
		}
		
		public boolean mustPlayerRead() {
			return mustRead;
		}
		
		public String readPage() throws IOException {
			return MPFileUtils.readFile(pageFile);
		}
	}
	
	public class CustomPageButton {
		
		public  CustomPageButton() {
			
		}
	}
	
}
