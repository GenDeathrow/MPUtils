package com.gendeathrow.tipalert.prompt.Tips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

import org.apache.logging.log4j.Level;

import com.gendeathrow.tipalert.core.TipAlert;
import com.gendeathrow.tipalert.prompt.TipList.TipType;
import com.google.gson.JsonObject;

public class TipEntity extends Tip
{
	public String entityID;
	
	private static Entity renderEntity = null;
	
	
	public TipEntity(String statIdIn, String nameIn, TipType typeIn) 
	{
		super(statIdIn, nameIn, typeIn);
	}
	
	public TipEntity(String statIdIn, String nameIn, TipType typeIn, String desc) 
	{
		super(statIdIn, nameIn, typeIn);
		this.setInfo(statIdIn, desc);
	}
	
	
	
	public void drawNotification(Minecraft mc, int x, int y)
	{
		super.drawNotification(mc, x, y);
		
		if(this.renderEntity == null || EntityList.getEntityString(renderEntity) != this.entityID)
		{
			this.renderEntity = EntityList.createEntityByName(this.entityID, mc.theWorld);
		}
		else 
		{
			
//			RenderHelper.enableGUIStandardItemLighting();
//			
//			GlStateManager.disableLighting();
//			GlStateManager.enableRescaleNormal();
//			GlStateManager.enableColorMaterial();
//			GlStateManager.enableLighting();
//				mc.getRenderManager().doRenderEntity(renderEntity, 0, 0, 0, 0, 1.0f, false);
//			GlStateManager.disableLighting();
//			GlStateManager.depthMask(true);
//			GlStateManager.enableDepth();
		}
	    
	}
	
	public Tip ReadFromJson(JsonObject json)
    {
    	this.setDesc(json.get("desc").getAsString());
    	
    	if(json.has("longdesc")) this.longDesc = json.get("longdesc").getAsString();
    	else this.longDesc = this.getDescription();
    	
    	if(json.has("entityID"))
    	{
    		entityID = json.get("entityID").getAsString();
    		
    		
    		if(EntityList.NAME_TO_CLASS.containsKey(entityID)) 
    		{
    			TipAlert.logger.log(Level.ERROR, "Cant find Entity "+ entityID +" , Not Loading this Tip: "+ this.getTitle());
    			return null;
    		}
    		
    	}

    	return this;
    }
	
}
