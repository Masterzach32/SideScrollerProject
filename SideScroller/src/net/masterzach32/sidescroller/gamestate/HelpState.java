package net.masterzach32.sidescroller.gamestate;

import java.awt.Graphics2D;

import net.masterzach32.sidescroller.main.SideScroller;

public class HelpState extends MenuState {
	
	public static int currentChoice = 0;
	public static String[] help = {"Play", "Help", "About", "Options", "Quit"};

	public HelpState(SideScroller game) {
		super(game);
	}

	public void init() {}

	protected void load() {
		bgMusic.play();
	}
	
	protected void unload() {
		bgMusic.stop();
	}

	public void tick() {}

	public void render(Graphics2D g) {}

	public void keyPressed(int k) {}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}