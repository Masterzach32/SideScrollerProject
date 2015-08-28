package net.masterzach32.sidescroller.util.commands;

import java.util.ArrayList;

import net.masterzach32.sidescroller.util.LogHelper;

public class Help extends ConsoleCommand {

	public Help() {
		super("help", new String[] {""}, "list", "Lists all available commands");
	}

	protected void execute() {
		LogHelper.logInfo("List of available commands:");
		ArrayList<ConsoleCommand> commands = getCommands();
		for(int i = 0; i < commands.size(); i++) {
			LogHelper.logInfo("/" + commands.get(i).getIdentifier() + " - " + commands.get(i).getHelpText());
		}
	}
}