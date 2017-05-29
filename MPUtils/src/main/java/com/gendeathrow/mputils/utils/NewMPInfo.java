package com.gendeathrow.mputils.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.command.ForgeCommand;

import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class NewMPInfo extends DummyModContainer
{
    
	@Instance("modpack")
	private static NewMPInfo INSTANCE;

	public static String name = "name";
	public static String description = "desciption";
	public static String version = "0.0.0";
	public static String url = "";
	public static List<String> authorList = new ArrayList<String>();
	public static String credits = "";
	public static String logoFile = "";
    
    public static NewMPInfo getInstance()
    {
        return INSTANCE;
    }

    public NewMPInfo()
    {
        super(new ModMetadata());
        
        LoadMPInfo();
        
System.out.println("TETSTSTETS");
        
        ModMetadata meta = getMetadata();
        meta.modId       = "mputils_container";
        meta.name        = name;
        meta.version     = version;
        meta.credits     = credits;
        meta.authorList  = authorList;
        meta.description = description;
        meta.url         = url;
        meta.screenshots = new String[0];
        meta.logoFile    = logoFile;

        //        try {
//            updateJSONUrl    = new URL("http://files.minecraftforge.net/maven/net/minecraftforge/forge/promotions_slim.json");
//        } catch (MalformedURLException e) {}

        INSTANCE = this;
        

    }
    
    
    
	public static void LoadMPInfo()
	{

		if(!Settings.useMPInfo) return;
		
        Gson gson = new Gson();
        
		JsonObject mpinfo = JsonHandler.ReadJsonFile(getMPInfoFile());
		
		name = mpinfo.get("name").getAsString();
		description = mpinfo.get("description").getAsString();
		version = mpinfo.get("version").getAsString();
		url = mpinfo.get("url").getAsString();
		//logoFile = mpinfo.get("logoFile").getAsString().length() > 0 ? ConfigHandler.configDir +"/"+ mpinfo.get("logoFile").getAsString() : null;
		
		credits = mpinfo.get("credits").getAsString();

		JsonArray array = mpinfo.get("authorList").getAsJsonArray();
		authorList = new ArrayList<String>();
		for(JsonElement author : mpinfo.get("authorList").getAsJsonArray())
		{ authorList.add(author.getAsString()); }
	}
	
	private static File getMPInfoFile()
	{
        try 
        {	
        	File file = new File(ConfigHandler.configDir, "mpinfo.info");
        	
        	if (file.exists())
        	{
        		return file;
        	}else file.createNewFile();
			
        	if (file.canWrite())
        	{
        		FileWriter fw = new FileWriter(file);
        			
        		JsonObject json = new JsonObject();
       		
        		json.addProperty("name", "Example Name");
        		json.addProperty("description", "Example Description");
        		json.addProperty("version", "0.0.0");
        		json.addProperty("url", "");
        		//json.addProperty("logoFile", "");
        		json.add("authorList", new JsonArray());
        		json.addProperty("credits", "");
        		
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
	
	public static void SaveMPInfo()
	{
		JsonObject json = new JsonObject();
		
		JsonArray array = new JsonArray();
		for(String author : MPInfo.authorList)
			{ array.add(new JsonPrimitive(author)); }
		 
		 //Type type = new TypeToken<List<String[]>>() {}.getType();
		json.addProperty("name", MPInfo.name);
		json.addProperty("description", MPInfo.description);
		json.addProperty("version", MPInfo.version);
		json.addProperty("url", MPInfo.url);
		//json.addProperty("logoFile", "");
		json.add("authorList", array);
		json.addProperty("credits", MPInfo.credits);
		
		
		if(JsonHandler.WriteJsonFile(new File(ConfigHandler.configDir, "mpinfo.info"), json))
		{
			System.out.println("Saved File");
		}
		
	}
	
	
    
	public static boolean isActive()
	{
		return Settings.useMPInfo;
	}
	
	
	public static int[] getFormatedMPVer(String vernum)
	{
		int[] version = new int[]{0,0,0};
		
		try
		{
			String[] versionRaw = vernum.split("\\.");
			for(int i=0; i < 3; i++)
			{
				if(i < versionRaw.length)
				{
					try
					{
						version[i] = Integer.valueOf(versionRaw[i]);
					}catch(NumberFormatException e)
					{
						MPUtils.logger.warn("A NumberFormatException occured while checking version!\n", e);
						version[i] = 0;
					}
				}
				else version[i] = 0;  
			}
			
		} catch(IndexOutOfBoundsException e)
		{
			MPUtils.logger.warn("An IndexOutOfBoundsException occured while checking version! Make sure all your Version Numbers are formated as (MajorVersion.MinorVersion.RevesionVersion = 1.2.0) And Contain no special characters or text.", e);
		}catch(NullPointerException e)
		{
			MPUtils.logger.warn("An NullPointerException occured while checking version! Make sure all your Version Numbers are formated as (MajorVersion.MinorVersion.RevesionVersion = 1.2.0) And Contain no special characters or text.", e);
		}
		
		return version; 
	}
    
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void modConstruction(FMLConstructionEvent evt)
    {

    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt)
    {

    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent evt)
    {

    }

    @Subscribe
    public void onAvailable(FMLLoadCompleteEvent evt)
    {

    }

    @Subscribe
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new ForgeCommand());
    }
}
