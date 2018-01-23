package com.gendeathrow.mpbasic.network;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.core.MPUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InfoPanelUpdate implements IMessage 
{
	
	private String infopanelPage;
	
	public InfoPanelUpdate(){	}

	public InfoPanelUpdate(String infoPanelPageIn) // Use PacketDataTypes to instantiate new packets
	{
		infopanelPage = infoPanelPageIn;
	}
		
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		infopanelPage = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		  ByteBufUtils.writeUTF8String(buf, infopanelPage); 
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
					
				}
			});
			return null;
		}
			
	}

}