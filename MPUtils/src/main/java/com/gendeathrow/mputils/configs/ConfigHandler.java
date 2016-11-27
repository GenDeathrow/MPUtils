package com.gendeathrow.mputils.configs;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.gendeathrow.mputils.utils.MPInfo;

public class ConfigHandler 
{
	
	public static final File configDir = new File("config/"+ MPUtils.MODID);
	public static final File configFile = new File(configDir, MPUtils.MODID+".cfg");
	public static final File addonDir = new File(ConfigHandler.configDir +"/addons/");
	
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

		//preLoad(); 
	
		loadConfiguration();
		
		MPInfo.LoadMPInfo();
	}
	
	@SuppressWarnings("unused")
	private static void preLoad()
	{

		if(config.getLoadedConfigVersion() != config.getDefinedConfigVersion())
		{
			loadLegacyConfig(config.getLoadedConfigVersion());
			MPUtils.logger.log(Level.INFO, "Config is ");
			configFile.delete();
		}
		
	}

	

	private final static String tabMenuCat = "menu_tabs";
	private final static String changelogCat = "changeLog_settings";
	
	public static void loadConfiguration()
	{
		config.load();	
			//config.getBoolean("Edit Mode", Configuration.CATEGORY_GENERAL, true, "Not used just yet");
			
			Settings.useMPInfo = config.getBoolean("Use MPInfo", config.CATEGORY_GENERAL, false, "Set to true, Will create a \"mpinfo.info\" file to input modpack data");
			
			Settings.editMode = config.getBoolean("Edit Mode", config.CATEGORY_GENERAL, false, "Edit mode opens up a set of new tools to use with MPUtils and its addons.");

		config.save();
	}
	
	
	private static void loadLegacyConfig(String version)
	{
	}
	
	


}
