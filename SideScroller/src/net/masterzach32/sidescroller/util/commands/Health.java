package net.masterzach32.sidescroller.util.commands;

import net.masterzach32.sidescroller.util.LogHelper;

public class Health extends ConsoleCommand {

	public Health() {
		super("setHealth", new String[] {"[value]"}, "float", "Sets the player health to the provided float value.");
	}

	protected void execute(String parameters) {
		LogHelper.logInfo("setHealth 0");
	}
}