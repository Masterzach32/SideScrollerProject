package net.masterzach32.sidescroller.gamestate.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.util.Utilities;

public class HelpState extends MenuState {
	
	private Font subtitleFont;
	
	public static int currentChoice = 0;
	public static String[] helpText = {
		"Default Controls:", 
		"Left: A", 
		"Right: D", 
		"Up: W", 
		"Down: S", 
		"Jump: W or SPACE", 
		"Glide: E", 
		"Scratch: R or Left Click", 
		"Fire: F or Right Click", 
		"Main Menu: ESCAPE"
	};

	public HelpState(SideScroller game) {
		super(game);
	}

	public void init() {
		bg = new Background(Assets.getImageAsset("grassbg"), 1);
		bg.setVector(-0.25, 0);
		
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.BOLD, 32);
		subtitleFont = new Font("Century Gothic", Font.BOLD, 20);
			
		font = new Font("Arial", Font.PLAIN, 12);
		selectfont = new Font("Arial", Font.PLAIN, 14);
	}

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
		Utilities.drawCenteredString(g, "SideScroller RPG", 45);
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