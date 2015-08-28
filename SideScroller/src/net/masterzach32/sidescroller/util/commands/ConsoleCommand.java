package net.masterzach32.sidescroller.util.commands;

import java.util.ArrayList;

import net.masterzach32.sidescroller.util.LogHelper;
import net.masterzach32.sidescroller.util.Utilities;

public abstract class ConsoleCommand {
	
	protected String identifier, type, helpText;
	protected String[] subIdentifiers;
	
	private static ArrayList<ConsoleCommand> commands = new ArrayList<ConsoleCommand>();

	public ConsoleCommand(String identifier, String[] subIdentifiers, String type, String helpText) {
		this.identifier = identifier;
		this.subIdentifiers = subIdentifiers;
		this.type = type;
		this.helpText = helpText;
		
		commands.add(this);
	}
	
	protected abstract void execute();
	
	public static void reciveCommand(String command) {
		try {
			if(!command.startsWith("/")) return;
			LogHelper.logInfo("User Input Command: " + command);

			boolean doesExist = false;
			command = command.substring(1, command.length());
			String identifier = command;

			for(int i = 0; i < commands.size(); i++) {
				if(identifier.equals(commands.get(i).getIdentifier())) {
					doesExist = true;
					commands.get(i).execute();
				}
			}
			if(!doesExist) LogHelper.logInfo("Could not find command identifier: " + identifier + ". Type /help for a list of commands.");
		} catch(Exception e) {
			Utilities.createErrorDialog("Command Input Error", "The command system could not process this command: " + command + "\nTry /help for a list of commands.", e);
		}
	}
	
	public static void enableCommands() {
		new Help();
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getType() {
		return type;
	}
	
	public String getHelpText() {
		return helpText;
	}

	public String[] getSubIdentifiers() {
		return subIdentifiers;
	}

	public static ArrayList<ConsoleCommand> getCommands() {
		return commands;
	}
}