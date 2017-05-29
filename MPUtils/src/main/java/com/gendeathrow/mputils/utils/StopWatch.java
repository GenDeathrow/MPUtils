package com.gendeathrow.mputils.utils;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.Level;

import com.gendeathrow.mputils.core.MPUtils;


public class StopWatch 
{
	  private boolean isRunning;
	  private boolean isPaused;
	  
	  private boolean gamePaused;
	  
	  private long elapsedNanos;
	  private long startTick;
	  private long splitTick;
	  
	  public StopWatch createTimer(long nano)
	  {
		  this.gamePaused = false;
		  this.isPaused = false;
		  return new StopWatch().setElapsedNanos(nano);
	  }
	  
	  public StopWatch setElapsedNanos(long nano)
	  {
		  this.elapsedNanos = nano;
		  return this;
	  }
	  
	  public boolean isRunning() {
		  return this.isRunning;
	  }
	  
	  public boolean isPaused() {
		  return this.isPaused || this.gamePaused ? true : false;
	  }
	  
	  private boolean isGamePaused() 
	  {
		  if(!MPUtils.proxy.isClient()) return false; 
		  return Minecraft.getMinecraft().isGamePaused();
	  }
	  
	  public StopWatch start() 
	  {
		  if(isRunning) { MPUtils.logger.log(Level.WARN, "This stopwatch is already running."); return this; };
		  isRunning = true;
		  startTick = System.nanoTime();
		  return this;
	  }

	  public StopWatch stop() 
	  {
		  long tick = System.nanoTime();
		  if(!isRunning) { MPUtils.logger.log(Level.WARN, "This stopwatch is already stopped."); return this; };
		  isRunning = false;
		  elapsedNanos += tick - startTick;
		  return this;
	  }
	  
	  public StopWatch split()
	  {
		  long tick = System.nanoTime();
		  if(!isRunning)  { MPUtils.logger.log(Level.WARN, "This stopwatch is already stopped."); return this; };
		  if(isPaused()) { MPUtils.logger.log(Level.WARN, "This stopwatch is paused."); return this; };
		  splitTick += tick - startTick;
		  
		  System.out.println(splitTick +"< split");
		  return this;
	  }

	  public StopWatch pause() 
	  {
		    long tick = System.nanoTime();
		    if(!isRunning) { MPUtils.logger.log(Level.WARN, "This stopwatch is already stopped."); 
		    	return this; };
		    if(isPaused) { MPUtils.logger.log(Level.WARN, "This stopwatch is already paused."); 
		    	return this; };
		    
		   // if(isGamePaused()) gamePaused = true;
		    isPaused = true;		    
		    elapsedNanos += tick - startTick;
		    return this;
	  }

	  public StopWatch resume() 
	  {
		    long tick = System.nanoTime();
		    if(!isRunning) { MPUtils.logger.log(Level.WARN, "This stopwatch is already stopped."); return this; };
		    if(!isPaused) { MPUtils.logger.log(Level.WARN, "This stopwatch is not paused."); return this; };
		   // if(!isGamePaused() && gamePaused) gamePaused = false;
		    isPaused = false;
		    startTick = tick;
		    
		    return this;
	  }

	  //Removed for now
//	  public SpeedStopWatch gamePaused() 
//	  {
//		    long tick = System.nanoTime();
//		    isRunning = false;
//		    gamePaused = true;
//		    elapsedNanos += tick - startTick;
//		    return this;
//	  }
//
//	  public SpeedStopWatch gamePauseResume() 
//	  {
//		    long tick = System.nanoTime();
//		    isRunning = true;
//		    gamePaused = false;
//		    startTick = tick;
//		    return this;
//	  }

	  public StopWatch reset() {
	    elapsedNanos = 0;
	    isRunning = false;
	    return this;
	  }

	  private long elapsedNanos() 
	  {
	    return isRunning && !isPaused ? System.nanoTime() - startTick + elapsedNanos : elapsedNanos;
	  }

	  public long elapsed(TimeUnit desiredUnit) 
	  {
	    return desiredUnit.convert(elapsedNanos(), NANOSECONDS);
	  }

	  public long getSplit(TimeUnit desiredUnit)
	  {
		return  desiredUnit.convert(splitTick, NANOSECONDS);
	  }
	  /**
	   * Returns a string representation of the current elapsed time.
	   */
	  @Override public String toString() {
	    long nanos = elapsedNanos();

	    TimeUnit unit = chooseUnit(nanos);
	    double value = (double) nanos / NANOSECONDS.convert(1, unit);

	    // Too bad this functionality is not exposed as a regular method call
	    return String.format("%.4g %s", value, abbreviate(unit));
	  }

	  private static TimeUnit chooseUnit(long nanos) {
	    if (DAYS.convert(nanos, NANOSECONDS) > 0) {
	      return DAYS;
	    }
	    if (HOURS.convert(nanos, NANOSECONDS) > 0) {
	      return HOURS;
	    }
	    if (MINUTES.convert(nanos, NANOSECONDS) > 0) {
	      return MINUTES;
	    }
	    if (SECONDS.convert(nanos, NANOSECONDS) > 0) {
	      return SECONDS;
	    }
	    if (MILLISECONDS.convert(nanos, NANOSECONDS) > 0) {
	      return MILLISECONDS;
	    }
	    if (MICROSECONDS.convert(nanos, NANOSECONDS) > 0) {
	      return MICROSECONDS;
	    }
	    return NANOSECONDS;
	  }

	  private static String abbreviate(TimeUnit unit) {
	    switch (unit) {
	      case NANOSECONDS:
	        return "ns";
	      case MICROSECONDS:
	        return "\u03bcs"; // μs
	      case MILLISECONDS:
	        return "ms";
	      case SECONDS:
	        return "s";
	      case MINUTES:
	        return "min";
	      case HOURS:
	        return "h";
	      case DAYS:
	        return "d";
	      default:
	        throw new AssertionError();
	    }
	  }
}
