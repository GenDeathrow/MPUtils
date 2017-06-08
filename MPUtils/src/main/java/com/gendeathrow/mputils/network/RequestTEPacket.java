package com.gendeathrow.mputils.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.commands.common.MP_InContainer;
import com.gendeathrow.mputils.core.MPUtils;


public class RequestTEPacket implements IMessage 
{
	public int requestID = 0;
	public NBTTagCompound tags = new NBTTagCompound();
	public String[] args;
		
	public RequestTEPacket(){	}

	public RequestTEPacket(NBTTagCompound tags, String[] arg) // Use PacketDataTypes to instantiate new packets
	{
		this.requestID = 0;
		this.tags = tags;
		this.args = arg;
	}
		
	protected RequestTEPacket(int requestID, NBTTagCompound tags, String[] arg) // Use PacketDataTypes to instantiate new packets
	{
		this.requestID = requestID;
		this.tags = tags;
		this.args = arg;
	}
		
		
	public static RequestTEPacket requestTEItemDump(BlockPos pos, String[] args)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setDouble("bposX", pos.getX());
		nbt.setDouble("bposY", pos.getY());
		nbt.setDouble("bposZ", pos.getZ());
		return new RequestTEPacket(1, nbt, args);
	}
		
		
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		requestID = ByteBufUtils.readVarInt(buf, 3);
		tags = ByteBufUtils.readTag(buf);
		
		int length = buf.readInt(); 
		int count = 0; 
		args = new String[length];
		
		while (count != length) 
		{ 
			args[count] = ByteBufUtils.readUTF8String(buf); 
			count++; 
		} 
	
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeVarInt(buf, requestID, 3);
		ByteBufUtils.writeTag(buf, tags);
		
		buf.writeInt(args.length); 
		for (String arg : args) 
		{ 
		  ByteBufUtils.writeUTF8String(buf, arg); 
		} 
	}
		
	/////////////////////////////////////////////
	// Server Message
	////////////////////////////////////////////
	public static class ServerHandler implements IMessageHandler<RequestTEPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final RequestTEPacket message, final MessageContext ctx) 
		{
			if(message == null || message.tags == null)
			{
				MPUtils.logger.log(Level.ERROR, "A critical NPE error occured during while handling a Hatchery packet server side", new NullPointerException());
				return null;
			}
			
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
		          
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{

					EntityPlayerMP sender = ctx.getServerHandler().player;
					NBTTagCompound nbt = message.tags;
							
					if(message.requestID == 1) 
					{
						//System.out.println("Server Recieved:"+ nbt.getDouble("bposX")+","+ nbt.getDouble("bposY")+","+ nbt.getDouble("bposZ"));
					   	     
					   			
						TileEntity te = sender.world.getTileEntity(new BlockPos(nbt.getDouble("bposX"), nbt.getDouble("bposY"), nbt.getDouble("bposZ")));
						
						if(te == null) return;
								
						NBTTagCompound requestNBT = new NBTTagCompound();
									
						requestNBT = te.writeToNBT(requestNBT);
										
						requestNBT.setDouble("bposX", te.getPos().getX());
						requestNBT.setDouble("bposY", te.getPos().getY());
						requestNBT.setDouble("bposZ", te.getPos().getZ());
										
						MPUtils.network.sendTo(new RequestTEPacket(requestNBT, message.args), sender);
									
									
					}				
					
				}
			});
		            
				
			return null;
		}
			
			
	}
		

	///////////////////////////////////////////
	// Client Handler
	///////////////////////////////////////////
	public static class ClientHandler implements IMessageHandler<RequestTEPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final RequestTEPacket message, MessageContext ctx) 
		{
			if(message == null || message.tags == null)
			{
				MPUtils.logger.log(Level.ERROR, "A critical NPE error occured during while handling a Hatchery packet Client side", new NullPointerException());
				return null;
			}
				
			IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					NBTTagCompound nbt = message.tags;
					if(message.requestID == 0) 
					{
						//System.out.println("Client Recieved"+ nbt.getDouble("bposX")+","+ nbt.getDouble("bposY")+","+ nbt.getDouble("bposZ"));

						BlockPos pos = new BlockPos(nbt.getDouble("bposX"), nbt.getDouble("bposY"), nbt.getDouble("bposZ"));
						TileEntity te = Minecraft.getMinecraft().player.world.getTileEntity(pos);
						
						if(te == null) return;
						
						te.readFromNBT(nbt);
						
						MP_InContainer.copyInventoryClipboard(te, message.args);
					}
				}
			});
			return null;
		}
			
	}

}