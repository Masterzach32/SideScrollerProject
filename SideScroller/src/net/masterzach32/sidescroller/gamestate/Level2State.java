package net.masterzach32.sidescroller.gamestate;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import net.masterzach32.sidescroller.main.SideScroller;

public class Level2State extends GameState {

	public Level2State(SideScroller game) {
		super(game);
		init();
	}

	public void init() {}

	protected void load() {}

	protected void unload() {}
	
	public void levelCompleted() {}

	public void tick() {}

	public void render(Graphics2D g) {}

	public void keyPressed(int k) {}
	
	public void keyReleased(int k) {}
}