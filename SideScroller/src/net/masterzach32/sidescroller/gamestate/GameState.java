package net.masterzach32.sidescroller.gamestate;

import java.awt.Graphics2D;

import net.masterzach32.sidescroller.main.SideScroller;

public abstract class GameState {
	
	protected static GameState state = null;
	protected SideScroller game = null;
	
	public GameState(SideScroller game) {
		this.game = game;
	}
	
	public abstract void init();
	
	protected abstract void load();
	
	protected abstract void unload();
	
	public abstract void levelCompleted();
	
	public abstract void tick();
	
	public abstract void render(Graphics2D g);
	
	public abstract void keyPressed(int k);
	
	public abstract void keyReleased(int k);
	
	public abstract void mousePressed(int k);
	
	public abstract void mouseReleased(int k);
	
	private static void unloadState() {
		if(state != null) state.unload();
		state = null;
	}
	
	private static void loadState(GameState s) {
		state = s;
		state.load();
	}
	
	/**
	 * Sets the game state by unloading the current state and loading the new one
	 * @param currentState
	 * @param newState
	 */
	public static void setState(GameState newState) {
		unloadState();
		loadState(newState);
	}
	
	/**
	 * Returns the current state
	 * @return
	 */
	public static GameState getState() {
		return state;
	}
}