package net.masterzach32.sidescroller.util.commands;

import java.util.ArrayList;

import net.masterzach32.sidescroller.util.LogHelper;
import net.masterzach32.sidescroller.util.Utilities;

public abstract class ConsoleCommand {
	
	protected String identifier, type, helpText;
	protected String[] subIdentifiers;
	
	private static ArrayList<ConsoleCommand> commands;

	public ConsoleCommand(String identifier, String[] subIdentifiers, String type) {
		this.identifier = identifier;
		this.subIdentifiers = subIdentifiers;
		this.type = type;
		
		commands.add(this);
	}
	
	protected abstract void execute();
	
	public static void reciveCommand(String command) {
		try {
		LogHelper.logInfo("User Input Command: " + command);
		
		boolean doesExist = false;
		int length = command.indexOf(' ');
		String identifier = command.substring(0, length);
		
		for(int i = 0; i < commands.size(); i++) {
			if(identifier.equals(commands.get(i).getIdentifier())) {
				doesExist = true;
				commands.get(i).execute();
			}
		}
		if(!doesExist) LogHelper.logInfo("Could not find command identifier: " + identifier);
		} catch(Exception e) {
			Utilities.createErrorDialog("Command Input Error", "The command system could not process this command: " + command + "\nTry /help for a list of commands.", e);
		}
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