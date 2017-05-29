package com.gendeathrow.mputils.javaenforcer;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.utils.Tools;

public class ModChecker 
{
// (modid/modName):(reason)	
	
	private static HashMap<String, String> incompatableMods = new HashMap<String, String>();
	
	private static ArrayList<String> ignore = new ArrayList<String>();
	
	
	public static void run()
	{
		
		MPUtils.logger.log(Level.INFO, "Checking installed Mods...");
		
		if(FMLClientHandler.instance().hasOptifine() && !JE_Settings.isOptifineCompatable)
		{
			String msg = "This Modpack is not compatable with Optifine please uninstall this mod";
			Tools.popUpError(msg,"<html><center><p> This Modpack is not compatable with Optifine <br> <font Color=red>please uninstall this mod </font>", MPUtils.logger);
            throw new RuntimeException(msg);
		}

		ignore.add("FML");
		//ignore.add(JavaEnforcer.MODID);
		ignore.add("mcp");
		ignore.add("Forge");
		
		for(String modid : JE_Settings.incompMods)
		{
			String incompID = modid;
			String incompReason = "";
			
			if(modid.contains(":")) 
			{
				String[] split = modid.split("\\:", 2);
				incompID = split[0].trim();
				incompReason = split[1];
			}
			
			if(!ignore.contains(incompID))
			{
				incompatableMods.put(incompID, incompReason);
			}else if(ignore.contains(incompID)) 
			{
				MPUtils.logger.log(Level.WARN, "Can not Disable "+ incompID);
			}

		}

		for(net.minecraftforge.fml.common.ModContainer mod : Loader.instance().getActiveModList())
		{
			if(incompatableMods.containsKey(mod.getModId()) || incompatableMods.containsKey(mod.getName()))
			{
				String msg = "This Modpack is not compatable with "+mod.getName()+" please uninstall this mod";
				Tools.popUpError(msg,"<html><center><p> This Modpack is not compatable with "+mod.getName()+" <br> <font Color=red>please uninstall this mod </font> ", MPUtils.logger);
	            throw new RuntimeException(msg);
			}
		}
		
	}
	

}
