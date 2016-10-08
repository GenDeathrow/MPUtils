package com.gendeathrow.tipalert.core;

import java.io.IOException;

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

import org.apache.logging.log4j.Logger;

import com.gendeathrow.tipalert.core.network.TipAlertPacket;
import com.gendeathrow.tipalert.core.proxies.TipCommonProxy;
import com.gendeathrow.tipalert.prompt.TipManager;

@Mod(modid = TipAlert.MODID, name= TipAlert.NAME, version = TipAlert.VERSION, guiFactory="com.gendeathrow.tipalert.config.TipConfigGuiFactory", dependencies=TipAlert.dependencies)
public class TipAlert 
{
    public static final String MODID = "tipalert";
    public static final String VERSION = "1.1.0";
    
    public static final String NAME = "Helpful Tip Alerts";
    public static final String PROXY = "com.gendeathrow.tipalert.core.proxies";
    public static final String CHANNELNAME = "gentipalert";
	
    public static final String MCVERSION = "1.0.0";

    
    public static final String dependencies =  "required-after:mputils@[1.2.0,1.3.0]";
	
    @Instance(MODID)
	public static TipAlert instance;
    
    public static TipManager tipManager;
    
	@SidedProxy(clientSide = PROXY + ".TipClientProxy", serverSide = PROXY + ".TipCommonProxy")
	public static TipCommonProxy proxy;
	
	public static SimpleNetworkWrapper network;
    
    public static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	
    	
       	TipAlert.network = NetworkRegistry.INSTANCE.newSimpleChannel(TipAlert.CHANNELNAME);
       	
   	 
        network.registerMessage(TipAlertPacket.ClientHandler.class, TipAlertPacket.class, 0, Side.CLIENT);
        network.registerMessage(TipAlertPacket.ServerHandler.class, TipAlertPacket.class, 1, Side.SERVER);

    	
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
