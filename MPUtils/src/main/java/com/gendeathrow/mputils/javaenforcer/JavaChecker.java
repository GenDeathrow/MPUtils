package com.gendeathrow.mputils.javaenforcer;

import java.net.URI;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.gendeathrow.mputils.utils.Tools;

public class JavaChecker 
{
	private static Logger logger;
	
	public static void run(Logger log) throws InterruptedException
	{

		logger = log;
		
		JE_Settings.JAVA_VERSION = getJavaVersion();

		if(JE_Settings.JAVA_VERSION < JE_Settings.JAVA_ENFORCER) 
		{
			String msg = "This Modpack requires Java Version "+ JE_Settings.JAVA_ENFORCER +". You have version "+ JE_Settings.JAVA_VERSION +". Go to "+ JE_Settings.http;
		
			String msgpop = "<html><center><p> This Modpack requires <font Color=red>Java Version "+ JE_Settings.JAVA_ENFORCER +"</font>. <br> You have version "+ JE_Settings.JAVA_VERSION +".<br> "+ JE_Settings.customMSG +"  <br> <br>  <br> <br> Java Enforcer will take you to the download page <Br><font Color=Blue><color Go to "+ JE_Settings.http +"</font>";
			
			Tools.popUpError(msg, msgpop, logger);
			
			logger.log(Level.ERROR, msg);
			
			System.out.println(msg);
            
			gotoHttp();
        
            logger.log(Level.ERROR, msg);
		
            throw new RuntimeException(msg);
		}
		else
		{
			logger.log(Level.INFO, "You are using the correct Java Version Congrats!");
			//System.out.println("You are using the correct Java Version Congrats!");
		}
		
	}
	
    private static double getJavaVersion()
    {
     	 String version = System.getProperty("java.version");
     	    
     	 logger.log(Level.INFO, "Grabbing Java Version..."+ version);
     	 //System.out.println("Grabbing Java Version..."+ version);
     	 
    	 int pos = version.indexOf('.');
    	 pos = version.indexOf('.', pos+1);
    	 return Double.parseDouble (version.substring (0, pos));
    }
    
    private static void gotoHttp()
    {
    
        try
        {
        	Class oclass = Class.forName("java.awt.Desktop");
        	Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
        	oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(JE_Settings.http)});
        }	
        catch (Throwable throwable)
        {
        	 logger.error("Couldn\'t open link", throwable);
        	//System.out.println("Couldn\'t open link "+ throwable);
        }
    }

}
