package com.gendeathrow.mpbasic.core;

public class MPBSettings 
{

	public static String changeLogTitle = "What's New!";
	public static boolean isHttp = false;
	public static String url = "changelog.txt";
	
	
	public static boolean showChangeLogButton = true;
	public static boolean showSupport = true;
	public static boolean showBugReporter = true;
	
	public static String supportURL = "https://www.patreon.com/GenDeathrow";
	
//	@Deprecated
//	public static String bugURL = "http://minecraft.curseforge.com/projects/mputils/issues";
//	
	public static boolean showUpdateNotification = true;
	
	public static String issuetrackerURL =  "https://github.com/GenDeathrow/MPUtils/issues";
	public static boolean useInGameForm = false;
	public static boolean crashlogsToGist = true;
	public static boolean sendJsonData = false;
	public static boolean collectContact = true;
	public static String disclaimerFile = "config/mputils/addons/mpbasic/disclaimer.txt";
	
	public static String[] contactTypes = new String[] {"Email", "Curse", "GitHub"};
	public static String[] issueTypes = new String[] {"Bug", "Game Crash", "Config Issue", "Mod Conflict"};
	public static boolean useDisclaimer = false;
	
	
	public static String faqURL = "faq.txt";
	
	
	
}
