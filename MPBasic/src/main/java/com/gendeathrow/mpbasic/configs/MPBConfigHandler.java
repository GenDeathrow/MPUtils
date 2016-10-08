package com.gendeathrow.mpbasic.configs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;

import com.gendeathrow.mpbasic.core.MPBSettings;
import com.gendeathrow.mpbasic.core.MPBasic;
import com.gendeathrow.mputils.configs.ConfigHandler;

public class MPBConfigHandler 
{
		public static final File configFile = new File(ConfigHandler.configDir +"/addons/"+MPBasic.MODID, MPBasic.MODID+".cfg");
		
		public static Configuration config;
		
	    public static final String ConfigVer = "1.0.0";
	    
	    private final static String tabMenuCat = "menu_tabs";
	    
	    public static final String NEW_LINE;

	    static
	    {
	        NEW_LINE = System.getProperty("line.separator");
	    }
	    
		public static void load()
		{
			config = new Configuration(configFile, ConfigVer);
			config.load();	
			
			loadConfiguration();
			
			generateChangeLog();
		}
		
		private final static String changelogCat = "changeLog_settings";
		
		public static void loadConfiguration()
		{
			MPBSettings.showChangeLogButton = config.get(tabMenuCat, "Show Changelog", true, "Adds Changelog button to menu").getBoolean();
			MPBSettings.showBugReporter = config.get(tabMenuCat, "Show Bug Report", true, "Adds Bug Report button to menu. Soon will have a Reporting Form").getBoolean();;
			MPBSettings.showSupport = config.get(tabMenuCat, "Show Support", true, "Adds Support you button to menu").getBoolean();;
				
			MPBSettings.changeLogTitle = config.getString("ChangeLog Title", changelogCat, "What's New!", "Change button and Title of change log");
			MPBSettings.isHttp = config.getBoolean("isHttp", changelogCat, false, "If change log file is located on a web url");
			MPBSettings.url = config.getString("File Address", changelogCat, "changelog.txt", "Location of the file http or filename");
					
			MPBSettings.bugURL = config.getString("Issue tracker url", Configuration.CATEGORY_GENERAL, "http://minecraft.curseforge.com/projects/mputils/issues", "http of your Issue Tracker");
			MPBSettings.supportURL = config.getString("Support url", Configuration.CATEGORY_GENERAL, "https://www.patreon.com/GenDeathrow", "http of your SupportPage");

			MPBSettings.showUpdateNotification = config.getBoolean("Update Notification", Configuration.CATEGORY_GENERAL, true, "Shows an update Notification dropdown from top center of screen. Only happens when versions change.");
			config.save();
		}
		
		/**
		 * Creates Default ChangeLogs
		 */
		private static void generateChangeLog()
		{
	        try 
	        {	
	        	File file = new File(ConfigHandler.configDir, "/changelog.txt");
			
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
//	                FileOutputStream fos = new FileOutputStream(file);
//	                BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));

	            	FileWriter fos = new FileWriter(file);
	                BufferedWriter buffer = new BufferedWriter(fos);
	                
	                buffer.write(TextFormatting.YELLOW +"# Add Your ChangeLog to "+TextFormatting.RED+"'changelog.txt'"+TextFormatting.RESET+" in the config files" + NEW_LINE + NEW_LINE + "You can use Minecrafts "+TextFormatting.BLUE+"Color "+TextFormatting.DARK_GREEN+"Codes "+TextFormatting.GOLD+"to"+TextFormatting.RESET+" "+TextFormatting.UNDERLINE+"make your changlogs look Good.");

	                
	                
	                buffer.close();
	                fos.close();
	            }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
}
