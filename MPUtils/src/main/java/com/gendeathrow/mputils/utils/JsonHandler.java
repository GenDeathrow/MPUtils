package com.gendeathrow.mputils.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.core.MPUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


public class JsonHandler 
{

	public static JsonObject ReadJsonFile(File f) 
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
	
	
	public static boolean WriteJsonFile(File file, JsonObject json) 
	{
		 try 
	        {	
	         	
	        	file.createNewFile();
				
	        	if (file.canWrite())
	        	{
	        		FileWriter fw = new FileWriter(file);
	        			
	         		new GsonBuilder().setPrettyPrinting().create().toJson(json, fw);

	        		fw.flush();
	        		fw.close();
	        		
	                return true;
	        	}
			}catch (IOException e) 
	        {
				e.printStackTrace();
				return false;
	        }
	        
	        return false;
	}
	
}
