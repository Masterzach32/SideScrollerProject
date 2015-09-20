package net.masterzach32.sidescroller.util;

import net.masterzach32.sidescroller.main.SideScroller;

public class LogHelper {
	
	public static LogHelper logger = new LogHelper();
	
	public LogHelper() {}

	public void logInfo(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + SideScroller.game.getThread().getName() + "] " + "[INFO] " + s);
	}
	
	public void logWarning(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + SideScroller.game.getThread().getName() + "] " + "[WARNING] " + s);
	}
	
	public void logError(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + SideScroller.game.getThread().getName() + "] " + "[ERROR] " + s);
	}
	
}