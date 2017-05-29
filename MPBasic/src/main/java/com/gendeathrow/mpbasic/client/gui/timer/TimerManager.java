package com.gendeathrow.mpbasic.client.gui.timer;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.mpbasic.common.capabilities.player.IMPBasic_PlayerData;
import com.gendeathrow.mpbasic.core.MPBasic;
import com.gendeathrow.mputils.utils.RenderAssist;
import com.gendeathrow.mputils.utils.StopWatch;

@SideOnly(Side.CLIENT)
public class TimerManager 
{

	public static boolean firstload = true;
		
	private StopWatch timer = new StopWatch();
	
	public TimerManager() {}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderGameOverlayEvent.Post event)
	{
		if(event.getType() != ElementType.HELMET || event.isCancelable())
		{
			return;
		}
		if(firstload)
		{
			timer.start();
			firstload = false;
		}
		
		this.drawscreen(event);
		
	}
	
	@SubscribeEvent
	public void onSave(WorldEvent.Save event)
	{
		System.out.println("test");
		if(event.getWorld().isRemote)
		{
			SaveTimer(new File(DimensionManager.getCurrentSaveRootDirectory(),"timerData.dat"));
		}
	}

	private static int loadAttempts = 1;
	
	@SubscribeEvent
	public void onLoad(WorldEvent.Load event) throws IOException
	{
		   Minecraft mc = Minecraft.getMinecraft();
	
	       File file = new File(DimensionManager.getCurrentSaveRootDirectory(),"timerData.dat");

	        if (!file.exists()) 
	        {
	        	MPBasic.logger.warn("Config load canceled, file ("+ file.getAbsolutePath()  +")does not exist. This is normal for first run.");
	            if(loadAttempts <= 3)
	            {
	            	MPBasic.logger.warn(loadAttempts +" Attempt to load....");
	            	loadAttempts++;
	            	
	            	 SaveTimer(file);
	            	 LoadTimer(file);
	            }
	            return;
	        } else 
	        {
	        	LoadTimer(file);
	        	MPBasic.logger.info("Config load successful.");
	        }


	}
	
	
	public void SaveTimer(File file)
	{

        try 
        {
             NBTTagCompound nbt = new NBTTagCompound();
             FileOutputStream fileOutputStream = new FileOutputStream(file);

             nbt.setLong("playerTimer", timer.elapsed(TimeUnit.NANOSECONDS));
             CompressedStreamTools.writeCompressed(nbt, fileOutputStream);
             
             fileOutputStream.close();
             MPBasic.logger.info("Saved Timer Properties");
         } catch (IOException e) {
             throw new ReportedException(new CrashReport("An error occured while saving", new Throwable()));
         }
	}
	
	public void LoadTimer(File file)
	{
        try {
            NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(file));
            
            if (nbt.hasNoTags())
            {
            	return;
            }

           timer.setElapsedNanos(nbt.getLong("playerTimer"));
           
        }catch(IOException e)
        {
        	
        }
	}
	
	@SubscribeEvent
	public void onPlayerLogout(GuiOpenEvent event)
	{
		if(event.getGui() == null) return;
		
		if(event.getGui() instanceof GuiMainMenu)
		{
			firstload = true;
			
			
			//TimerHandler.reset();
			//EventHandler.pauseWait = false;
			//if(TimerHandler.WORLDSTOPWATCH != null) TimerHandler.WORLDSTOPWATCH.resetTimer();
		}
	
	}
	
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiPostInit(InitGuiEvent.Post event)
	{

	}
	
	
	static boolean wasPaused = false;
	
	private void drawscreen(Post event)
	{

		try
		{
			int startY = 10;
			

			if(timer == null) return;
			
				if(Minecraft.getMinecraft().isGamePaused() && !wasPaused)
				{
					wasPaused = true;
					timer.pause();
				}
				else if(wasPaused && !Minecraft.getMinecraft().isGamePaused())
				{
					wasPaused = false;
					timer.resume();
				}	
				
				//if(!timer.getVisability()) return;
				
				long elipsed = timer.elapsed(TimeUnit.MILLISECONDS);
				int hour = (int) TimeUnit.MILLISECONDS.toHours(elipsed);
				int mins = (int) TimeUnit.MILLISECONDS.toMinutes(elipsed) % 60;
				int secs = (int) TimeUnit.MILLISECONDS.toSeconds(elipsed) % 60;
				int mill = (int) (TimeUnit.MILLISECONDS.toMillis(elipsed) / 100) % 10;
			
				String text = " T: "+ String.format("%02d", hour) +":"+ String.format("%02d", mins) +":"+ String.format("%02d", secs) +":"+  String.format("%02d", mill);
					
				int boxWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 12;
				int boxheight = 18;
				RenderAssist.drawRectWithBorder(10, 10 + startY, 10+boxWidth,  10+boxheight +startY, java.awt.Color.white.getRGB(), java.awt.Color.gray.getRGB(), 2);
			
				Minecraft.getMinecraft().fontRendererObj.drawString(text , 10 + 5, 10 + 5 + startY, Color.black.getRGB());
				
				startY+= 20;
			
		}catch(NullPointerException e)
		{
		}
	}
		
}

