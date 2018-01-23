package com.gendeathrow.mputils.client.gui.tablet;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.utils.RenderAssist;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class TabletWindow extends GuiScreen
{
	ResourceLocation tabletImage = new ResourceLocation(MPUtils.MODID , "textures/gui/tablet.png");
	int backgroundColor;
	
	protected int tabletwidth = 225;
	protected int tabletheight = 151;
	
	protected int tabletX = 0;
	protected int tabletY = 0;
	
	protected int openFully = 0;
	
	public TabletWindow()
	{
		super();
		
		this.backgroundColor =  RenderAssist.getColorFromRGBA(168, 228, 247, 255);
	}
	
	@Override
	public void initGui()
	{
		this.tabletX = (this.width - tabletwidth)/2;
		this.tabletY = (this.height - tabletheight)/2;
		
		super.initGui();
	}
	
	public TabletWindow(int backgroundColor)
	{
		super();
		this.backgroundColor = backgroundColor;
	}
	
	
    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int p_146278_1_)
    {
    	RenderAssist.bindTexture(tabletImage);
    	TabletWindow.drawRect(tabletX + 3 , tabletY + 3, tabletX + tabletwidth - 3, tabletY + tabletheight-3, backgroundColor);

    	RenderAssist.drawTexturedModalRectCustomSize(tabletX, tabletY, 0, 0, tabletwidth, tabletheight, tabletwidth, tabletheight);
    }

}
