package com.gendeathrow.mputils.core;

import java.io.IOException;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import org.apache.logging.log4j.Logger;

import com.gendeathrow.mputils.core.proxies.MPCommonProxy;

// 1.10.2 Minecraft

@Mod(modid = MPUtils.MODID, name=MPUtils.NAME, version = MPUtils.VERSION, guiFactory = "com.gendeathrow.mputils.configs.ConfigGuiFactory")
public class MPUtils 
{
    public static final String MODID = "mputils";
    public static final String VERSION = "1.2.0";
    public static final String NAME = "MPUtils";
    public static final String PROXY = "com.gendeathrow.mputils.core.proxies";
    

    public static final String MCVERSION = "1.0.0";
    public static final String VERSION_MAX = "1.0.0";
	public static final String version_group = "required-after:" + MODID + "@[" + VERSION + "," + VERSION_MAX + ");";
	
	
    @Instance(MODID)
	public static MPUtils instance;
    
	@SidedProxy(clientSide = PROXY + ".MPClientProxy", serverSide = PROXY + ".MPCommonProxy")
	public static MPCommonProxy proxy;
    
    public static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	
    	proxy.preInit(event);
    }
	
    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException
    {
    	//Tools.sendpost();
    	
    	proxy.init(event);
     }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }
    
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{

	}
    
}
