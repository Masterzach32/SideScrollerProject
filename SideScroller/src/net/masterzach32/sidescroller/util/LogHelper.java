package net.masterzach32.sidescroller.util;

import net.masterzach32.sidescroller.main.SideScroller;

public class LogHelper {
	
	private String name;
	
	public static LogHelper logger = new LogHelper(SideScroller.game.getThread().getName());
	
	public LogHelper(String name) {
		this.name = name;
	}

	public void logInfo(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + name + "] " + "[INFO] " + s);
	}
	
	public void logWarning(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + name + "] " + "[WARNING] " + s);
	}
	
	public void logError(String s) {
		System.out.println("[" + Utilities.getTime() + "] " + "[" + name + "] " + "[ERROR] " + s);
	}	
}