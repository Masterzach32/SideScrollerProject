package net.masterzach32.sidescroller.gamestate;

import java.awt.*;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;

public class MenuState extends GameState {
	
	protected Background bg;
	
	public static int currentChoice = 0;
	public static String[] options = {"Play", "Help", "About", "Options", "Quit"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	private Font selectfont;
	
	private AudioPlayer bgMusic;
	
	public MenuState(SideScroller game) {
		super(game);
		init();		
	}
	
	public void init() {
		bg = new Background(Assets.getImageAsset("grassbg"), 1);
		bg.setVector(-0.25, 0);
			
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.PLAIN, 32);
			
		font = new Font("Arial", Font.PLAIN, 12);
		selectfont = new Font("Arial", Font.PLAIN, 14);
		bgMusic = new AudioPlayer(Assets.getSoundAsset("warriors"));
	}
	
	protected void load() {
		bgMusic.play();
	}
	
	protected void unload() {
		bgMusic.stop();
	}
	
	public void levelCompleted() {}
	
	public void tick() {
		bg.tick();
	}
	
	public void render(Graphics2D g) {
		// draw bg
		bg.render(g);						
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("SideScroller RPG", 195, 45);
		g.drawString("", 225, 75);
						
		// draw menu options
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setFont(selectfont);
				g.setColor(Color.BLACK);
			} else {
				g.setFont(font);
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 5, (590 + i * 30) / 2);
		}	
	}
	
	private void select() {
		if(currentChoice == 0)
			GameState.setState(SideScroller.level1);
		if(currentChoice == 1)
			GameState.setState(SideScroller.menuState); // Help
		if(currentChoice == 2)
			GameState.setState(SideScroller.menuState); // About
		if(currentChoice == 3)
			GameState.setState(SideScroller.menuState); // Options
		if(currentChoice == 4)
			System.exit(0);
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