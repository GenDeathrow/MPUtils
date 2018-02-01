package com.gendeathrow.mpbasic.common.infopanel;

import javax.annotation.Nullable;

import com.gendeathrow.mpbasic.api.IInfoPanelData;
import com.gendeathrow.mpbasic.common.SimpleCapabilityProvider;
import com.gendeathrow.mpbasic.core.MPBasic;
import com.gendeathrow.mpbasic.network.InfoPanelUpdate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class CapabilityInfoPanel {

	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(IInfoPanelData.class)
	public static final Capability<IInfoPanelData> INFO_PANEL_CAPABILITY = null;

	/**
	 * The default {@link EnumFacing} to use for this capability.
	 */
	public static final EnumFacing DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(MPBasic.MODID, "infopanel");

	public static void register() {
		CapabilityManager.INSTANCE.register(IInfoPanelData.class, new Capability.IStorage<IInfoPanelData>() {

			@Override
			public NBTBase writeNBT(Capability<IInfoPanelData> capability, IInfoPanelData instance, EnumFacing side) {
				return instance.WriteNBT(new NBTTagCompound());
			}

			@Override
			public void readNBT(Capability<IInfoPanelData> capability, IInfoPanelData instance, EnumFacing side, NBTBase nbt) {
				instance.ReadNBT((NBTTagCompound)nbt);
			}
		},() -> new InfoPanelPlayerExtraData(null));
	}

	
	@Nullable
	public static IInfoPanelData getInfoPanelData(EntityPlayer player) {
		return player.hasCapability(INFO_PANEL_CAPABILITY, DEFAULT_FACING) ? player.getCapability(INFO_PANEL_CAPABILITY, DEFAULT_FACING) : null;
 	}
	
	
	public static void sendToPlayer(String panelID, EntityPlayerMP player) {
		MPBasic.network.sendTo(new InfoPanelUpdate(panelID), player);
	}
	

	public static ICapabilityProvider createProvider(InfoPanelPlayerExtraData infopanel) {
		return new SimpleCapabilityProvider<>(INFO_PANEL_CAPABILITY, DEFAULT_FACING, infopanel);
	}

	
	@Mod.EventBusSubscriber
	public static class EventHandler {
	
		@SubscribeEvent
		public static void onConstructing(AttachCapabilitiesEvent<Entity> event) {
			if(event.getObject() instanceof EntityPlayer){
				if(!event.getObject().hasCapability(INFO_PANEL_CAPABILITY, null))
					event.addCapability(ID, createProvider(new InfoPanelPlayerExtraData(null)));
			}
		}
	
		@SubscribeEvent
		public static void onRespawn(PlayerEvent.Clone event) {
			
			if(event.isWasDeath()) {
				IInfoPanelData OriginalCap = event.getOriginal().getCapability(INFO_PANEL_CAPABILITY, null);	
				NBTTagCompound orginalNBT = new NBTTagCompound();
				OriginalCap.ReadNBT(orginalNBT);
				event.getEntity().getCapability(INFO_PANEL_CAPABILITY, null).WriteNBT(orginalNBT);	
			}
		
		}
	}
}
