package net.masterzach32.sidescroller.gamestate.menus;

import java.awt.*;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.util.Utilities;

public class MenuState extends GameState {
	
	protected static Background bg;
	
	protected static String info = "Use the Up and Down arrows to navigate, and ENTER to select.";
	
	public int currentChoice;
	public static String[] options = {
		"Play",
		"Help", 
		"About", 
		"Options", 
		"Quit"
	};
	
	protected static Color titleColor;
	protected static Font titleFont;
	protected static Font subtitleFont;
	
	protected static Font font;
	protected static Font selectfont;
	
	public static AudioPlayer bgMusic;
	
	public MenuState(SideScroller game) {
		super(game);
	}
	
	public void init() {
		bg = new Background(Assets.getImageAsset("grassbg"), 1);
		bg.setVector(-.25, 0);
		
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.BOLD, 32);
		subtitleFont = new Font("Century Gothic", Font.BOLD, 20);
		
		font = new Font("Arial", Font.PLAIN, 12);
		selectfont = new Font("Arial", Font.PLAIN, 14);
		bgMusic = new AudioPlayer(Assets.getAudioAsset("warriors"));
	}
	
	protected void load() {
		currentChoice = 0;
	}
	
	protected void unload() {}
	
	public void tick() {
		bg.tick();
	}
	
	public void render(Graphics2D g) {
		// draw bg
		bg.render(g);			
		
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(info, 290, 355);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		Utilities.drawCenteredString(g, "SideScroller Project", 45);
						
		// draw menu options
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setFont(selectfont);
				g.setColor(Color.BLACK);
			} else {
				g.setFont(font);
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 5, (580 + i * 30) / 2);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			bgMusic.stop();
			GameState.setState(SideScroller.level1_1);
		}
		if(currentChoice == 1)
			GameState.setState(SideScroller.helpState); // Help
		if(currentChoice == 2)
			GameState.setState(SideScroller.aboutState); // About
		if(currentChoice == 3)
			GameState.setState(SideScroller.optionsState); // Options
		if(currentChoice == 4)
			SideScroller.stop();
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	
	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}

}