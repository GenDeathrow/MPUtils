package com.gendeathrow.mputils.client.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.gendeathrow.mputils.api.client.gui.IMPSaveHandler;
import com.gendeathrow.mputils.core.MPUtils;

import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;

public class MPUtils_SaveHandler 
{
	public static final File settingsDir = new File("mputils");
	public static final File settingsFile = new File(settingsDir, "settings.dat");

	public static final File globalQuickDir = new File(System.getProperty("user.home"), "."+settingsDir);
	public static final File globalQuickFile = new File(globalQuickDir, "settings.dat");
	
	public static boolean firstSave = false;
	private static boolean globalFirstSave = true;
	
	public static final ArrayList<IMPSaveHandler> extraSaveData = new ArrayList<IMPSaveHandler>();

	public static void RegisterExtraData(IMPSaveHandler handler)
	{
		if(!extraSaveData.contains(handler))
		{
			MPUtils.logger.info("Registered MPUtils SaveHandler: "+ handler.getClass().getSimpleName());
			extraSaveData.add(handler);
		}
	}
	
	public static void loadSettings()
	{
		// Read Normal File
		NBTTagCompound mainTag = ReadNBTFile(getSettingsFile());
		
		if(mainTag != null && !mainTag.hasNoTags())
		{

			Iterator<IMPSaveHandler> ite = extraSaveData.iterator();
			while(ite.hasNext())
			{
				IMPSaveHandler obj = ite.next();
				obj.ReadNBT(mainTag);
			}

			QuickCommandManager.load(QuickCommandManager.getList(0), mainTag);
		}
		
		// Read GlobalFile
		NBTTagCompound globalTag = ReadNBTFile(getGlobalFile());
		
		if(globalTag != null && !globalTag.hasNoTags())
		{
			QuickCommandManager.load(QuickCommandManager.getList(1), globalTag);
		}
		
		
	}
	
	public static void saveSettings()
	{	
		saveLocal();
		
		saveGlobal();
	}
	
	private static void saveLocal()
	{
		// Save Instance File
		NBTTagCompound mainTag = new NBTTagCompound();
		
		QuickCommandManager.save(QuickCommandManager.getList(0), mainTag);
		
		Iterator<IMPSaveHandler> ite = extraSaveData.iterator();
		while(ite.hasNext())
		{
			IMPSaveHandler obj = ite.next();
			obj.SaveNBT(mainTag);
		}
		SaveNBTFile(settingsFile, mainTag);			
	}
	
	private static void saveGlobal()
	{
		// Save Global File
		NBTTagCompound globalTag = new NBTTagCompound();
		
		QuickCommandManager.save(QuickCommandManager.getList(1), globalTag);
		
		//System.out.println(globalQuickFile.getAbsolutePath());
		SaveNBTFile(globalQuickFile, globalTag);
	}
	
	private static File getSettingsFile()
	{
		try 
		{	
			if (settingsFile.exists())
			{
				firstSave = false;
				return settingsFile;
			} else 
			{
				firstSave = true;
				settingsDir.mkdirs();
				settingsFile.createNewFile();
				saveLocal();	
			}
			

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return settingsFile;
	}
	
	private static File getGlobalFile()
	{
		try 
		{	
			if (globalQuickFile.exists())
			{
				globalFirstSave = false;
				return globalQuickFile;
			} else 
			{
				globalFirstSave = true;
				globalQuickDir.mkdirs();
				globalQuickFile.createNewFile();
				saveGlobal();	
			}
			
			//System.out.println("::"+ globalQuickFile.getAbsolutePath());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return settingsFile;
	}

	public static void SaveNBTFile(File file, NBTTagCompound nbt)
	{
        try 
        {
            
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            CompressedStreamTools.writeCompressed(nbt, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new ReportedException(new CrashReport("An error occured while saving", new Throwable()));
        }
	}
	
	
	public static NBTTagCompound ReadNBTFile(File file)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		
		
		if (!file.exists()) 
		{
			MPUtils.logger.warn("File load canceled, file ("+ file.getAbsolutePath()  +")does not exist. This is normal for first run.");
			return null;
		} else 
		{
	        MPUtils.logger.info("File load successful.");
		}
	        
		try 
		{
			nbt = CompressedStreamTools.readCompressed(new FileInputStream(file));
	            
			if (nbt.hasNoTags())
			{
				return null;
	        }
	            
	  
	        } catch (IOException e) 
			{
	            e.printStackTrace();
	        }
	        
		return nbt;
	}
	
	
	
}
