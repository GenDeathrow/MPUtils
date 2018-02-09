package com.gendeathrow.mpbasic.core;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.gendeathrow.mpbasic.common.infopanel.CapabilityInfoPanel;
import com.gendeathrow.mpbasic.core.proxies.MPBCommonProxy;
import com.gendeathrow.mpbasic.network.InfoPanelUpdate;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MPBasic.MODID, name=MPBasic.NAME, version = MPBasic.VERSION, dependencies=MPBasic.dependencies, guiFactory = "com.gendeathrow.mpbasic.configs.BTConfigGuiFactory")
public class MPBasic 
{
    public static final String MODID = "mpbasic";
    public static final String VERSION = "1.4.5";
    public static final String MCVERSION = "1.0.0";
    public static final String NAME = "ModPack Utils";
    public static final String PROXY = "com.gendeathrow.mpbasic.core.proxies";
    public static final String CHANNELNAME = "genmpbasic";
    @Instance(MODID)
	public static MPBasic instance;
    
    public static final String dependencies =  "required-after:mputils@[1.5.4,1.6.0]";
    
	@SidedProxy(clientSide = PROXY + ".MPBClientProxy", serverSide = PROXY + ".MPBCommonProxy")
	public static MPBCommonProxy proxy;
	
	public static SimpleNetworkWrapper network;
    
    public static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNELNAME);
    	network.registerMessage(InfoPanelUpdate.ClientHandler.class, InfoPanelUpdate.class, 0, Side.CLIENT);
    	network.registerMessage(InfoPanelUpdate.ServerHandler.class, InfoPanelUpdate.class, 1, Side.SERVER);
    	
    	CapabilityInfoPanel.register();
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
    
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{

	}
}
