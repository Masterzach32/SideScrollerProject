package net.masterzach32.sidescroller.gamestate.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.util.Utilities;

public class AboutState extends MenuState {
	
	public static String[] aboutText = {
		"SideScroller RPG Created By:", 
		"Head Designer and Programmer:", 
		"Zach Kozar", 
		"Assistant Programmers:", 
		"Anthony Kozar", 
		"Keiosu", 
		"StoryLine:",
		"LtzVoid",
		"Catchamp",
		"Art and Music:", 
		"Sk8erclone25", 
	};

	public AboutState(SideScroller game) {
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
		Utilities.drawCenteredString(g, "About", 75);
		
		g.setFont(selectfont);
		g.setColor(Color.BLACK);
		
		g.setFont(font);
		g.setColor(Color.RED);
		for(int i = 0; i < aboutText.length; i++) {
			Utilities.drawCenteredString(g, aboutText[i], (250 + i * 30) / 2);
		}
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(info, 290, 355);
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