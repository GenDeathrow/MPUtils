package com.gendeathrow.mputils.javaenforcer.plugin;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import org.apache.logging.log4j.LogManager;

import com.gendeathrow.mputils.javaenforcer.JavaChecker;
import com.gendeathrow.mputils.javaenforcer.Utils;

//@TransformerExclusions(value = "com.gendeathrow.mputils.javaenforcer")
public class CorePlugin //implements IFMLCallHook
{  
	  public static final String mcVersion = "1.11.2";
	  public static final String version = "1.0.0";
	  public static org.apache.logging.log4j.Logger logger = LogManager.getLogger("java_enforcer");
	  
	  public CorePlugin() 
	  {
System.out.println("WORKING OR NOT");
	  }
	  
//	  @Override
//	  public String[] getASMTransformerClass() {  return null; }

//	  @Override
//	  public String getModContainerClass() { return null; }

//	  @Override
//	  public String getSetupClass() { return null; }
//
//	  @Override
//	  public void injectData(Map<String, Object> data) { }

//	  @Override
//	  public String getAccessTransformerClass() { return null; }

//	@Override
//	public Void call() throws Exception 
//	{
//		  logger.info("Loading JavaEnforcer Core...................................................................................................................................................................................");
//		  Utils.loadConfigData(logger);
//		  try 
//		  {
//			  JavaChecker.run(logger);
//		  } catch (InterruptedException e) 
//		  {
//			e.printStackTrace();
//		  }
//		
//		return null;
//	}

}
