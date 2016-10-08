package com.gendeathrow.mputils.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;


public abstract class WindowBase 
{
	  protected GuiScreen gui;
	  protected ResourceLocation texture;
	  protected int posX;
	  protected int posY;
	  protected int sizeX;
	  protected int sizeY;
	  protected int texW = 256;
	  protected int texH = 256;
	  protected String name;
	  private boolean visible = true;
	  private boolean enabled = true;
	  
	  
	  public WindowBase(GuiScreen gui, int x, int y)
	  {
	    this.gui = gui;
	    this.posX = x;
	    this.posY = y;
	  }
	  
	  public WindowBase (GuiScreen gui, int x, int y, int sizeX, int sizeY)
	  {
	    this.gui = gui;
	    this.posX = x;
	    this.posY = y;
	    this.sizeX = sizeX;
	    this.sizeY = sizeY;
	  }

	  public WindowBase setName(String paramString)
	  {
	    this.name = paramString;
	    return this;
	  }

	  public WindowBase setPosition(int paramInt1, int paramInt2)
	  {
	    this.posX = paramInt1;
	    this.posY = paramInt2;
	    return this;
	  }

	  public WindowBase setSize(int sizeX, int sizeY)
	  {
	    this.sizeX = sizeX;
	    this.sizeY = sizeY;
	    return this;
	  }

	  public WindowBase setTexture(String paramString, int paramInt1, int paramInt2)
	  {
	    this.texture = new ResourceLocation(paramString);
	    this.texW = paramInt1;
	    this.texH = paramInt2;
	    return this;
	  }

	  public final WindowBase setVisible(boolean paramBoolean)
	  {
	    this.visible = paramBoolean;
	    return this;
	  }

	  public boolean isVisible()
	  {
	    return this.visible;
	  }

	  public final WindowBase setEnabled(boolean paramBoolean)
	  {
	    this.enabled = paramBoolean;
	    return this;
	  }

	  public boolean isEnabled()
	  {
	    return this.enabled;
	  }

	  public void update(int paramInt1, int paramInt2)
	  {
	    update();
	  }

	  public void update()
	  {
		  
	  }

	  public abstract void drawBackground(int paramInt1, int paramInt2, float paramFloat);

	  public abstract void drawForeground(int paramInt1, int paramInt2);

	  @SuppressWarnings("rawtypes")
	  public void addTooltip(List paramList)
	  {
		  
	  }

	  public void drawModalRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
	  {
	    //this.gui.drawSizedModalRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
	  }

}
