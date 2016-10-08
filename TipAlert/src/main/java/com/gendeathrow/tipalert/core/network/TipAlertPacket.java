package com.gendeathrow.tipalert.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.gendeathrow.tipalert.core.TipAlert;

public class TipAlertPacket implements IMessage 
{
	public int requestID = 0;
	public NBTTagCompound tags = new NBTTagCompound();


	public TipAlertPacket(){}
	
	public TipAlertPacket(NBTTagCompound tags) // Use PacketDataTypes to instantiate new packets
	{
		this.requestID = 0;
		this.tags = tags;
	}
	
	public TipAlertPacket(int requestID, NBTTagCompound tags) // Use PacketDataTypes to instantiate new packets
	{
		this.requestID = requestID;
		this.tags = tags;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		requestID = ByteBufUtils.readVarInt(buf, 3);
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeVarInt(buf, requestID, 3);
		ByteBufUtils.writeTag(buf, tags);
	}

	///////////////////////////////////////////
	// Client Handler
	///////////////////////////////////////////
	public static class ClientHandler implements IMessageHandler<TipAlertPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final TipAlertPacket message, MessageContext ctx) 
		{
			
			if(message == null || message.tags == null)
			{
				TipAlert.logger.error("A critical NPE error occured during while handling a Hatchery packet Client side", new NullPointerException());
				return null;
			}
			
			
		    IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
            mainThread.addScheduledTask(new Runnable() 
            {
                @Override
                public void run() 
                {


        			NBTTagCompound nbt = message.tags;
        			// 0 is To tipManager
        			if(message.requestID == 0) 
        			{
        				TipAlert.tipManager.OnPacket(nbt);
        			}
        			
        			

                }
            });
            
			return null;
		}
	}
	
	/////////////////////////////////////////////
	// Server Message
	////////////////////////////////////////////
	public static class ServerHandler implements IMessageHandler<TipAlertPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final TipAlertPacket message, final MessageContext ctx) 
		{
			if(message == null || message.tags == null)
			{
				TipAlert.logger.error("A critical NPE error occured during while handling a Hatchery packet server side", new NullPointerException());
				return null;
			}
			
			
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
	          
			mainThread.addScheduledTask(new Runnable() 
				{
					@Override
	                public void run() 
					{

						EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
						NBTTagCompound nbt = message.tags;
						
						if(message.requestID == 1) 
						{

						}				
						
	                }
	            });
			
			return null;
		}
	}
		
}
