package com.gendeathrow.mpbasic.client;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

public class GuiTextArea extends Gui
{
	
    private final FontRenderer fontRenderObj;

	public String text = "";
	int listScroll = 0;
	int rows = 0;
	
    /** if true the textbox can lose focus by clicking elsewhere on the screen */
    private boolean canLoseFocus = true;
    /** If this value is true along with isEnabled, keyTyped will process the keys. */
    private boolean isFocused;
    /** If this value is true along with isFocused, keyTyped will process the keys. */
    private boolean isEnabled = true;

    public int xPosition;
    public int yPosition;
    /** The width of this text field. */
    public int width;
    public int height;
    
    /** The current character index that should be used as start of the rendered text. */
    private int lineScrollOffset;
    private int cursorPosition;
    private int cursorCounter;
    /** other selection position, maybe the same as the cursor */
    private int selectionEnd;
    
    
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;

    /** True if this textbox is visible */
    private boolean visible = true;
    
    public GuiTextArea(FontRenderer fontRendererObj, int x, int y, int w, int h)
    {
    	fontRenderObj = fontRendererObj;
    	this.xPosition = x;
    	this.yPosition = y; 
    	this.width = w;
    	this.height = h;
     }
    
    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter()
    {
        ++this.cursorCounter;
    }
    
    @SuppressWarnings("unchecked")
  	public void initGui()
      {
            Keyboard.enableRepeatEvents(true);

           // maxRows = (sizeY - 48)/20;
  		
  	   		//cursorPosition = text.length();
  		
  //		RefreshColumns();
      }
    
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    
    public void moveCursorBy(int p_146182_1_)
    {
        this.setCursorPosition(this.cursorPosition + p_146182_1_);
    }
    
    public void setCursorPosition(int p_146190_1_)
    {
        this.cursorPosition = p_146190_1_;
        int j = this.text.length();

        if (this.cursorPosition < 0)
        {
            this.cursorPosition = 0;
        }

        if (this.cursorPosition > j)
        {
            this.cursorPosition = j;
        }

        this.setSelectionPos(this.cursorPosition);
    }
      
    public void setCursorPositionZero()
    {
        this.setCursorPosition(0);
    }
    
    public void setCursorPositionEnd()
    {
        this.setCursorPosition(this.text.length());
    }
    
    public int getCursorPosition()
    {
        return this.cursorPosition;
    }
    
    public void setSelectionPos(int p_146199_1_)
    {
        int j = this.text.length();

        if (p_146199_1_ > j)
        {
            p_146199_1_ = j;
        }

        if (p_146199_1_ < 0)
        {
            p_146199_1_ = 0;
        }
    }
    
    public void deleteFromCursor(int p_146175_1_)
    {
        if (this.text.length() != 0)
        {
            boolean flag = p_146175_1_ < 0;
            int j = flag ? this.cursorPosition + p_146175_1_ : this.cursorPosition;
            int k = flag ? this.cursorPosition : this.cursorPosition + p_146175_1_;
            String s = "";

            if (j >= 0)
            {
                s = this.text.substring(0, j);
            }

            if (k < this.text.length())
            {
                s = s + this.text.substring(k);
            }

            this.text = s;

            if (flag)
            {
                this.moveCursorBy(p_146175_1_);
            }
        }
    }
    

