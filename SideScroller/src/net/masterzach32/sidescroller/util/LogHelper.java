package net.masterzach32.sidescroller.util;

import net.masterzach32.sidescroller.main.SideScroller;

public class LogHelper {

	public static void logInfo(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + SideScroller.getGame().getThread().getName() + "] " + "[INFO] " + s);
	}
	
	public static void logWarning(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + SideScroller.getGame().getThread().getName() + "] " + "[WARNING] " + s);
	}
	
	public static void logError(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + SideScroller.getGame().getThread().getName() + "] " + "[ERROR] " + s);
	}
	
}