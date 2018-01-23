package com.gendeathrow.mputils.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.gendeathrow.mputils.client.settings.QuickCommandManager.CommandElement;
import com.gendeathrow.mputils.core.MPUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCommandWand extends Item
{

	CommandElement command;
	
	
    public ItemCommandWand() 
    {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName("itemcommandwand");
        this.setCreativeTab(CreativeTabs.TOOLS);
        
    }
	
	
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	if(MPUtils.proxy.isClient())
    	{
        	if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        	{
        		//open gui
        	}
    		Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(command.getCommand());
            if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(playerIn, command.getCommand()) != 0);
    	}
    	
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }
    
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
    	tooltip.add("Hold Shift + Click to Edit");
    }

}
