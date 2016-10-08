package com.gendeathrow.tipalert.prompt.Tips;

import java.text.NumberFormat;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.gendeathrow.mputils.client.gui.notification.NotificationBase;
import com.gendeathrow.tipalert.core.TipSettings;
import com.gendeathrow.tipalert.prompt.TipList;
import com.gendeathrow.tipalert.prompt.TipList.TipType;


public class TipBase extends NotificationBase 
{
	 private static final ResourceLocation tipsBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
	 
	    /** The Stat ID */
	    public final String tipId;
	    
	    /** The Stat name */
	   // private final String tipName;
	    
	    public boolean isIndependent; 
	    
	    public ItemStack theItemStack;
	    
	    private Class <? extends IJsonSerializable > jelment;
	    
	    private static NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
	    
	    public TipType type;
	  
	    public boolean useOreDic;
	    public String oreDicName;
	    
	    public int showTipMax = TipSettings.maxTipCnt;
	    public int showTipCnt = 0;
	    
	   // private RenderItem renderItem = new RenderItem();
	    

	    public TipBase(String statIdIn, String NameIn, TipType typeIn)
	    {
	        super(10,0);
	        
	    	this.tipId = statIdIn;
	        this.theItemStack = null;
	        this.type = typeIn;
	        this.setInfo(NameIn, "");
	    }
	    
	    public TipBase(String statIdIn, String NameIn, Item itemin, Tip parent)
	    {
	    	super(10,0);
	    	
	        this.tipId = statIdIn;
	        this.theItemStack = new ItemStack(itemin);
	        if(this.theItemStack != null) this.setTextOffset(30);
	        this.setInfo(NameIn, "");
	    }
	    
	    public TipBase(String statIdIn, String NameIn, Block blockin, Tip parent)
	    {
	    	super(10,0);
	        this.tipId = statIdIn;
	        this.theItemStack = new ItemStack(blockin);
	        if(this.theItemStack != null) this.setTextOffset(30);
	        this.setInfo(NameIn, "");
	    }
	    
	    @Override
	    public boolean shouldNotifiy()
	    {
	    	if(Minecraft.getSystemTime() - this.lastSeen() > 600000 && this.showTipCnt < this.showTipMax)
	    	{
				setSeen(Minecraft.getSystemTime());
				this.showTipCnt++;
	    		return true;
	    	}
	    	return false;
	    }
	    
	    @Override
	    public ResourceLocation getBGTexture()
	    {
	    	return tipsBg;
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
	    
	    public ItemStack getItemStack()
	    {
	    	return this.theItemStack;	
	    }
	    
	    public String toString()
	    {
	        return "Tip{id=" + this.tipId + ", nameId=" + this.title + ", type="+ this.type.toString() +" '}'";
	    }
	    
	    
		public void drawNotification(Minecraft mc, int x, int y)
		{
			super.drawNotification(mc, x, y);
			
			if(this.theItemStack != null)
			{
				RenderHelper.enableGUIStandardItemLighting();
				
				GlStateManager.disableLighting();
				GlStateManager.enableRescaleNormal();
				GlStateManager.enableColorMaterial();
				GlStateManager.enableLighting();
					mc.getRenderItem().renderItemAndEffectIntoGUI(this.theItemStack,  x + 8, y + 8);
				GlStateManager.disableLighting();
				GlStateManager.depthMask(true);
				GlStateManager.enableDepth();
			}
		    
		}

	    
	    public void ReadNBT(NBTTagCompound nbt)
	    {
	    	
	    }
	    
	    public void SaveNBT(NBTTagCompound nbt)
	    {
	    	
	    }
}
