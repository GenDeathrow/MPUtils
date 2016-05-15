package com.gendeathrow.mputils.configs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.prompt.TipList;
import com.gendeathrow.mputils.prompt.TipList.TipType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ConfigHandler 
{
	
	public static final File configDir = new File("config/"+ MPUtils.MODID);
	public static final File configFile = new File(configDir, MPUtils.MODID+".cfg");
	
	public static Configuration config;
	
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String NEW_LINE;
    
    public static final String ConfigVer = "1.0.1";

    static
    {
        NEW_LINE = System.getProperty("line.separator");
    }

	public static void load()
	{
		config = new Configuration(configFile, ConfigVer);
		
		//config.load();
		
		//preLoad(); 
		
		loadConfiguration();
		
		generateChangeLog();

		loadTips();
		
		
		
		
	}
	
	public static void loadTips()
	{
		TipList.ReadJsonTips(ReadJsonFile(generateDefaultTips()));
		
		MPUtils.tipHandler.ReadNBTFile();
	}
	
	
	private static void preLoad()
	{

		//System.out.println(config.getLoadedConfigVersion()+" != "+ config.getDefinedConfigVersion());
		
		
		if(config.getLoadedConfigVersion() != config.getDefinedConfigVersion())
		{
			loadLegacyConfig(config.getLoadedConfigVersion());
			MPUtils.logger.log(Level.INFO, "Config is ");
			configFile.delete();
		}
		
	}

	private final static String tabMenuCat = "menu_tabs";
	private final static String changelogCat = "changeLog_settings";
	
	private static void loadConfiguration()
	{
		config.load();
		
		Settings.showTips = config.getBoolean("Show Tip Notification", Configuration.CATEGORY_CLIENT, Settings.showTips, "Turns tip notification on/off");
		
		// Menu buttons
		Settings.tipsMenuButton = config.get(tabMenuCat, "Tips Tab", Settings.tipsMenuButton, "Adds 'Tips!' button to menu").getBoolean();

		Settings.showChangeLogButton = config.get(tabMenuCat, "ChangeLogs Tab", Settings.showChangeLogButton, "Adds Changelog button to menu").getBoolean();
			
			Settings.changeLogTitle = config.getString("ChangeLog Title", changelogCat, Settings.changeLogTitle, "Change button and Title of change log");
			Settings.isHttp = config.getBoolean("isHttp", changelogCat, Settings.isHttp, "If change log file is located on a web url");
			Settings.url = config.getString("File Address", changelogCat, Settings.url, "Location of the file http or filename");
			
			 //ishttp
			// url
		
		
		//Settings.showSupportButton = config.get(tabMenuCat, "Support Tab", Settings.showSupportButton, "Adds a Support you button to menu, Also ").getBoolean();

		config.save();
	}
	
	
	private static void loadLegacyConfig(String version)
	{
		if(version == "1.0.0")
		{
			Settings.tipsMenuButton = config.get(Configuration.CATEGORY_GENERAL, "Tips Tab", Settings.tipsMenuButton, "Adds 'Tips!' button to menu").getBoolean();
			Settings.showChangeLogButton = config.get(Configuration.CATEGORY_GENERAL, "ChangeLogs Tab", Settings.showChangeLogButton, "Adds 'Whats New!' button to menu").getBoolean();
		}
	}
	
	
	/**
	 * Creates Default ChangeLogs
	 */
	private static void generateChangeLog()
	{
        try 
        {	
        	File file = new File(configDir, "/changelog.txt");
		
        	if (file.getParentFile() != null)
        	{
        		file.getParentFile().mkdirs();
        	}


			if (file.exists())
			{
			    return;
			}
			else
			{
				file.createNewFile();
			}
			
            if (file.canWrite())
            {
                FileOutputStream fos = new FileOutputStream(file);
                BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, DEFAULT_ENCODING));

                buffer.write("# Add Your ChangeLog to 'changelog.txt' in the config files" + NEW_LINE + NEW_LINE + "You can use Minecrafts Color Codes to make your changlogs look Good.");

                
                
                buffer.close();
                fos.close();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a Default Tip
	 */
	public static File generateDefaultTips()
	{
        try 
        {	
        	File file = new File(configDir, "tips.json");
        	
        	if (file.exists())
        	{
        		return file;
        	}else file.createNewFile();
			
        	if (file.canWrite())
        	{
        		FileWriter fw = new FileWriter(file);
        			
        		JsonArray defaultjson = new JsonArray();
        		
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
	
	private static JsonObject ReadJsonFile(File f) 
	{
		try
		{
			FileReader fr = new FileReader(f);
			JsonObject json = new Gson().fromJson(fr, JsonObject.class);
			fr.close();
			return json;
		} catch(Exception e)
		{
			MPUtils.logger.log(Level.ERROR, "An error occured while loading JSON from file:", e);
			return new JsonObject(); // Just a safety measure against NPEs
		}
	}
	
}
