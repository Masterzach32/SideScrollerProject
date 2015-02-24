package net.masterzach32.sidescroller.gamestate;

import net.masterzach32.sidescroller.main.SideScroller;

public abstract class LevelState extends GameState {

	public LevelState(SideScroller game) {
		super(game);
		
	}
	
	/**
	 * Only use this with game levels, called when the level is completed
	 */
	public abstract void levelCompleted();
	
	/**
	 * Spawns the enemies on the map
	 */
	protected abstract void populateEnemies();

}
