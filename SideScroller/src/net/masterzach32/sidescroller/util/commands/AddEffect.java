package net.masterzach32.sidescroller.util.commands;

import net.masterzach32.sidescroller.util.LogHelper;

public class AddEffect extends ConsoleCommand {

	public AddEffect() {
		super("addEffect", new String[] {"[type]", "<strength>", "<duration>"}, "String", "Adds an effect to the player.");
	}

	protected void execute(String parameters) {
		LogHelper.logInfo("addEffect command is not");
	}
}