package com.gendeathrow.tipalert.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraftforge.common.config.Configuration;

import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.JsonHandler;
import com.gendeathrow.tipalert.core.TipAlert;
import com.gendeathrow.tipalert.core.TipSettings;
import com.gendeathrow.tipalert.prompt.TipList;
import com.gendeathrow.tipalert.prompt.TipList.TipType;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TipConfigHandler 
{
	public static final File configFile = new File(ConfigHandler.configDir+"/addons/"+TipAlert.MODID, TipAlert.MODID+".cfg");

	public static Configuration config;
	
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final String ConfigVer = "1.0.0";
	
	public static void load()
	{
		config = new Configuration(configFile, ConfigVer);
		config.load();	
		
		loadConfiguration();
		
		loadTips();
	}
	
    private final static String tabMenuCat = "menu_tabs";
    
	public static void loadConfiguration()
	{
		TipSettings.tipsMenuButton = config.get(tabMenuCat, "Show Tips Tab", true, "Adds a way to browse all loaded tips").getBoolean();
		TipSettings.showTips = config.getBoolean("Show Tip Notification", "client", true, "Turns tip notification on/off");
		
		TipSettings.maxTipCnt = config.getInt("Showing Tip Cnt", "Tip Settings", 2, 1, 10, "This sets how many times a player will see the same tip during gameplay. This is just a default setting.");
				
		config.save();
	}
	
	public static void loadTips() 
	{
		TipList.ReadJsonTips(JsonHandler.ReadJsonFile(generateDefaultTips()));
		
		TipAlert.tipManager.ReadNBTFile();
	}
	
	/**
	 * Creates a Default Tip
	 */
	public static File generateDefaultTips()
	{
        try 
        {	
        	File file = new File(ConfigHandler.configDir+"/addons/"+TipAlert.MODID, "tips.json");
        	
        	if (file.exists())
        	{
        		System.out.println(file.getAbsolutePath());
        		return file;
        	}else file.createNewFile();
			
        	if (file.canWrite())
        	{
        		FileWriter fw = new FileWriter(file);
        			
        		JsonObject json = new JsonObject();
        		
        		JsonArray lookingAt = new JsonArray();
        		
        		JsonArray tooltip = new JsonArray();
        		
        		JsonArray info = new JsonArray();
        		
        		JsonArray dimension = new JsonArray();
        		
        		JsonObject json1 = new JsonObject();
       			
        		json1.addProperty("tipID", "dirt");
        		json1.addProperty("tipName", "Dirt Tip");
        		json1.addProperty("block", "minecraft:dirt");
        		json1.addProperty("desc", "Use Dirt to Grow plants");
//        		json1.addProperty("type", "look");
        		lookingAt.add(json1);
        		
        		json1 = new JsonObject();
        		
        		json1.addProperty("tipID", "grass");
        		json1.addProperty("tipName", "Grass Tip");
        		json1.addProperty("block", "minecraft:grass");
        		json1.addProperty("desc", "Use Grass to feed sheep");
//        		json1.addProperty("type", "look");
        		lookingAt.add(json1);

        		json1 = new JsonObject();
        		
        		json1.addProperty("tipID", "wood");
        		json1.addProperty("tipName", "Wood Tip");
        		json1.addProperty("block", "minecraft:log");
        		json1.addProperty("desc", "Use Wood to make stuff");
        		json1.addProperty("useOreDictonary", true);
//        		json1.addProperty("type", "look");
        		lookingAt.add(json1);
        		json1 = new JsonObject();
        		
        		json1.addProperty("tipID", "redstone");
        		json1.addProperty("tipName", "Redstone ToolTip");
        		json1.addProperty("item", "minecraft:redstone");
        		json1.addProperty("desc", "Use redstone to power \nyour devices");
        		tooltip.add(json1);
        		json1 = new JsonObject();
        		
        		json1.addProperty("tipID", "nether");
        		json1.addProperty("tipName", "Enter the Nether");
        		json1.addProperty("dimid", -1);
        		json1.addProperty("desc", "You have Entered the Nether. \nThis place is very dangerous. Keep armor, a sword, and a bow handy.");
        		dimension.add(json1);
        		json1 = new JsonObject();
        		
        		json1.addProperty("tipID", "info1");
        		json1.addProperty("tipName", "Dont Dig Down");
        		json1.addProperty("desc", "Never dig down as you could fall into lava or a deep cavern!");
        		info.add(json1);
        		json1 = new JsonObject();
        		
        		json.add(TipType.BLOCK_LOOKING_AT.toString(), lookingAt);
        		json.add(TipType.TOOLTIP.toString(), tooltip);
        		json.add(TipType.INFO.toString(), info);
        		json.add(TipType.DIMENSION.toString(), dimension);
//        			
        		new GsonBuilder().setPrettyPrinting().create().toJson(json, fw);
        			
        		fw.flush();
        		fw.close();
        		
                return file;
        	}
			}catch (IOException e) 
        	{
				e.printStackTrace();
        	}
        
        return null;

 	}
	


}
