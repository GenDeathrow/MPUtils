package com.gendeathrow.mpbasic.common.capabilities.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MPBasic_PlayersDataStorage implements IStorage<IMPBasic_PlayerData>
{

    @Override
    public NBTBase writeNBT(Capability<IMPBasic_PlayerData> capability, IMPBasic_PlayerData instance, EnumFacing side)
    {
    	NBTTagCompound compound = new NBTTagCompound();
           compound.setLong("playersTimer", instance.getElapsed());
           
           System.out.println("save");
        return compound;
    }

    @Override
    public void readNBT(Capability<IMPBasic_PlayerData> capability, IMPBasic_PlayerData instance, EnumFacing side, NBTBase nbt)
    {
    	NBTTagCompound compound = (NBTTagCompound) nbt;
    	
    	instance.setElapsed(compound.getLong("playersTimer"));
       
    }
}
