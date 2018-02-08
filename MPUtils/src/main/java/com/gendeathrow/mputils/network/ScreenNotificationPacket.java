package com.gendeathrow.mputils.network;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.client.gui.ScreenNotification;
import com.gendeathrow.mputils.core.MPUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ScreenNotificationPacket implements IMessage 
{
	private int count;
	private ArrayList<String> lines = new ArrayList<String>();
	private String soundFile;
	
	public ScreenNotificationPacket(){	}

	
	public ScreenNotificationPacket(ArrayList<String> linesIn) // Use PacketDataTypes to instantiate new packets
	{
		this(linesIn, SoundEvents.ENTITY_PLAYER_LEVELUP.getRegistryName().toString());
	}
	
	public ScreenNotificationPacket(ArrayList<String> linesIn, String sound) // Use PacketDataTypes to instantiate new packets
	{
		lines = linesIn;
		count = linesIn.size();
		soundFile = sound;
	}
		
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		lines.clear();
		
		soundFile = ByteBufUtils.readUTF8String(buf);
		
		count = ByteBufUtils.readVarShort(buf);
		
		for(int i = 0; i < count; i++)
			lines.add(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeUTF8String(buf, soundFile);
		
		// write amount of lines.
		ByteBufUtils.writeVarShort(buf, lines.size());
		
		for(String line : lines)
			ByteBufUtils.writeUTF8String(buf, line);
			
	}
		
	///////////////////////////////////////////
	// Client Handler
	///////////////////////////////////////////
	public static class ClientHandler implements IMessageHandler<ScreenNotificationPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final ScreenNotificationPacket message, MessageContext ctx) 
		{
			if(message == null || message.lines.isEmpty())
			{
				MPUtils.logger.log(Level.ERROR, "A critical NPE error occured during while handling a Raiders notification packet Client side", new NullPointerException());
				return null;
			}
				
			IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					ScreenNotification.ScheduleNotice(message.lines,  message.soundFile);
				}
			});
			return null;
		}
			
	}


}