package com.gendeathrow.mpbasic.core.proxies;


import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.mpbasic.client.SideButtonHandler;
import com.gendeathrow.mpbasic.client.MPSave.BTSaveHandler;
import com.gendeathrow.mpbasic.client.gui.timer.TimerManager;
import com.gendeathrow.mpbasic.client.notification.BasicNotifcationManager;
import com.gendeathrow.mpbasic.configs.MPBConfigHandler;

public class MPBClientProxy extends MPBCommonProxy
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
		
		SideButtonHandler eventhandler = new SideButtonHandler();
		MinecraftForge.EVENT_BUS.register(eventhandler);
		
		BasicNotifcationManager notification = new BasicNotifcationManager(Minecraft.getMinecraft());
		MinecraftForge.EVENT_BUS.register(notification);
		
		MinecraftForge.EVENT_BUS.register(new TimerManager());

	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		BTSaveHandler.register();
		
		super.preInit(event);

	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		registerEventHandlers();
		
		MPBConfigHandler.load();
		
		SideButtonHandler.RegisterSideButtons();
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
