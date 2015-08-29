package net.masterzach32.sidescroller.util.commands;

import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.util.LogHelper;

public class Damage extends ConsoleCommand {

	public Damage() {
		super("setDamage", new String[] {"[value]"}, "int", "Sets the player's damage to the provided int value.");
	}

	protected void execute(String parameters) {
		int damage = Integer.parseInt(parameters);
		LevelState.getPlayer().setDamage(damage);
		LogHelper.logInfo("Player damage set to " + damage);
	}
}