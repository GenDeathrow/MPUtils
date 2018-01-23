package com.gendeathrow.mputils.client.gui;

import net.minecraft.client.gui.GuiScreen;



public class TabWindowBase extends WindowBase
{

	public static int tabExpandSpeed = 8;

	protected int offsetX = 0;
	  protected int offsetY = 0;
	  public boolean open;
	  public boolean fullyOpen;
	
	  public int headerColor = 14797103;
	  public int subheaderColor = 11186104;
	  public int textColor = 0;
	  public int backgroundColor = 16777215;

	  protected int currentShiftX = 0;
	  protected int currentShiftY = 0;

	  public int minWidth = 22;
	  public int maxWidth = 124;
	  public int currentWidth = this.minWidth;

	  public int minHeight = 22;
	  public int maxHeight = 22;
	  public int currentHeight = this.minHeight;

	 public TabWindowBase(GuiScreen paramGuiBase, int paramInt1, int paramInt2) 
	 {
		super(paramGuiBase, paramInt1, paramInt2);
	 }
		 
	 public TabWindowBase setOffsets(int paramInt1, int paramInt2)
	 {
	    this.posX -= this.offsetX;
	    this.posY -= this.offsetY;
	    this.offsetX = paramInt1;
	    this.offsetY = paramInt2;
	    this.posX += this.offsetX;
	    this.posY += this.offsetY;
   		return this;
	 }

	 public TabWindowBase setPosition(int paramInt1, int paramInt2)
	 {
		 this.posX = (paramInt1 + this.offsetX);
		 this.posY = (paramInt2 + this.offsetY);
		 return this;
	 }


	 @Override
	 public void drawBackground(int paramInt1, int paramInt2, float paramFloat) 
	 {
		
	 }


	 @Override
	 public void drawForeground(int paramInt1, int paramInt2) 
	 {
		
	 }
	  

	  
}
