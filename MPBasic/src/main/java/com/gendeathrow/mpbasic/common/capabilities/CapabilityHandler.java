package com.gendeathrow.mpbasic.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.mpbasic.common.capabilities.player.MPBasic_PlayerDataProvider;
import com.gendeathrow.mputils.core.MPUtils;

public class CapabilityHandler
{
    public static final ResourceLocation PLAYERSDATA = new ResourceLocation(MPUtils.MODID, "mpbasic_playersdata");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent event)
    {
//    	// HardCoded fakeplayer for jei
//        if (!(event.getObject() instanceof EntityPlayer) || (event.getObject() instanceof FakePlayer) || (event.getObject().getClass().getName().equals("mezz.jei.util.FakeClientPlayer"))) return;
//
//        System.out.println("wahat");
//        EntityPlayer player = (EntityPlayer) event.getObject();
////       if(!event.getCapabilities().containsKey(new ResourceLocation(MPUtils.MODID, "mpbasic_playersdata")))
//        	event.addCapability(new ResourceLocation(MPUtils.MODID, "mpbasic_playersdata"), new MPBasic_PlayerDataProvider());
    }
}