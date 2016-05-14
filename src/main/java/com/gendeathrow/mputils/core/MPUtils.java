package com.gendeathrow.mputils.core;

import java.io.IOException;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.gendeathrow.mputils.core.proxies.MPCommonProxy;
import com.gendeathrow.mputils.prompt.TipManager;

// 1.9 
@Mod(modid = MPUtils.MODID, version = MPUtils.VERSION)
public class MPUtils 
{
    public static final String MODID = "mputils";
    public static final String VERSION = "0.0.11";
    public static final String NAME = "ModPack Utils";
    public static final String PROXY = "com.gendeathrow.mputils.core.proxies";
    
    @SideOnly(Side.CLIENT)
    public static TipManager tipHandler;
	
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
    	proxy.init(event);
     }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }
    
//	@EventHandler
//	public void serverStart(FMLServerStartingEvent event)
//	{
//
//		ICommandManager command = event.getServer().getCommandManager();
//		ServerCommandManager manager = (ServerCommandManager) command;
//		
//		manager.registerCommand(new MP_Commands());
//
//	}
    
}
