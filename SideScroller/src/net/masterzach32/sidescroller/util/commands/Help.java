package net.masterzach32.sidescroller.util.commands;

import java.util.ArrayList;

import net.masterzach32.sidescroller.util.LogHelper;

public class Help extends ConsoleCommand {

	public Help() {
		super("help", new String[] {"<page>"}, "int", "Lists all available commands.");
	}

	protected void execute(String parameters) {
		LogHelper.logger.logInfo("List of available commands: Parameters: [required] <not required>");
		ArrayList<ConsoleCommand> commands = getCommands();
		StringBuilder sb;
		String commandParams = "";
		for(ConsoleCommand command : commands) {
			String[] params = command.getSubIdentifiers();
			sb = new StringBuilder();
			for(int i = 0; i < params.length; i++) {
				sb.append(" " + params[i]);
			}
			commandParams = sb.toString();
			LogHelper.logger.logInfo("/" + command.getIdentifier() + commandParams + " - " + command.getHelpText());
		}
	}
}