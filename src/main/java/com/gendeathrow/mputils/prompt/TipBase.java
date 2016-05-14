package com.gendeathrow.mputils.prompt;

import java.text.NumberFormat;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IJsonSerializable;

import com.gendeathrow.mputils.prompt.TipList.TipType;

public class TipBase 
{
    /** The Stat ID */
    public final String tipId;
    
    /** The Stat name */
    private final String tipName;
    
    public boolean isIndependent; 
    
    public ItemStack theItemStack;
    
    private Class <? extends IJsonSerializable > jelment;
    
    private static NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
    
    public TipType type;
    
    private long lastSeen;
    
    public boolean useOreDic;
    public String oreDicName;
    
    public int showTipMax = 2;
    public int showTipCnt = 0;
    

    public TipBase(String statIdIn, String NameIn, TipType typeIn)
    {
        this.tipId = statIdIn;
        this.tipName = NameIn;
        this.theItemStack = null;
        this.type = typeIn;
    }
    
    public TipBase(String statIdIn, String NameIn, Item itemin, Tip parent)
    {
        this.tipId = statIdIn;
        this.tipName = NameIn;
        this.theItemStack = new ItemStack(itemin);
    }
    
    public TipBase(String statIdIn, String NameIn, Block blockin, Tip parent)
    {
        this.tipId = statIdIn;
        this.tipName = NameIn;
        this.theItemStack = new ItemStack(blockin);
    }
    
    public void setSeen(long l)
    {
    	this.lastSeen = l;
    }
    
    public long LastSeen()
    {
    	return this.lastSeen;
    }
    
    public boolean shouldNotifiy()
    {
    	if(Minecraft.getSystemTime() - this.lastSeen > 600000 && this.showTipCnt <= this.showTipMax)
    	{
			setSeen(Minecraft.getSystemTime());
			this.showTipCnt++;
    		return true;
    	}
    	return false;
    }
    
     /**
     * Initializes the current stat as independent (i.e., lacking prerequisites for being updated) and returns the
     * current instance.
     */
    public TipBase initIndependentStat()
    {
        this.isIndependent = true;
        return this;
    }
    
    public String getStatName()
    {
        return tipName;
    }
    
    public ItemStack getItemStack()
    {
    	return this.theItemStack;	
    }
    
    public String toString()
    {
        return "Tip{id=" + this.tipId + ", nameId=" + this.tipName + ", type="+ this.type.toString() +" '}'";
    }
    
    public void ReadNBT(NBTTagCompound nbt)
    {
    	
    }
    
    public void SaveNBT(NBTTagCompound nbt)
    {
    	
    }
}
