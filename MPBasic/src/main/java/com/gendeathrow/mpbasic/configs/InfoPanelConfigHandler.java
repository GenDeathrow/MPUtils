package com.gendeathrow.mpbasic.configs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.gendeathrow.mpbasic.common.infopanel.InfoPanelPages;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.MPFileUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class InfoPanelConfigHandler {
	
	public static InfoPanelConfigHandler INSTANCE = new InfoPanelConfigHandler();
	
	public static InfoPanelPages onLogInLoadInfoPage = null;
	
	public static HashMap<String, InfoPanelPages> PAGES = new HashMap<String, InfoPanelPages>();

	public static File infoDirectory = new File(ConfigHandler.configDir, "infopanel");
	
	
	public static boolean hasOnLoginPage() {
		return onLogInLoadInfoPage != null;
	}
	
	public static void setOnLoginPage(InfoPanelPages page) {
		if(onLogInLoadInfoPage == null)
			onLogInLoadInfoPage = page;
	}
	
	public static void readInfoPanelConfigs() {
	
		if(!infoDirectory.exists()) CreateDirectory();
		
		for( File object : infoDirectory.listFiles()) {
			
			if(object.isDirectory()) {
				File[] files = object.listFiles(new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        return name.toLowerCase().endsWith(".json");
				    }
				});
				
				for(File file : files) {
					String name = file.getName().split("\\.")[0];
					PAGES.put(name, new InfoPanelPages(name, file));
				}
					
			}
		}
	}
	
	protected static void CreateDirectory() {
			infoDirectory.mkdirs();
			generateExamples();
	}
	
	
	public static void giveBook(EntityPlayer player,String panelID) {
		if(PAGES.containsKey(panelID))
			player.addItemStackToInventory(createBook(panelID, PAGES.get(panelID)));
	}
	
	public static void giveBook(EntityPlayer player,String panelID, InfoPanelPages panel) {
		player.addItemStackToInventory(createBook(panelID, panel));
	}
	
	public static void giveAllBooks(EntityPlayer player) {
		for(Entry<String, InfoPanelPages> pageEntry : PAGES.entrySet()) {
			giveBook(player, pageEntry.getKey());
		}
	}
	
	public static ItemStack createBook(String infoPanelID, InfoPanelPages panel) {
		ItemStack book = new ItemStack(Items.BOOK);
		NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("infopanel", infoPanelID);
		book.setTagCompound(nbt);
		if(panel != null && panel.getCurrentPageProperty() != null)
			book.setStackDisplayName(panel.getCurrentPageProperty().getTitle());
		else
			book.setStackDisplayName(infoPanelID);
		
		return book;
	}
	
	/**
	 * Creates Default ChangeLogs
	 */
	private static void generateExamples()
	{
		File exampleFolder = new File(infoDirectory, "example");
		
		if (exampleFolder.getParentFile() != null) {
			exampleFolder.getParentFile().mkdirs();
		}

		List<String> p1lines = new ArrayList<String>();
	
		
		p1lines.add("Info panels allow you to write tutorials, welcome messages, or any type of information you would like to add into the game.");p1lines.add("");
		p1lines.add("If you just want it to start up on first world load. add "+ TextFormatting.YELLOW +"'loadOnLogin:true'"+ TextFormatting.RESET +" to your infopanel json file.");p1lines.add("");
		p1lines.add("You can also use minecraft books to open the gui. All you have to do is create a minecraft book with nbt data. "+TextFormatting.BLUE+" '{infopanel:<json file name>}' ");p1lines.add(TextFormatting.YELLOW + "~~Command Example (For welcome.json example):"+TextFormatting.BLUE+" /give <player name> minecraft:book 1 0 {infopanel:welcome}"+ TextFormatting.RESET); p1lines.add("");
		p1lines.add("A quicker command to give the command sender a book for a specfic infopanel. '/mpadmin infopanel <playername> <giveBook/opengui> <json file name>', Auto complete is set up to help you out."); p1lines.add("");
		p1lines.add("If you have load on login true, And you want it to retrigger with a new modpack update, just change the json file name.");p1lines.add("");
		p1lines.add(TextFormatting.RED +"Files are read in UTF-8 Encoding! So if you get strange characters. Your file may be the incorrect encoding. Using a tool like Notpad++ will tell your encoding in the bottom right corner. And in a dropdown you can convert the text file. Some editing my be needed. "+ TextFormatting.RESET);
		p1lines.add("						  "+TextFormatting.BOLD +""+ TextFormatting.BLUE +"Click Next for more info on infopanels...");
			
		List<String> p2lines = new ArrayList<String>();
		p2lines.add("When Creating your first infopanel goto your "+ TextFormatting.YELLOW +"'configs/mputils/infopanel'"+ TextFormatting.RESET +"");
		p2lines.add("Create a folder with anyname you want. What ever you name your folder wont matter, its just for you. Now since your folder is created.");
		p2lines.add("You will create your controller json file.Your controller json file can be called anything you want.");
		p2lines.add("Make sure your json has a unique name as it is how you will reference it in-game.");p2lines.add("");
		p2lines.add("Inside the controller json you can link all your text files. You can have 1 page ,or a lot of pages.");p2lines.add("");
		p2lines.add("Just add all your text files into this folder. You are not limited to just 1 controller json in each folder. ");p2lines.add("");
		p2lines.add("Check the configs for this example to get an idea how to set your new info panels up.");p2lines.add("");p2lines.add("");
		p2lines.add("						     "+ TextFormatting.RED +"Please keep all names lowercase!"+ TextFormatting.RESET);

		List<String> p3lines = new ArrayList<String>();
		p3lines.add("Look page 3!");
		
		p3lines.add("You can use Minecrafts "+TextFormatting.BLUE+"Color "+TextFormatting.DARK_GREEN+"Codes "+TextFormatting.GOLD+"to"+TextFormatting.RESET+" "+TextFormatting.UNDERLINE+"make your InfoPanels look Good.");
		p3lines.add("Setting the 'mustRead' in your json file will force the player to read the infopanel for at least 20 secs. Unless its was opened from a book... This will change to forcing player to read something new only once. At the moment it is what it is. ");
		List<String> jsonLines = new ArrayList<String>();
		jsonLines.add(dumpJson());
			
		try {
			MPFileUtils.CreateTextFile(new File(exampleFolder, "page1.txt"), p1lines);
			MPFileUtils.CreateTextFile(new File(exampleFolder, "page2.txt"), p2lines);
			MPFileUtils.CreateTextFile(new File(exampleFolder, "page3.txt"), p3lines);
			MPFileUtils.CreateTextFile(new File(exampleFolder, "welcome.json"), jsonLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String dumpJson() {
		
		JsonObject json = new JsonObject();
		
		json.addProperty(NBTONLOGIN, true);
		json.addProperty(NBTMUSTREAD, true);
		
		JsonArray pageArray = new JsonArray();
		
			JsonObject page1 = new JsonObject();
				page1.addProperty(NBTTITLE, "Getting Started");
				page1.addProperty(NBTFILE, "page1");
			
			JsonObject page2 = new JsonObject();
				page2.addProperty(NBTTITLE, "Extra Pages");
				page2.addProperty(NBTFILE, "page2");			
			
			JsonObject page3 = new JsonObject();
				page3.addProperty(NBTTITLE, "Welcome Page 3");
				page3.addProperty(NBTFILE, "page3");
				
		pageArray.add(page1);
		pageArray.add(page2);
		pageArray.add(page3);
		
		json.add(NBTPAGES, pageArray);
		
		return new GsonBuilder().setPrettyPrinting().create().toJson(json);
	}
	
	
	
	public static String NBTTITLE = "title";
	public static String NBTGIVEBOOK = "";
	public static String NBTFILE = "file";
	public static String NBTPAGES = "pages";
	public static String NBTMUSTREAD = "mustRead";
	public static String NBTONLOGIN = "loadOnLogin";
	public static String NBTCANCHANGEPAGES = "canChangePages";



	
}
