package com.gendeathrow.mputils.core.proxies;


import java.util.ArrayList;

import scala.actors.threadpool.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.mputils.client.gui.GuiEventHandler;
import com.gendeathrow.mputils.client.keybinds.KeyBinds;
import com.gendeathrow.mputils.client.settings.MPUtils_SaveHandler;
import com.gendeathrow.mputils.commands.client.MP_ClientCommands;
import com.gendeathrow.mputils.configs.ConfigHandler;
import com.gendeathrow.mputils.utils.NewMPInfo;
import com.google.common.eventbus.Subscribe;

public class MPClientProxy extends MPCommonProxy
{
	
	
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public boolean isOpenToLAN()
	{
		if (Minecraft.getMinecraft().isIntegratedServerRunning())
		{
			return Minecraft.getMinecraft().getIntegratedServer().getPublic();
		} else
		{
			return false;
		}
	}
	
	@Override
	public void registerTickHandlers()
	{
		super.registerTickHandlers();
	}
	
	@Override
	public void registerEventHandlers()
	{
		super.registerEventHandlers();
		
		
			GuiEventHandler guieventhandler = new GuiEventHandler();
    		MinecraftForge.EVENT_BUS.register(guieventhandler);
    		
    		guieventhandler.addMPUtilsTabs();
   
    		FMLCommonHandler.instance().bus().register(new KeyBinds());
	}
	

    
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		
		ConfigHandler.load();
		


        //Loader.instance().getActiveModList().add(mp);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		registerEventHandlers();
		
	  	ClientCommandHandler.instance.registerCommand(new MP_ClientCommands());

		KeyBinds.Init();
		
		MPUtils_SaveHandler.loadSettings();		
	}
	
	@SideOnly(Side.CLIENT)
	public static void initRenderers()
	{

	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);

		//VoxelMenu();
	}
	
	
	public void VoxelMenu()
	{
//		try
//		{
//
//			Class<? extends GuiMainMenu> ingameGuiClass = (Class<? extends GuiMainMenu>) Class.forName("com.thevoxelbox.voxelmenu.ingame.GuiIngameMenu");
//			Method mRegisterCustomScreen = ingameGuiClass.getDeclaredMethod("registerCustomScreen", String.class, Class.class, String.class);
//			
//			mRegisterCustomScreen.invoke(null, "", EM_Gui_Menu.class, StatCollector.translateToLocal("options.enviromine.menu.title"));
//		
//			boolean voxelMenuExists = true;
//		} catch (ClassNotFoundException ex) { // This means VoxelMenu does not
//			// 	exist
//			boolean voxelMenuExists = false;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
