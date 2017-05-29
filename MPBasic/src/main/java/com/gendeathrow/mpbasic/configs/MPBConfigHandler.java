package com.gendeathrow.mpbasic.configs;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.ConfigCategory;
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
		private final static String issueTrackerCat = "Issue_Tracker_settings";
		
		public static void loadConfiguration()
		{
			MPBSettings.showChangeLogButton = config.get(tabMenuCat, "Show Changelog", true, "Adds Changelog button to menu").getBoolean();
			MPBSettings.showBugReporter = config.get(tabMenuCat, "Show Bug Report", true, "Adds Bug Report button to menu. Soon will have a Reporting Form").getBoolean();;
			MPBSettings.showSupport = config.get(tabMenuCat, "Show Support", true, "Adds Support you button to menu").getBoolean();;
			
			config.addCustomCategoryComment(changelogCat, "For change log settings visit: "+ NEW_LINE +" https://minecraft.curseforge.com/projects/mputils-basic-tools/pages/mputils-basic-tools/change-log");
			MPBSettings.changeLogTitle = config.getString("ChangeLog Title", changelogCat, "What's New!", "Change button and Title of change log");
			MPBSettings.isHttp = config.getBoolean("isHttp", changelogCat, false, "If change log file is located on a web url");
			MPBSettings.url = config.getString("File Address", changelogCat, "changelog.txt", "Location of the file http or filename");
					
			
			MPBSettings.supportURL = config.getString("Support url", Configuration.CATEGORY_GENERAL, "https://www.patreon.com/GenDeathrow", "http of your SupportPage");

			MPBSettings.showUpdateNotification = config.getBoolean("Update Notification", Configuration.CATEGORY_GENERAL, true, "Shows an update Notification dropdown from top center of screen. Only happens when versions change.");

			
			
			// issuetracker url
			// issuetracker boolean useIngameForm
			// issuetracker default crashelogs to create gist. 
			// issuetracker send json data (automated issue report)
//			
//			boolean removeBugURL = false;
//			
//			ConfigCategory remove = new ConfigCategory("remove");
//			if(config.hasKey(Configuration.CATEGORY_GENERAL, "Issue tracker url"))
//			{
//				MPBSettings.bugURL = config.getString("Issue tracker url", Configuration.CATEGORY_GENERAL, "http://minecraft.curseforge.com/projects/mputils/issues", "http of your Issue Tracker");
//				
//				config.moveProperty(Configuration.CATEGORY_GENERAL, "Issue tracker url", remove.getName());
//				
//				config.removeCategory(remove);
//				removeBugURL = true;	
//			}
//			
			
			
			MPBSettings.issuetrackerURL = config.getString("Issue Tracker url", issueTrackerCat, "https://github.com/GenDeathrow/MPUtils/issues", "http of your IssueTracker");
			MPBSettings.useInGameForm = config.getBoolean("Use in game form", issueTrackerCat, false, "Use an in game form, If false the issue tracker button will just link to the tracker url.");
			MPBSettings.crashlogsToGist = config.getBoolean("Crashlogs to Gist default", issueTrackerCat, true, "This just changes default setting to create a Gist like for change log. May break if git hub changes api.");
			MPBSettings.sendJsonData = config.getBoolean("Use Automated issue tracker", issueTrackerCat, false, "This is an Advance Settings, The web address must accept Json data to be able to automate issue reporting."+ NEW_LINE +" It is up to you how you accept the json data on your website. "+ NEW_LINE +" A simple database Example with Google Sheets & Scripts is here: "+ NEW_LINE +" https://minecraft.curseforge.com/projects/mputils-basic-tools/pages/issue-tracker");
			
			config.renameProperty(issueTrackerCat, "Collect Emails", "Collect Contact Info");
			
			MPBSettings.collectContact = config.getBoolean("Collect Contact Info", issueTrackerCat, true, "If 'useInGameForm' && 'sendJsonData' = true, than ask users for thier contact info.");
			
			MPBSettings.useDisclaimer = config.getBoolean("Use Disclaimer", issueTrackerCat, true, "This adds a check box if user should accept your Issue trackers to send data from thier computer");
			MPBSettings.disclaimerFile = config.getString("Disclaimer File", issueTrackerCat, "config/mputils/addons/mpbasic/disclaimer.txt", "Sets your disclaimer file. Suggested that you use this.");
			MPBSettings.contactTypes = config.getStringList("Contact Info Types", issueTrackerCat,new String[] {"Email", "Curse", "GitHub"}, "Types of contact info you would like to collect from ppl.");		
			MPBSettings.issueTypes = config.getStringList("Issue Types", issueTrackerCat,new String[] {"Bug", "Game Crash", "Config Issue", "Mod Conflict"}, "Types of Issues you would like to catogrize");
			
			
			if(MPBSettings.useDisclaimer) 
			{
				File disclamerfile = new File(MPBSettings.disclaimerFile);
				if(!disclamerfile.exists())
					try 
					{
						disclamerfile.createNewFile();
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
			}

			
			
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
