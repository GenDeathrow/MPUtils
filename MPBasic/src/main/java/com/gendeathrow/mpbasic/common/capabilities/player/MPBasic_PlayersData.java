package com.gendeathrow.mpbasic.common.capabilities.player;

import java.util.concurrent.TimeUnit;

import com.gendeathrow.mputils.utils.StopWatch;

public class MPBasic_PlayersData implements IMPBasic_PlayerData
{

	private StopWatch timer = new StopWatch();
		
	public StopWatch getTimer()
	{
		return timer;
	}
	
	public StopWatch createTimer()
	{
		timer = new StopWatch();
		timer.setElapsedNanos(getElapsed());
		return timer;
	}
	
	@Override
	public void setElapsed(Long time) 
	{
		timer.setElapsedNanos(time);
	}

	@Override
	public Long getElapsed() 
	{
		return timer.elapsed(TimeUnit.NANOSECONDS);
	}
	
	

}
