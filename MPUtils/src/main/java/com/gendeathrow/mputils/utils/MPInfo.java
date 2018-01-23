package com.gendeathrow.mputils.utils;

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionRange;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MPInfo implements ModContainer
{
	public static String name = "name";
	public static String description = "desciption";
	public static String version = "0.0.0";
	public static String url = "";
	public static List<String> authorList = new ArrayList<String>();
	public static String credits = "";
	public static String logoFile = "";
	
	public static final MPInfo instance = new MPInfo();
	
	
	public static boolean isActive()
	{
		return Settings.useMPInfo;
	}
	
	public static List<Integer> getVersionList()
	{
		return null;
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
	
	public static int[] getFormatedMPVer()
	{
		return getFormatedMPVer(MPInfo.version);
	}
// File Read & Writers	
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
			MPUtils.logger.info("Saved MPInfo File");
		
	}

// MOD Container Readers
	@Override
	public ModMetadata getMetadata() 
	{
		
		ModMetadata meta = new ModMetadata();
		meta.modId = MPInfo.name.toLowerCase().replaceAll(" ", "");
		meta.name = MPInfo.name;
		meta.authorList = MPInfo.authorList;
		meta.description = MPInfo.description;
		//meta.logoFile = MPInfo.logoFile;
		meta.url = MPInfo.url;
		meta.version = MPInfo.version;
		meta.credits = MPInfo.credits;
		return meta; 
	}


	
// GUI DRAWING EVENTS Starts here
	private static Field logoField;
	private static Field selectedField ;
	private static Field modField;
	private static Field dimensionField;
	private static boolean canDraw = true;
	private static Dimension dimension;
	private static ResourceLocation cache;
	
	@SideOnly(Side.CLIENT)
	public static void AddtoGui(GuiModList gui)
	{
		try 
		{
			modField = gui.getClass().getDeclaredField("mods");
			Field selectedmodField = gui.getClass().getDeclaredField("selectedMod");
			modField.setAccessible(true);
			selectedmodField.setAccessible(true);
			
			ArrayList<ModContainer> modFieldValue = (ArrayList<ModContainer>) modField.get(gui);
		
			if(!modFieldValue.contains(MPInfo.instance))
			{
				modFieldValue.add(0, MPInfo.instance);
			}
		
		} catch (NoSuchFieldException e) {
			canDraw = false;
			e.printStackTrace();
		} catch (SecurityException e) {
			canDraw = false;
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			canDraw = false;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			canDraw = false;
			e.printStackTrace();
		}	
		
	}


	public static void post(GuiModList gui) 
	{
		gui.selectModIndex(0);
	}
	
	@SideOnly(Side.CLIENT)
	public static void DrawScreen(GuiModList gui)
	{
		if(!canDraw) return;
		try 
		{
			selectedField = GuiModList.class.getDeclaredField("selected");
			selectedField.setAccessible(true);
			int selected = selectedField.getInt(gui);

		} 
		catch (SecurityException e) { 	e.printStackTrace();} 
		catch (IllegalArgumentException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace();	} 
		catch (NoSuchFieldException e) { e.printStackTrace(); } 	

	}

	@Override
	public String getModId() { return this.name; }


	@Override
	public String getName() { return this.name; }


	@Override
	public String getVersion() { return this.version; }
	
	@Override
	public String getDisplayVersion() {	return this.version; }
	
	
	@Override
	public URL getUpdateUrl() {	return null; }


	
// JUNK	

	@Override
	public File getSource() { return null; }
	
	@Override
	public void bindMetadata(MetadataCollection mc) {  }


	@Override
	public void setEnabledState(boolean enabled) { }


	@Override
	public Set<ArtifactVersion> getRequirements() { return null; }


	@Override
	public List<ArtifactVersion> getDependencies() { return null; }


	@Override
	public List<ArtifactVersion> getDependants() { 	return null; }


	@Override
	public String getSortingRules() { return null; }


	@Override
	public boolean registerBus(EventBus bus, LoadController controller) { return false; }


	@Override
	public boolean matches(Object mod) { return false; }


	@Override
	public Object getMod() { return null; }


	@Override
	public ArtifactVersion getProcessedVersion() {	return null; }


	@Override
	public boolean isImmutable() {	return false; }


	@Override
	public VersionRange acceptableMinecraftVersionRange() {	return null; }


	@Override
	public Certificate getSigningCertificate() { return null; }


	@Override
	public Map<String, String> getCustomModProperties() { return null; }


	@Override
	public Class<?> getCustomResourcePackClass() {	return null; }


	@Override
	public Map<String, String> getSharedModDescriptor() { return null; }


	@Override
	public Disableable canBeDisabled() { return Disableable.NEVER; }


	@Override
	public String getGuiClassName() {	return null; }


	@Override
	public List<String> getOwnedPackages() { return null; }


	@Override
	public boolean shouldLoadInEnvironment() {	return false; }

	@Override
	public void setClassVersion(int classVersion) { }


	@Override
	public int getClassVersion() {	return 0; }


}