    public void keyTyped(char p_146201_1_, int p_146201_2_)
    {
    	if(!this.isFocused) return;
    	else
    	{
            switch (p_146201_1_)
            {
                case 1:
                    this.setCursorPositionEnd();
                    this.setSelectionPos(0);
                    return;
                case 22:
                    this.writeText(GuiScreen.getClipboardString());
                    return;
                default:
                    switch (p_146201_2_)
                    {
                        case 14:
                            this.deleteFromCursor(-1);
                            return;
                        case 28:
                        case 156:
                            this.writeText("\n");
                            return;
                        case 199:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setSelectionPos(0);
                            }
                            else
                            {
                                this.setCursorPositionZero();
                            }

                            return;
                        case 203:
                            this.moveCursorBy(-1);
                            return;
                        case 205:
                           this.moveCursorBy(1);
                            return;
                        case 207:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setSelectionPos(this.text.length());
                            }
                            else
                            {
                                this.setCursorPositionEnd();
                            }

                            return;
                        case 211:
                            this.deleteFromCursor(1);
                            return;
                        default:
                            this.writeText(ChatAllowedCharacters.filterAllowedCharacters(Character.toString(p_146201_1_)));
                            return;
                    }
            }
    	}
            
    }
    
    
    public void setText(String p_146180_1_)
    {
        this.text = p_146180_1_;
        this.setCursorPositionEnd();
    }
    
    public void writeText(String raw)
    {
    	
    	if(this.rows > this.getMaxRows()) return;
    	
    	
        String s1 = "";
        String s2 = raw;
        int i = this.cursorPosition;

        if (this.text.length() > 0)
        {
            s1 = s1 + this.text.substring(0, i);
        }

        int l;
        s1 = s1 + s2;
        l = s2.length();

        if (this.text.length() > 0 && i < this.text.length())
        {
            s1 = s1 + this.text.substring(i);
        }

        this.text = s1;
        this.moveCursorBy(l);
    }
    
    /**
     * Args: x, y, buttonClicked
     */
    public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_)
    {
        boolean flag = p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height;

        if (this.canLoseFocus)
        {
            this.setFocused(flag);
        }

//        if (this.isFocused && p_146192_3_ == 0)
//        {
//            int l = p_146192_1_ - this.xPosition;
//
//            String s = this.fontRenderObj.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
//            this.setCursorPosition(this.fontRenderObj.trimStringToWidth(s, l).length() + this.lineScrollOffset);
//        }
    }
    

    /**
     * Sets focus to this gui element
     */
    public void setFocused(boolean p_146195_1_)
    {
        if (p_146195_1_ && !this.isFocused)
        {
            this.cursorCounter = 0;
        }

        this.isFocused = p_146195_1_;
    }

    /**
     * Getter for the focused field
     */
    public boolean isFocused()
    {
        return this.isFocused;
    }

    public void setEnabled(boolean p_146184_1_)
    {
        this.isEnabled = p_146184_1_;
    }
    
    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int getWidth()
    {
        return  this.width;
    }
    
    /**
     * Returns the contents of the textbox
     */
    public String getText()
    {
        return this.text;
    }
    
    int currentLine = 1;
    
    public void moveLine(int value)
    {
    	
    }
    
    public void setLinePosEnd()
    {
    	
    }
        
    /**
     * Draws the textbox
     */
    public void drawTextBox()
    {
        if (this.getVisible())
        {
        	
            drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
            drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);


        	int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            //String s = this.fontRenderObj.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            
            @SuppressWarnings("unchecked")
			List<String> s = this.fontRenderObj.listFormattedStringToWidth(this.getText(), this.getWidth());
            
            boolean flag = j >= 0 && j <= getText().length();
            boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            
            this.rows = s.size();
            
            int l = this.xPosition;
            int i1 = this.yPosition;
            int j1 = l;

            
         // this.fontRenderObj.drawSplitString(this.getText(), this.xPosition, this.yPosition, this.width, java.awt.Color.white.getRGB());
            
          boolean flag2 = this.cursorPosition < this.text.length();
     
          int k1 = j1;
          
          int lineRow = 1;

          
          int row = 0;
          int charCnt = 0;
          int cursorY = this.yPosition;
          boolean cursorFlag = false;
          
          for(String line : s)
          {
        	  i1 = this.yPosition  + (row * this.fontRenderObj.FONT_HEIGHT);

        	  k1 = this.fontRenderObj.drawString(line, this.xPosition, i1, java.awt.Color.white.getRGB());
        	  
        	  if((this.cursorPosition) >= charCnt && this.cursorPosition <= charCnt+line.length())
        	  {
        		  int charWidth = (this.cursorPosition - charCnt) < 0 ? 0 : (this.cursorPosition - charCnt); 
        		 
        		  k1 = this.xPosition + this.fontRenderObj.getStringWidth(line.substring(0, charWidth));
        		  
        		  cursorY = this.yPosition + (row * this.fontRenderObj.FONT_HEIGHT);
        		  cursorFlag = true;
        		  
        	  }
        	  else if(!cursorFlag)
        	  {
        		  cursorY = i1;
        	  }
        	  
        	  charCnt += line.length();
        	  
           	  row++;
          }
          
          
        
          if (flag1)
          {
              if (flag2)
              {
                  Gui.drawRect(k1, cursorY - 1, k1 + 1, cursorY + 1 + this.fontRenderObj.FONT_HEIGHT, -3092272);
              }
              else
              {
                  this.fontRenderObj.drawStringWithShadow("_", k1, cursorY, i);
              }
          }

        }
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    private void drawCursorVertical(int startX, int startY, int endX, int endY)
    {
        int i1;

        if (startX < endX)
        {
            i1 = startX;
            startX = endX;
            endX = i1;
        }

        if (startY < endY)
        {
            i1 = startY;
            startY = endY;
            endY = i1;
        }

        if (endX > this.xPosition + this.width)
        {
            endX = this.xPosition + this.width;
        }

        if (startX > this.xPosition + this.width)
        {
            startX = this.xPosition + this.width;
        }
        
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexbuffer.pos((double)startX, (double)endY, 0.0D).endVertex();
        vertexbuffer.pos((double)endX, (double)endY, 0.0D).endVertex();
        vertexbuffer.pos((double)endX, (double)startY, 0.0D).endVertex();
        vertexbuffer.pos((double)startX, (double)startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    
    /**
     * returns true if this textbox is visible
     */
    public boolean getVisible()
    {
        return this.visible;
    }

    /**
     * Sets whether or not this textbox is visible
     */
    public void setVisible(boolean p_146189_1_)
    {
        this.visible = p_146189_1_;
    }
    
    
    //TODO
    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    public int getMaxRows()
    {
        return this.height/this.fontRenderObj.FONT_HEIGHT;
    }

}
