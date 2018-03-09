package com.gendeathrow.mputils.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.MPUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class Tools 
{
	//
	
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
    
    
    
    public static List<Class> getAllSuperclasses(Class cls) 
    {
        List<Class> res = new ArrayList<Class>();
        while ((cls = cls.getSuperclass()) != null) 
        {
            res.add(cls);
        }
        return res;
    }
    
    
    public static void DownloadFile(String url, String fileName) throws IOException 
    {
		 
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
    
//    @Deprecated
//    public static String CreateSaveFile(File file, String content)
//	{
//		try 
//		{
//
//			// if file doesnt exists, then create it
//			if (!file.exists()) 
//			{
//				file.createNewFile();
//			}
//
//			FileWriter fw = new FileWriter(file.getAbsoluteFile());
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(content);
//			bw.close();
//			
//			
//			return "File saved to: "+ file.getPath();
//		} catch (IOException e) 
//		{
//			e.printStackTrace();
//			
//			return "Error: While creating/saving file. Check Logs!";
//		}
//	}
    public static void CopyandPasteResourceToFile(InputStream ddlStream, String outputUrl) throws FileNotFoundException, IOException{
//        InputStream ddlStream = Tools.class
//        	    .getClassLoader().getResourceAsStream("some/pack/age/somelib.dll");

        	try (FileOutputStream fos = new FileOutputStream(outputUrl);){
        	    byte[] buf = new byte[2048];
        	    int r;
        	    while(-1 != (r = ddlStream.read(buf))) {
        	        fos.write(buf, 0, r);
        	    }
        	}
    	
    }
    
    @SuppressWarnings("resource")
	public static String URLReader(String urlString) throws Exception 
    {
    		String string = "";
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
            	string += inputLine + ConfigHandler.NEW_LINE;
            in.close();
        
    	
    	return removeUTF8BOM(string);
    }
    
    
    public static final String UTF8_BOM = "\uFEFF";

    private static String removeUTF8BOM(String s) 
    {
    	//System.out.println("checking for boom");
        if (s.startsWith(UTF8_BOM)) 
        {
        	//System.out.println("boom found");
            s = s.substring(1);
        }
        return s;
    }
    
	public static File lastFileModified(String dir) 
	{
	    File fl = new File(dir);
	    File[] files = fl.listFiles(new FileFilter() {          
	        public boolean accept(File file) {
	            return file.isFile();
	        }
	    });
	    long lastMod = Long.MIN_VALUE;
	    File choice = null;
	    for (File file : files) {
	        if (file.lastModified() > lastMod) {
	            choice = file;
	            lastMod = file.lastModified();
	        }
	    }
	    return choice;
	}
	
//	@Deprecated
//	public static String readFile(File path, Charset encoding) throws IOException 
//	{
//		return readFile(path.getPath(), encoding);
//	}
//	
//	@Deprecated
//	public static String readFile(String path, Charset encoding) throws IOException 
//	{
//		byte[] encoded = Files.readAllBytes(Paths.get(path));
//		return removeUTF8BOM(new String(encoded, encoding));
//	}
	
	public static String sendJsonHttpPost(String url, JsonObject postData) throws IOException
	{
		URL object=new URL(url);
		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json"); 
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");

		con.getOutputStream().write(postData.toString().getBytes());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();
	   
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			stringBuilder.append(line + "\n");
			//System.out.println(line);
		}
	    	
		con.connect();
	        
		return stringBuilder.toString();
	}
	 
    public static void sendpost() throws IOException
    {
    	URL object=new URL("http://logs-01.loggly.com/inputs/a522e114-193a-4518-ae3e-10a2732bc9f3/tag/http/");

    	HttpURLConnection con = (HttpURLConnection) object.openConnection();
    	con.setDoOutput(true);
    	con.setDoInput(true);
    	con.setRequestProperty("Content-Type", "content-type:text/plain");
    	//con.setRequestProperty("Accept", "application/json");
    	con.setRequestMethod("POST");
    	String hello = "hello";
    	
    	con.getOutputStream().write(hello.getBytes());
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
   
        String line = null;
        while ((line = reader.readLine()) != null)
        {
          stringBuilder.append(line + "\n");
          //System.out.println(line);
        }
    	
        con.connect();
    }
    
    public static String createGist(String filename, String data, String title) throws IOException
    {
    	URL object=new URL("https://api.github.com/gists");

    	Gson gson = new Gson();
    	
    	HttpURLConnection con = (HttpURLConnection) object.openConnection();
    	con.setDoOutput(true);
    	con.setDoInput(true);
    	con.setRequestProperty("Content-Type", "application/json");
    	con.setRequestProperty("Accept", "application/vnd.github.v3+json");
    	con.setRequestMethod("POST");
    	
    	JsonObject payload = new JsonObject();
    	
    	payload.addProperty("description", "CrashLog MPUtils: "+ title);
    	payload.addProperty("public", true);
    	
    		JsonObject files = new JsonObject();
    			JsonObject sendfile = new JsonObject();
    				sendfile.addProperty("content", data);
    	
    			files.add(filename, sendfile);
    		payload.add("files", files);
    		
    	
    	con.getOutputStream().write(payload.toString().getBytes());
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
   
        String line = null;
        while ((line = reader.readLine()) != null)
        {
          stringBuilder.append(line + "\n");
          //System.out.println(line);
        }
   
        
        
        JsonParser jsonParser = new JsonParser();
        JsonObject json = jsonParser.parse(stringBuilder.toString()).getAsJsonObject();
        
        
        con.connect();
        if( json.has("html_url") )
        {
        	return json.get("html_url").getAsString();
        }
        
        return line;
    }


	public static boolean CopytoClipbard(String string)
	{
		try
		{
			StringSelection selection = new StringSelection(string);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
			return true;
		}catch(Exception e)
		{
			MPUtils.logger.log(Level.ERROR, e);
			return false;
		}
	}
	

	
	public static String getItemInfo(String[] args, String itemID, ItemStack stack)
	{

		String ore = "";
		String nbt = "";
		
		if(args.length > 1)
		{
		
			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			
			for(String arg : args)
			{

				if(arg.toLowerCase().trim().equals("<>") && !flag1)
				{
					itemID = "<"+ itemID+">";
					flag1 = true;
				}
				else if(arg.toLowerCase().trim().equals("ore")  && !flag2)
				{
					ore += " Ores:[";
					boolean f = false;
					for(int id : OreDictionary.getOreIDs(stack))
					{
						ore += (!f ? " ore:" : " | ore:")+ OreDictionary.getOreName(id);
						f = true;
					}
					ore += "]";
					flag2 = true;
				}
				else if(arg.toLowerCase().trim().equals("nbt") && !flag3)
				{
					NBTTagCompound nbtdata = stack.getTagCompound();
				
					nbt += " NBT:";
					if(nbtdata != null)
					{
						nbt += " "+ new GsonBuilder().create().toJson(NBTJSONConverter.NBTtoJSON_Compound(nbtdata, new JsonObject()));
					}
					else nbt += " {NBT Null}";
					flag3 = true;
				}
			}
		
		}
		return itemID + ore + nbt +  ConfigHandler.NEW_LINE;
	}
	
	
	public static void popUpError(String msg, String msgpop, Logger logger)
	{
		final JFrame parent = new JFrame();
        
        parent.setSize(400, 400);
        
        JOptionPane.showMessageDialog(parent, msgpop, "Incompatible Error",  JOptionPane.ERROR_MESSAGE);

        logger.log(Level.ERROR, msg);

	}
}
