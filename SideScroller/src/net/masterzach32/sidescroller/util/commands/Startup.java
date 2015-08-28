package net.masterzach32.sidescroller.util.commands;

import net.masterzach32.sidescroller.util.LogHelper;

public class Startup extends ConsoleCommand {

	public Startup() {
		super("startup", new String[] {"[option]"}, "String", "Base command for many different startup options.");
	}

	@Override
	protected void execute(String parameters) {
		LogHelper.logInfo("startup 0");
	}
}