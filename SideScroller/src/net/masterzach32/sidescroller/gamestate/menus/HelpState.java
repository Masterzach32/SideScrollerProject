package net.masterzach32.sidescroller.gamestate.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.util.Utilities;

public class HelpState extends MenuState {
	
	public static String[] helpText = {
		"How To Play:", 
		"Default Controls:", 
		"Movement:", 
		"A and D for movement", 
		"Space to jump", 
		"Jump: SPACE", 
		"Goal:", 
		"Defeat all enemies and restore Shurima", 
		"as the new ascended emperor Azir!", 
		"Exit these menus with ESCAPE"
	};

	public HelpState(SideScroller game) {
		super(game);
	}

	public void init() {}

	protected void load() {}
	
	protected void unload() {}

	public void tick() {
		bg.tick();
	}

	public void render(Graphics2D g) {
		bg.render(g);
		// title
		g.setColor(titleColor);
		g.setFont(titleFont);
		Utilities.drawCenteredString(g, "SideScroller Project", 45);
		g.setFont(subtitleFont);
		Utilities.drawCenteredString(g, "Help", 75);
		
		g.setFont(selectfont);
		g.setColor(Color.BLACK);
		
		g.setFont(font);
		g.setColor(Color.RED);
		for(int i = 0; i < helpText.length; i++) {
			Utilities.drawCenteredString(g, helpText[i], (250 + i * 30) / 2);
		}
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(info, 280, 355);
		g.setColor(Color.WHITE);
	}

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ESCAPE) {
			GameState.setState(SideScroller.menuState);
		}
	}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}