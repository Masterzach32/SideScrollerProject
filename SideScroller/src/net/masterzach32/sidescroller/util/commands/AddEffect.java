package net.masterzach32.sidescroller.util.commands;

import net.masterzach32.sidescroller.util.LogHelper;

public class AddEffect extends ConsoleCommand {

	public AddEffect() {
		super("addEffect", new String[] {""}, "String", "Adds an effect to the player.");
	}

	protected void execute() {
		LogHelper.logInfo("addEffect true");
	}
}