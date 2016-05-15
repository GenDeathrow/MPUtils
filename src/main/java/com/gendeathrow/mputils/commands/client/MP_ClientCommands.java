package com.gendeathrow.mputils.commands.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class MP_ClientCommands  extends CommandBase
{
	ArrayList<MP_ClientBaseCommand> coms = new ArrayList<MP_ClientBaseCommand>();
	
	public MP_ClientCommands()
	{
		coms.add(new MP_LookAtCommand());
		coms.add(new MP_ReloadTips());
	}
	
	@Override
	public String getCommandName() 
	{
		return "mputil";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) 
	{
		String txt = "";
		
		for(int i = 0; i < coms.size(); i++)
		{
			MP_ClientBaseCommand c = coms.get(i);
			txt += "/mputil " + c.getCommand();
			
			if(c.getUsageSuffix().length() > 0)
			{
				txt += " " + c.getUsageSuffix();
			}
			
			if(i < coms.size() -1)
			{
				txt += ", ";
			}
		}
		
		return txt;

	}
	
	@SuppressWarnings("unchecked")
	@Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] strings, BlockPos pos)
    {
		if(strings.length == 1)
		{
			ArrayList<String> base = new ArrayList<String>();
			for(MP_ClientBaseCommand c : coms)
			{
				base.add(c.getCommand());
			}
        	return getListOfStringsMatchingLastWord(strings, base.toArray(new String[0]));
		} else if(strings.length > 1)
		{
			for(MP_ClientBaseCommand c : coms)
			{
				if(c.getCommand().equalsIgnoreCase(strings[0]))
				{
					return c.autoComplete(sender, strings);
				}
			}
		}
		
		return new ArrayList<String>();
    }

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		if(args.length < 1)
		{
			throw new WrongUsageException(this.getCommandUsage(sender));
		}
		
		for(MP_ClientBaseCommand c : coms)
		{
			if(c.getCommand().equalsIgnoreCase(args[0]))
			{
				if(c.validArgs(args))
				{
					c.runCommand(this, sender, args);
					return;
				} else
				{
					throw c.getException(this);
				}
			}
		}
		
		throw new WrongUsageException(this.getCommandUsage(sender));
	}

}