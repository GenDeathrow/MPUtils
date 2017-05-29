package com.gendeathrow.mpbasic.common.capabilities.player;

import com.gendeathrow.mputils.utils.StopWatch;


public interface IMPBasic_PlayerData 
{
	public abstract void setElapsed(Long time);
	
	public abstract Long getElapsed();
	
	public abstract  StopWatch getTimer();
	
}
