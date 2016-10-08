package com.gendeathrow.tipalert.prompt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import org.lwjgl.input.Keyboard;

import com.gendeathrow.mputils.client.gui.notification.NotificationManager;
import com.gendeathrow.tipalert.core.TipAlert;
import com.gendeathrow.tipalert.core.TipSettings;
import com.gendeathrow.tipalert.core.network.TipAlertPacket;
import com.gendeathrow.tipalert.prompt.Tips.Tip;
import com.google.common.collect.Lists;

public class TipManager 
{
	
	public NotificationManager tipNotification = new NotificationManager(Minecraft.getMinecraft());
	
	public Tip lastTip;
	
	private boolean flag = true;
	
	private final File datfile = new File(Minecraft.getMinecraft().mcDataDir, "mputils/tipcache.dat");
	
	@SubscribeEvent
	public void drawNotification(RenderGameOverlayEvent.Post event)
	{
		if (!event.getType().equals(ElementType.CHAT)) return;
		tipNotification.updateTipWindow();
	}
	
	
	private void setLastTip(Tip tipIn)
	{
		this.lastTip = tipIn;
	}
	
	public Tip getLastTip()
	{
		return this.lastTip;
	}
	
	@SubscribeEvent	
	public void lookingAt(DrawBlockHighlightEvent event)
	{
		if(!TipSettings.showTips) return;
		
		if(!(Minecraft.getSystemTime() % 20 == 0)) return;

		if(event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK)
		{
			IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(event.getTarget().getBlockPos());
			
			if(blockState != null && blockState != Blocks.AIR)
			{
				Tip tip = TipList.checkTipLookingAt(blockState);
				
				if(tip != null)
				{
					tipNotification.addQueue(tip);
					setLastTip(tip);
					this.SaveNBTFile();
				}
			}
		}
	}
	
	@SubscribeEvent	
	public void travelDimension(PlayerChangedDimensionEvent event)
	{
		if(!TipSettings.showTips) return;
		
		Tip tip = TipList.checkdimension(event.toDim);
		
		if(tip == null) return;
		
		tipNotification.addQueue(tip);
		setLastTip(tip);
		this.SaveNBTFile();
	}
	
	@SubscribeEvent	
	public void onItemPickup(EntityItemPickupEvent event)
	{
		if(!TipSettings.showTips) return;
		
	}
	
	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event)
	{
		if(!TipSettings.showTips) return;
	}
	
	@SubscribeEvent
	public void onKill(LivingDeathEvent event)
	{
		if(!TipSettings.showTips) return;
		
		if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayerMP)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("type", "onDeath");
			tag.setString("entityID", EntityList.getEntityString(event.getEntity()));
			
			TipAlert.network.sendTo(new TipAlertPacket(0, tag), (EntityPlayerMP) event.getSource().getEntity());
		}
	}
	
	
	public void OnPacket(NBTTagCompound nbt)
	{
		if(nbt.hasKey("type"))
		{
			switch(nbt.getString("type"))
			{
				case "onDeath":
					Tip tip = TipList.checkEntityDeath(nbt.getString("entityID"));
					if(tip == null) return;
					
					tipNotification.addQueue(tip);
					setLastTip(tip);
					this.SaveNBTFile();
					
					break;
					
				default:
					break;
					
			}
		}
	}
	
	@SubscribeEvent
	public void onLocation(ClientTickEvent tick)
	{
		
		if(!(Minecraft.getSystemTime() % 40 == 0)) return;
		
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		
		
		
	}
	
	@SubscribeEvent
	public void itemToolTip(ItemTooltipEvent event) 
	{
		if(!TipSettings.showTips) return;
		
		ItemStack itemstack = event.getItemStack();
		
		Tip tip = TipList.checkToolTip(itemstack);
		
		if(tip == null) return;		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			event.getToolTip().add(TextFormatting.YELLOW +"Helpful Tip:");
			event.getToolTip().addAll(Lists.newArrayList(tip.getDescription().replaceAll("\t", "").split("\n"))); //.add(tip.desc);
		}
		else
		{
			event.getToolTip().add(TextFormatting.DARK_RED +"<Hold shift for helpful tip>");
		}
		
	}
	
	public void setClearTips()
	{
        for(Tip tip : TipList.TIPS)
        {
        	tip.setSeen(0);
        	tip.showTipCnt = 0;
        }
	}
	
	public void SaveNBTFile()
	{
        try 
        {
            NBTTagCompound nbt = new NBTTagCompound();
            FileOutputStream fileOutputStream = new FileOutputStream(datfile);

            for(Tip tip : TipList.TIPS)
            {
            	NBTTagCompound tipTag = new NBTTagCompound();
            	
            	tipTag.setString("tipID", tip.getTitle());
            	tipTag.setLong("lastSeen", tip.lastSeen());
            	tipTag.setInteger("showCnt", tip.showTipCnt);
            	
            	nbt.setTag(tip.tipId, tipTag);
            }
            
            NBTTagCompound nbtSettings = new NBTTagCompound();
            
            nbtSettings.setBoolean("showTips", TipSettings.showTips);
            
            nbt.setTag("Settings", nbtSettings);
            
            CompressedStreamTools.writeCompressed(nbt, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) 
        {
            throw new ReportedException(new CrashReport("An error occured while saving", new Throwable()));
        }
	}
	
	public void ReadNBTFile()
	{
		if (!datfile.exists()) 
        {
            TipAlert.logger.warn("File load canceled, file ("+ datfile.getAbsolutePath()  +")does not exist. This is normal for first run.");
            return;
        } else 
        {
        	TipAlert.logger.info("File load successful.");
        }
        
        try 
        {
            NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(datfile));
            
            if (nbt.hasNoTags())
            {
            	return;
            }
            
            for(Tip tip : TipList.TIPS)
            {
            	if(nbt.hasKey(tip.tipId))
            	{
            		NBTTagCompound tipTag = nbt.getCompoundTag(tip.tipId);
            		
                	tip.setSeen(tipTag.getLong("lastSeen"));
                	tip.showTipCnt = tipTag.getInteger("showCnt");
                	
              	//System.out.println(tip.LastSeen() +"-"+ tip.showTipCnt);
                	
            	}
            }
           
            if(nbt.hasKey("Settings"))
            {
            	 TipSettings.showTips = nbt.getCompoundTag("Settings").getBoolean("showTips");
            }
           
  
        } catch (IOException e) {
            e.printStackTrace();
        }
            
	}

}
