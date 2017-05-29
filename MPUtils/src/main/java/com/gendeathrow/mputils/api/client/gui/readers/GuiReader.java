package com.gendeathrow.mputils.api.client.gui.readers;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.inventory.container.ContainerNBTBase;
import com.gendeathrow.mputils.utils.RenderAssist;

/**
 * @author thislooksfun
 */
public class GuiReader extends GuiContainer
{
	private static final ResourceLocation img = new ResourceLocation(MPUtils.MODID, "textures/gui/reader.png");
	
	private ArrayList<String> displayStrings = new ArrayList<String>();
	private int start = 0;
	
	public GuiReader(EntityPlayer player)
	{
		super(null);
		this.xSize = 224;
		this.ySize = 233;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(img);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);
		
		drawRect(5, 5, this.xSize - 5, 146, RenderAssist.getColorFromRGBA(0, 11, 121, 255));
		drawRect(5, 5, this.xSize - 5, 17, RenderAssist.getColorFromRGBA(207, 0, 0, 255));
		
		ItemStack stack = this.inventorySlots.getSlot(0).getStack();
		this.drawString("Item: " + (stack == null ? "" : (stack.getCount() + " * " + stack.getDisplayName())), 7, 7);
		if (stack != null)
		{
			if (stack.hasTagCompound())
			{
				this.start = this.start < 0 ? 0 : (this.displayStrings.size() < 14 ? 0 : (this.start > this.displayStrings.size() - 14 ? this.displayStrings.size() - 14 : this.start));
				
				this.checkDisplayStrings();
				if (this.displayStrings.size() > 14)
				{
					for (int i = 0; i < 14; i++)
					{
						this.drawString(this.displayStrings.get(this.start + i), 7, 19 + (i * 9));
					}
					
					this.drawScrollBar();
				} else
				{
					for (int i = 0; i < this.displayStrings.size(); i++)
					{
						this.drawString(this.displayStrings.get(i), 7, 19 + (i * 9));
					}
				}
			} else
			{
				this.drawString("No NBT data!", 7, 19);
			}
		}
	}
	
	private void drawScrollBar()
	{
		drawRect(this.xSize - 9, 17, this.xSize - 5, 146, RenderAssist.getColorFromRGBA(0, 150, 0, 255));
		
		int height = 15;
		float percent = ((float)this.start / (this.displayStrings.size() - 14));
		int offset = (int)(percent * (127 - height));
		
		drawRect(this.xSize - 8, 18 + offset, this.xSize - 6, 18 + offset + height, RenderAssist.getColorFromRGBA(0, 255, 124, 255));
	}
	
	private void checkDisplayStrings()
	{
//		if (((ContainerNBTBase)this.inventorySlots).tile.triggerBool)
//		{
//			this.populateDisplayStrings();
//			((ContainerNBTBase)this.inventorySlots).tile.triggerBool = false;
//		}
	}
	
	private void populateDisplayStrings()
	{
		this.displayStrings.clear();
		
		Slot slot = this.inventorySlots.getSlot(0);
		if (slot == null) return; //Can't find slot
		ItemStack stack = slot.getStack();
		if (stack == null || !stack.hasTagCompound()) return; //Slot is empty, or has no NBT data
		
		for (String s : formatTag(stack.getTagCompound()))
		{
			this.displayStrings.add(s);
		}
	}
	
	private ArrayList<String> formatTag(NBTTagCompound tag)
	{
		String fullStr = tag.toString();
		ArrayList<String> arr = new ArrayList<String>();
		int indentLevel = 0;
		boolean isInString = false;

//		fullStr = fullStr.substring(1, fullStr.length()-1).replace("\n", "\\n");
		fullStr = fullStr.replace("\n", "\\n");
		String s = "";
		
		for (int i = 0; i < fullStr.length(); i++)
		{
			try
			{
				char c = fullStr.charAt(i); //Current char
				char c2 = (i > 0 ? fullStr.charAt(i - 1) : '.'); //Last char
				
				if (c == '"')
				{
					isInString = !isInString;
				}
				
				if (isInString)
				{
					if (c2 == '\\')
					{
						s += c + "\u00a7r";
					} else if (c == '\\')
					{
						s += "\u00a7d" + c;
					} else
					{
						s += c;
					}
				} else
				{
					if (c == '{' || c == '[')
					{
						if (s.length() > 0)
						{
							arr.add(repeat(" ", indentLevel) + s);
						}
						
						arr.add(repeat(" ", indentLevel) + c);
						
						indentLevel++;
						s = "";
					} else if (c == '}' || c == ']')
					{
						if (s.length() > 0)
						{
							arr.add(repeat(" ", indentLevel) + s);
						}
						indentLevel--;
						arr.add(repeat(" ", indentLevel) + c);
						s = "";
					} else if (c == ',')
					{
						if (s.length() > 0)
						{
							arr.add(repeat(" ", indentLevel) + s);
						}
						s = "";
					} else
					{
						s += (c == ':' ? c + " " : c);
					}
				}
			} catch (StringIndexOutOfBoundsException e)
			{
				e.printStackTrace();
			}
			
			if (indentLevel < 0)
			{
				indentLevel = 0; //Just in case
			}
		}
		
		return arr;
	}
	
	private String repeat(String s, int cnt)
	{
		String s2 = "";
		for (int i = 0; i < cnt; i++)
		{
			s2 += s;
		}
		
		return s2;
	}
	
	private void drawString(String s, int left, int top)
	{
		this.drawString(s, left, top, this.xSize - 20);
	}
	private void drawString(String s, int left, int top, int maxWidth)
	{
		this.drawString(this.fontRenderer, s, left, top, Color.WHITE.getRGB());
	}
	
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		
		if (i != 0 && this.displayStrings.size() > 14)
		{
			if (i > 0)
			{
				start -= 1;
			}
			if (i < 0)
			{
				start += 1;
			}
			
			if (this.start < 0)
			{
				this.start = 0;
			}
			
			if (this.start > this.displayStrings.size() - 14)
			{
				this.start = this.displayStrings.size() - 14;
			}
		}
	}
}