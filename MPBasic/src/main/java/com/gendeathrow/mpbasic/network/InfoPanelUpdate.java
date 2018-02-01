package com.gendeathrow.mpbasic.network;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mpbasic.api.IInfoPanelData;
import com.gendeathrow.mpbasic.client.gui.GuiInfoPanel;
import com.gendeathrow.mpbasic.common.infopanel.CapabilityInfoPanel;
import com.gendeathrow.mpbasic.configs.InfoPanelConfigHandler;
import com.gendeathrow.mputils.core.MPUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InfoPanelUpdate implements IMessage 
{
	
	private String infopanelPage;
	private boolean hasSeenPage = false;
	
	public InfoPanelUpdate(){	}

	public InfoPanelUpdate(String infoPanelPageIn) // Use PacketDataTypes to instantiate new packets
	{
		infopanelPage = infoPanelPageIn;
	}
	
	public InfoPanelUpdate(String infoPanelPageIn, boolean hasSeenIn) // Use PacketDataTypes to instantiate new packets
	{
		infopanelPage = infoPanelPageIn;
		hasSeenPage = hasSeenIn;
	}
		
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		infopanelPage = ByteBufUtils.readUTF8String(buf);
		hasSeenPage = ByteBufUtils.readVarShort(buf) == 1 ? true : false;
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		  ByteBufUtils.writeUTF8String(buf, infopanelPage); 
		  ByteBufUtils.writeVarShort(buf, hasSeenPage ? 1 : 0);
	}
		
	///////////////////////////////////////////
	// Client Handler
	///////////////////////////////////////////
	public static class ClientHandler implements IMessageHandler<InfoPanelUpdate, IMessage> 
	{

		@Override
		public IMessage onMessage(final InfoPanelUpdate message, MessageContext ctx) 
		{
			if(message == null || message.infopanelPage == null)
			{
				MPUtils.logger.log(Level.ERROR, "A critical NPE error occured during while handling a MPBasic Tools packet Client side", new NullPointerException());
				return null;
			}
				
			IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					if(InfoPanelConfigHandler.PAGES.containsKey(message.infopanelPage))
						Minecraft.getMinecraft().displayGuiScreen(new GuiInfoPanel(null, InfoPanelConfigHandler.PAGES.get(message.infopanelPage), message.hasSeenPage));
				}
			});
			return null;
		}
			
	}
	
	
	///////////////////////////////////////////
	// Server Handler
	///////////////////////////////////////////
	public static class ServerHandler implements IMessageHandler<InfoPanelUpdate, IMessage> 
	{

		@Override
		public IMessage onMessage(final InfoPanelUpdate message, MessageContext ctx) 
		{
			if(message == null || message.infopanelPage == null)
			{
				MPUtils.logger.log(Level.ERROR, "A critical NPE error occured during while handling a MPBasic Tools packet Server side", new NullPointerException());
				return null;
			}
				
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					IInfoPanelData cap = CapabilityInfoPanel.getInfoPanelData(ctx.getServerHandler().player);
					
					if(cap != null) {
						cap.addBookPanel(message.infopanelPage);
					}
				}
			});
			return null;
		}
			
	}


}