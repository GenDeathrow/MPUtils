package com.gendeathrow.mputils.network;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
	private List<String> lines = new ArrayList<String>();
	private String soundFile;
//	private ItemStack stack = ItemStack.EMPTY;
	private int bgColor = Color.GRAY.getRGB();
	private int borderColor = Color.BLACK.getRGB();
	
	public ScreenNotificationPacket(){	}

	
	public ScreenNotificationPacket(List<String> lines) // Use PacketDataTypes to instantiate new packets
	{
		this(lines, SoundEvents.ENTITY_PLAYER_LEVELUP.getRegistryName().toString());
	}
	
	public ScreenNotificationPacket(List<String> linesIn, String sound) // Use PacketDataTypes to instantiate new packets
	{
		this(linesIn, sound, Color.GRAY.getRGB(),Color.BLACK.getRGB());
	}
	
	public ScreenNotificationPacket(List<String> linesIn, String sound, int bgcolorIn, int bordercolorIn ) // Use PacketDataTypes to instantiate new packets
	{
		lines = linesIn;
		count = linesIn.size();
		soundFile = sound;
		bgColor = bgcolorIn;
		borderColor = bordercolorIn;
//		stack = stackIn;
	}
		
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		lines.clear();
		
		soundFile = ByteBufUtils.readUTF8String(buf);
		bgColor = ByteBufUtils.readVarInt(buf, 5);
		borderColor = ByteBufUtils.readVarInt(buf, 5);
//		stack = ByteBufUtils.readItemStack(buf);
		
		count = ByteBufUtils.readVarShort(buf);
		for(int i = 0; i < count; i++)
			lines.add(ByteBufUtils.readUTF8String(buf));
		
		
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{

		ByteBufUtils.writeUTF8String(buf, soundFile == null ? "null" : soundFile);
		
		
		ByteBufUtils.writeVarInt(buf, bgColor, 5);
		
		ByteBufUtils.writeVarInt(buf, borderColor, 5);
//		ByteBufUtils.writeItemStack(buf, stack);
		
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
				MPUtils.logger.log(Level.ERROR, "A critical NPE error occured during while handling a MPUtils notification packet Client side", new NullPointerException());
				return null;
			}
				
			IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() {
					ScreenNotification.ScheduleNotice(message.lines,  message.soundFile.equals("null") ? null : message.soundFile, message.bgColor, message.borderColor);
				}
			});
			return null;
		}
			
	}


}