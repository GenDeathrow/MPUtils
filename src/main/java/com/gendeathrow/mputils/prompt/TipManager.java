package com.gendeathrow.mputils.prompt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.input.Keyboard;

import com.gendeathrow.mputils.client.tips.TipsNotification;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.core.Settings;
import com.google.common.collect.Lists;

public class TipManager 
{
	
	public List<TipsNotification> tipsQueue = Lists.<TipsNotification>newArrayList();
	
	public TipsNotification tipNotification = new TipsNotification(Minecraft.getMinecraft());
	
	private boolean flag = true;
	
	private File datfile = new File(Minecraft.getMinecraft().mcDataDir, "tipcache.dat");
	
	@SubscribeEvent
	public void drawNotification(RenderGameOverlayEvent.Post event)
	{
		if (!event.getType().equals(ElementType.CHAT)) return;
		tipNotification.updateTipWindow();
	}
	
	
	@SubscribeEvent	
	public void lookingAt(DrawBlockHighlightEvent event)
	{
//		if(event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK)
//		{
//			if(event.getTarget() != null && MP_LookAtCommand.currentBlock != Minecraft.getMinecraft().theWorld.getBlockState(event.getTarget().getBlockPos()))
//			{
//				MP_LookAtCommand.currentBlock = Minecraft.getMinecraft().theWorld.getBlockState(event.getTarget().getBlockPos());
//			}
//		}
//		
		if(!Settings.showTips) return;
		
		if(!(Minecraft.getSystemTime() % 20 == 0)) return;

		if(event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK)
		{
			IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(event.getTarget().getBlockPos());
			
			if(blockState != null && blockState != Blocks.air)
			{
				Tip tip = TipList.checkTipLookingAt(blockState);
				
				if(tip != null)
				{
					tipNotification.addQueue(tip);
					this.SaveNBTFile();
				}
			}
		}
	}
	
	@SubscribeEvent	
	public void travelDimension(EntityTravelToDimensionEvent event)
	{
		if(!Settings.showTips) return;
		
		Tip tip = TipList.checkdimension(event.getDimension());
		
		if(tip == null) return;
		
		tipNotification.addQueue(tip);
		this.SaveNBTFile();
		
	}
	
//	@SubscribeEvent	
//	public void onItemPickup(EntityItemPickupEvent event)
//	{
//		if(!Settings.showTips) return;
//		
//	}
//	
//	@SubscribeEvent
//	public void onItemCrafted(ItemCraftedEvent event)
//	{
//		
//	}
//	
//	public void onKill(){}
	
	@SubscribeEvent
	public void itemToolTip(ItemTooltipEvent event) 
	{
		if(!Settings.showTips) return;
		
		ItemStack itemstack = event.getItemStack();
		
		Tip tip = TipList.checkToolTip(itemstack);
		
		if(tip == null) return;		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			event.getToolTip().add("§eHelpful Tip:");
			event.getToolTip().addAll(Lists.newArrayList(tip.desc.replaceAll("\t", "").split("\n"))); //.add(tip.desc);
		}
		else
		{
			event.getToolTip().add("§4<Hold shift for helpful tip>");
		}
		
	}
	
	public void interact(PlayerInteractEvent.RightClickBlock event) {}
	
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
            	
            	tipTag.setString("tipID", tip.getStatName());
            	tipTag.setLong("lastSeen", tip.LastSeen());
            	tipTag.setInteger("showCnt", tip.showTipCnt);
            	

            	
            	nbt.setTag(tip.tipId, tipTag);
            }
            
//            NBTTagCompound TipSettings = new NBTTagCompound();
//            
//            TipSettings.setBoolean("showTips", Settings.showTips);
//            
//            nbt.setTag("Settings", TipSettings);
            
            CompressedStreamTools.writeCompressed(nbt, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new ReportedException(new CrashReport("An error occured while saving", new Throwable()));
        }
	}
	
	public void ReadNBTFile()
	{
		if (!datfile.exists()) 
        {
            MPUtils.logger.warn("File load canceled, file ("+ datfile.getAbsolutePath()  +")does not exist. This is normal for first run.");
            return;
        } else 
        {
        	MPUtils.logger.info("File load successful.");
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
            	// Settings.showTips = nbt.getCompoundTag("Settings").getBoolean("showTips");
            }
           
  
        } catch (IOException e) {
            e.printStackTrace();
        }
            
	}

}
