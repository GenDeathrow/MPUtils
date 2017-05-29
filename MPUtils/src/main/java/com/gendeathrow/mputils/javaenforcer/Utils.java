package com.gendeathrow.mputils.javaenforcer;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.Settings;

public class Utils 
{

	public static void loadConfigData(Logger logger)
	{
		Configuration config;
		File file = new File(ConfigHandler.configDir, "java_enforcer.cfg");
	
		config = new Configuration(file);
	
		config.load();
		config.addCustomCategoryComment(Configuration.CATEGORY_GENERAL, "If you have any questions check the Curse pages/wiki for help. Will have a few examples https://github.com/GenDeathrow/Java-Enforcer/wiki");
			String val = config.getString("Enforce Java Version", Configuration.CATEGORY_GENERAL, Double.toString(JE_Settings.JAVA_ENFORCER), "Throws an error if user doesn't have correct java version for mod pack. ie: if you set to 1.8 player must have java version 1.8+");
			JE_Settings.customMSG = config.getString("Custom Message", Configuration.CATEGORY_GENERAL, JE_Settings.customMSG, "Use simple html code to write a message, ex: \"<center><font color=red> sample message <br> next line </font></center>\" ");
			JE_Settings.updateCheck = config.getBoolean("Check for Update",  Configuration.CATEGORY_GENERAL, JE_Settings.updateCheck, "If true, will check for an update when player logs in.");
			JE_Settings.incompMods  = config.getStringList("Unsupported Mods List",  Configuration.CATEGORY_GENERAL, JE_Settings.incompMods, "Uses modid's. These prevent mods from being added that have known incompatability with this modpack. each line is a new modid.");
			JE_Settings.isOptifineCompatable = config.getBoolean("is Compatible with Optifine", Configuration.CATEGORY_GENERAL, JE_Settings.isOptifineCompatable, "Is your modpack compatable with Optifine");
		config.save();

		try
		{
			JE_Settings.JAVA_ENFORCER = (double) Double.parseDouble(val);
		}
		catch(Throwable e)
		{
			logger.log(Level.ERROR, "Error reading Java version number. Please check your config file. Defalt version: "+ JE_Settings.JAVA_ENFORCER );
		}
		
		logger.log(Level.INFO, "Enforcing java to version: "+ JE_Settings.JAVA_ENFORCER);
	}

}
