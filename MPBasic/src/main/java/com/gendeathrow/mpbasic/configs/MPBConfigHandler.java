package com.gendeathrow.mpbasic.configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.gendeathrow.mpbasic.core.MPBSettings;
import com.gendeathrow.mpbasic.core.MPBasic;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.MPFileUtils;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;

public class MPBConfigHandler 
{
		public static final File MPBConfigDir =  new File(ConfigHandler.configDir +"/addons/"+MPBasic.MODID);
		public static final File configFile = new File(MPBConfigDir, MPBasic.MODID+".cfg");
		
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
			
			if(!MPBSettings.isHttp)
				generateChangeLog();
		}
		
		private final static String changelogCat = "changeLog_settings";
		private final static String issueTrackerCat = "Issue_Tracker_settings";
		
		public static void loadConfiguration()
		{
			MPBSettings.showChangeLogButton = config.get(tabMenuCat, "Show Changelog", true, "Adds Changelog button to menu").getBoolean();
			MPBSettings.showBugReporter = config.get(tabMenuCat, "Show Bug Report", true, "Adds Bug Report button to menu. Soon will have a Reporting Form").getBoolean();;
			MPBSettings.showSupport = config.get(tabMenuCat, "Show Support", true, "Adds Support you button to menu").getBoolean();;
				
			MPBSettings.changeLogTitle = config.getString("ChangeLog Title", changelogCat, "What's New!", "Change button and Title of change log");
			MPBSettings.isHttp = config.getBoolean("isHttp", changelogCat, false, "If change log file is located on a web url");
			MPBSettings.url = config.getString("File Address", changelogCat, "changelog.txt", "Location of the file http or filename");
					
			
			MPBSettings.supportURL = config.getString("Support url", Configuration.CATEGORY_GENERAL, "https://www.patreon.com/GenDeathrow", "http of your SupportPage");

			MPBSettings.showUpdateNotification = config.getBoolean("Update Notification", Configuration.CATEGORY_GENERAL, true, "Shows an update Notification dropdown from top center of screen. Only happens when versions change.");
			
			
			
			// issuetracker url
			// issuetracker boolean useIngameForm
			// issuetracker default crashelogs to create gist. 
			// issuetracker send json data (automated issue report)

			MPBSettings.issuetrackerURL = config.getString("Issue Tracker url", issueTrackerCat, "https://github.com/GenDeathrow/MPUtils/issues", "http of your IssueTracker");
			MPBSettings.useInGameForm = config.getBoolean("Use in game form", issueTrackerCat, false, "Use an in game form, If false the issue tracker button will just link to the tracker url.");
			MPBSettings.crashlogsToGist = config.getBoolean("Crashlogs to Gist default", issueTrackerCat, true, "This just changes default setting to create a Gist like for change log. May break if git hub changes api.");
			MPBSettings.sendJsonData = config.getBoolean("Use Automated issue tracker", issueTrackerCat, false, "This is an Advance Settings, The web address must accept Json data to be able to automate issue reporting. It is up to you how you accept the json data on the website. I may have an example but only use this if you know what your doing.");
			MPBSettings.collectEmails = config.getBoolean("Collect Emails", issueTrackerCat, true, "If 'useInGameForm' && 'sendJsonData' = true, than As users for thier email address.");
			config.save();
		}
		
		/**
		 * Creates Default ChangeLogs
		 */
		private static void generateChangeLog()
		{
			File file = new File(ConfigHandler.configDir, "/changelog.txt");
				
			if (file.getParentFile() != null) {
	        		file.getParentFile().mkdirs();
	        	}

				if (file.exists()) {
				    return;
				}
				
				ArrayList<String> lines = new ArrayList<String>();
				lines.add(TextFormatting.YELLOW +"# Add Your ChangeLog to "+TextFormatting.RED+"'changelog.txt'"+TextFormatting.RESET+" in the config files" + NEW_LINE + NEW_LINE + "You can use Minecrafts "+TextFormatting.BLUE+"Color "+TextFormatting.DARK_GREEN+"Codes "+TextFormatting.GOLD+"to"+TextFormatting.RESET+" "+TextFormatting.UNDERLINE+"make your changlogs look Good.");

				try {
					MPFileUtils.createSaveTextFile(file, lines);
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		
		
}
