package com.gendeathrow.mputils.core;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.gendeathrow.mputils.commands.common.MP_Commands;
import com.gendeathrow.mputils.core.proxies.MPCommonProxy;
import com.gendeathrow.mputils.network.RequestTEPacket;
import com.gendeathrow.mputils.network.ScreenNotificationPacket;

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

// 1.11.2 Minecraft

@Mod(modid = MPUtils.MODID, name=MPUtils.NAME, version = MPUtils.VERSION, dependencies="before:mpbasic@[1.4.7,)", guiFactory = "com.gendeathrow.mputils.configs.ConfigGuiFactory")
public class MPUtils 
{
    public static final String MODID = "mputils";
    public static final String VERSION = "1.5.6";
    public static final String NAME = "MPUtils";
    public static final String PROXY = "com.gendeathrow.mputils.core.proxies";
    public static final String CHANNELNAME = "genmputils";

    public static final String MCVERSION = "1.4.0";
    public static final String VERSION_MAX = "1.0.0";
	public static final String version_group = "required-after:" + MODID + "@[" + VERSION + "," + VERSION_MAX + ");";
	
	
    @Instance(MODID)
	public static MPUtils instance;
    
	@SidedProxy(clientSide = PROXY + ".MPClientProxy", serverSide = PROXY + ".MPCommonProxy")
	public static MPCommonProxy proxy;
    
	public static SimpleNetworkWrapper network;
	
    public static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	
    	proxy.preInit(event);
    	
		this.network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNELNAME);
    	network.registerMessage(RequestTEPacket.ServerHandler.class, RequestTEPacket.class, 0, Side.SERVER);
    	network.registerMessage(RequestTEPacket.ClientHandler.class, RequestTEPacket.class, 1, Side.CLIENT);
    	network.registerMessage(ScreenNotificationPacket.ClientHandler.class, ScreenNotificationPacket.class, 2, Side.CLIENT);

    }
	
   
    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException
    {
    	//Tools.sendpost();
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    	
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
		event.registerServerCommand(new MP_Commands());
	}
    
}
