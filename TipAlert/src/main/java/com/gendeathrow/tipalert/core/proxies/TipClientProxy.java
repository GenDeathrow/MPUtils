package com.gendeathrow.tipalert.core.proxies;


import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.tipalert.client.SideButtonHandler;
import com.gendeathrow.tipalert.commands.MP_ReloadTips;
import com.gendeathrow.tipalert.config.TipConfigHandler;
import com.gendeathrow.tipalert.core.TipAlert;
import com.gendeathrow.tipalert.prompt.TipManager;

public class TipClientProxy extends TipCommonProxy
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
		
		MinecraftForge.EVENT_BUS.register(TipAlert.tipManager = new TipManager());
		FMLCommonHandler.instance().bus().register(TipAlert.tipManager);

	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		

	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		registerEventHandlers();
		
		TipConfigHandler.load();
		
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
		MP_ReloadTips.init();
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
