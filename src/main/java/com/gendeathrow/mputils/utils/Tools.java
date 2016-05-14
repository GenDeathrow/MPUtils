package com.gendeathrow.mputils.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.gendeathrow.mputils.configs.ConfigHandler;
import com.google.gson.GsonBuilder;

public class Tools 
{
	
	
    public static InputStreamReader HttpRequest() throws IOException 
    {
    	
            String stringUrl = "https://api.twitch.tv/kraken/search/streams?q=starcraft";
	        URL url = new URL(stringUrl);
	        URLConnection uc = url.openConnection();

	        uc.setRequestProperty("application/vnd.twitchtv.v3+json", "Accept");

	        InputStreamReader inputStreamReader = new InputStreamReader(uc.getInputStream());

	        //System.out.println(IOUtils.toString(inputStreamReader));
	        
	        return inputStreamReader;
	}
    
    
    public static void DownloadFile(String url, String fileName) throws IOException {
		 
		 URL link = new URL(url); //The file that you want to download
		
		 InputStream in = new BufferedInputStream(link.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }
		 out.close();
		 in.close();
		 byte[] response = out.toByteArray();

		 FileOutputStream fos = new FileOutputStream(fileName);
		 fos.write(response);
		 fos.close();

	}

    @SuppressWarnings("resource")
	public static String URLReader(String urlString) throws Exception 
    {
    	String string = "";
    		try {
    		   URL url = new URL(urlString);
    		   Scanner s = new Scanner(url.openStream());
    		   // read from your scanner
    		   
    		   while(s.hasNextLine())
    		   {
    			  string += s.nextLine() + ConfigHandler.NEW_LINE;
    		   }
    		   

    		   
    		}
    		catch(IOException ex) {

    		   ex.printStackTrace(); 
    		   return null;
    		}
    		
    		return string;
    }
    
	
	
}
