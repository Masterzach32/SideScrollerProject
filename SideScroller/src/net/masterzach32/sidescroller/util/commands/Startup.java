package net.masterzach32.sidescroller.util.commands;

import net.masterzach32.sidescroller.util.LogHelper;

public class Startup extends ConsoleCommand {

	public Startup() {
		super("startup", new String[] {"[option]"}, "String", "Base command for many different startup options.");
	}

	protected void execute(String parameters) {
		LogHelper.logger.logInfo("startup 0");
	}
}