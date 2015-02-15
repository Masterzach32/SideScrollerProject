package net.masterzach32.sidescroller.gamestate;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 2;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == LEVEL1STATE)
			gameStates[state] = new Level1State(this);
	}
	

	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	/** 
	 * Sets a new state by unloading the current one and loading the new one
	 * @param state
	 */
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}
	
	public void tick() {
		try {
			gameStates[currentState].tick();
		} catch(Exception e) {}
	}
	
	public void render(Graphics2D g) {
		try {
			gameStates[currentState].render(g);
		} catch(Exception e) {}
	}
	
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
	
}