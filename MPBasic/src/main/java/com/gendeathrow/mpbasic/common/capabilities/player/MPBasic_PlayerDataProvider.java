package com.gendeathrow.mpbasic.common.capabilities.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class MPBasic_PlayerDataProvider implements ICapabilitySerializable<NBTBase>
{
	
    @CapabilityInject(IMPBasic_PlayerData.class)
    public static final Capability<IMPBasic_PlayerData> PLAYERDATA = null;
    
    private IMPBasic_PlayerData instance = PLAYERDATA.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		  return capability == PLAYERDATA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		 return capability == PLAYERDATA ? PLAYERDATA.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT()   
	{
		return PLAYERDATA.getStorage().writeNBT(PLAYERDATA, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) 
	{
		PLAYERDATA.getStorage().readNBT(PLAYERDATA, this.instance, null, nbt);
	}



}
