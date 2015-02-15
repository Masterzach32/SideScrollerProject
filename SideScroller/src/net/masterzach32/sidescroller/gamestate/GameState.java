package net.masterzach32.sidescroller.gamestate;

import java.awt.Graphics2D;

public abstract class GameState {
	
	protected GameStateManager gsm;
	
	public abstract void init();
	public abstract void tick();
	public abstract void render(Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
}
