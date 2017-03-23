package com.gendeathrow.mputils.client.settings;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;

import com.gendeathrow.mputils.client.gui.Gui_QuickMenu;

public class QuickCommandManager
{
	public static QuickCommandManager instance = new QuickCommandManager();
	
	public static ArrayList<CommandElement> commandList = new ArrayList<CommandElement>(10);
	
	public static ArrayList<CommandElement> globalCommandList = new ArrayList<CommandElement>(10);
	

	// set Defaults
	static
	{
		commandList.add(instance.new CommandElement("Survival", "/gamemode s"));
		commandList.add(instance.new CommandElement("Creative", "/gamemode c"));
		commandList.add(instance.new CommandElement("Toggle Rain", "/toggledownfall"));
		commandList.add(instance.new CommandElement("MPHand", "/mp hand"));
		commandList.add(instance.new CommandElement("MPHotbar", "/mputil hotbar"));
		commandList.add(instance.new CommandElement("MPInv", "/mp inventory"));
		commandList.add(instance.new CommandElement("Looking At", "/mputil lookingat"));
		commandList.add(null);
		commandList.add(null);
		commandList.add(null);
		
		
		globalCommandList.add(instance.new CommandElement("Survival", "/gamemode s"));
		globalCommandList.add(instance.new CommandElement("Creative", "/gamemode c"));
		globalCommandList.add(instance.new CommandElement("Toggle Rain", "/toggledownfall"));
		globalCommandList.add(instance.new CommandElement("MPHand", "/mp hand"));
		globalCommandList.add(instance.new CommandElement("MPHotbar", "/mputil hotbar"));
		globalCommandList.add(instance.new CommandElement("MPInv", "/mp inventory"));
		globalCommandList.add(instance.new CommandElement("Looking At", "/mputil lookingat"));
		globalCommandList.add(null);
		globalCommandList.add(null);
		globalCommandList.add(null);
	}
	
	public void removeCommand(ArrayList<CommandElement> list, int rmv)
	{
		list.set(rmv, null);
		
    	MPUtils_SaveHandler.saveSettings();
		
	}
	
	public void addCommand(ArrayList<CommandElement> list, int id, CommandElement cmd)
	{
		if(list.get(id) == null) list.set(id, cmd);
		
    	MPUtils_SaveHandler.saveSettings();
		
	}
	

	public static void load(ArrayList<CommandElement> list, NBTTagCompound tag)
	{
		list.clear();
		
		NBTTagCompound nbtCom = tag.getCompoundTag("quickcommands");
		
		ReadNBT(list, nbtCom);
	}
	
	public static void save(ArrayList<CommandElement> list, NBTTagCompound mainTag)
	{
		NBTTagCompound cmdCatTag = new NBTTagCompound();

		for(int i = 0; i <= 9; i++)
		{
			NBTTagCompound cmdTag = new NBTTagCompound();
			
			CommandElement commandElement = list.get(i);
			
			if(commandElement != null)
			{
				cmdTag.setString("title", commandElement.title);
				cmdTag.setString("cmd", commandElement.getCommand());
				cmdCatTag.setTag("c"+i, cmdTag);
			}
			
			cmdCatTag.setBoolean("isPaused", Gui_QuickMenu.pauseGame);
		}
		
		mainTag.setTag("quickcommands", cmdCatTag);
	}
	
	
	public static ArrayList<CommandElement> getList(int list)
	{
		if(list == 0)
			return commandList;
		else if(list == 1)
			return globalCommandList;
		else 
			return commandList;
	}
	
	private static void ReadNBT(ArrayList<CommandElement> list, NBTTagCompound nbt)
	{	
		
		for(int i = 0; i <= 9; i++)
		{
			String key = "c"+i;
			if(nbt.hasKey(key))
			{
				NBTTagCompound cmd = nbt.getCompoundTag(key);
				CommandElement element = instance.new CommandElement(cmd.getString("title"), cmd.getString("cmd"));
				list.add(element);
			}
			else
			{
				list.add(null);
			}
			
			if(nbt.hasKey("isPaused"))
			{
				Gui_QuickMenu.pauseGame = (nbt.getBoolean("isPaused"));
			}
		}
		
	}
	
	
	@SuppressWarnings("unused")
	private static void SaveNBT(ArrayList<CommandElement> list, NBTTagCompound nbt)
	{
		for(int i = 0; i <= 9; i++)
		{
			CommandElement cmd = list.get(i);
			NBTTagCompound cmdTag = new NBTTagCompound();
			if(cmd != null)
			{

				cmdTag.setString("title", cmd.title);
				cmdTag.setString("cmd", cmd.command);
					
				nbt.setTag("c"+i, cmdTag);
			}
		}
	}
	
	
	public class CommandElement
	{
		String title;
		String command;
		boolean isBlank = false;
		boolean toggleBoolen = false;

		public CommandElement(String title, String command)
		{
			this.title = title;
			this.command = command;
			this.isBlank = false;
		}
		
		public CommandElement(String title, String command, boolean isBlank)
		{
			this.title = title;
			this.command = command;
			this.isBlank = isBlank;
		}		

		public String getCommand()
		{
			return this.command;
		}
		
		public String getTitle()
		{
			return this.title;
		}
		
		public boolean isBlank()
		{
			return this.isBlank;
		}

		public void setCommand(String text) 
		{
			this.command = text;
		}

		public void setTitle(String text) 
		{
			this.title = text;
		}
		
		public String parseCommand()
		{
			String parsed = this.command;
				this.toggleBoolen = !toggleBoolen;
				parsed = this.command.replaceAll("\\@t", toggleBoolen+"");
			return parsed;
		}
		
	}
}
