package net.masterzach32.sidescroller.gamestate.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;

public class KeyConfigState extends MenuState {
	
	private Font subtitleFont;
	
	public static int currentChoice = 0;
	public static String[] options = new String[7]; 
	
	private boolean changingKey;
	public static int[] keyBinding = new int[6];

	public KeyConfigState(SideScroller game) {
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
		changingKey = false;
		
		keyBinding[0] = KeyEvent.VK_D;
		keyBinding[1] = KeyEvent.VK_A;
		keyBinding[2] = KeyEvent.VK_SPACE;
		keyBinding[3] = KeyEvent.VK_R;
		keyBinding[4] = KeyEvent.VK_F;
		keyBinding[5] = KeyEvent.VK_E;
	}

	protected void load() {
		currentChoice = 0;
	}

	protected void unload() {}

	public void tick() {
		bg.tick();
	}

	public void render(Graphics2D g) {
		options[0] = "Right: " + keyBinding[0]; 
		options[1] = "Left: " + keyBinding[1];
		options[2] = "Jump: " + keyBinding[2];
		options[3] = "Scratch: " + keyBinding[3];
		options[4] = "Orb: " + keyBinding[4];
		options[5] = "Glide: " + keyBinding[5];
		options[6] = "Back";
		// draw bg
		bg.render(g);						
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("SideScroller RPG", 195, 45);
		g.setFont(subtitleFont);
		g.drawString("Key Config", 267, 75);
		
		g.setFont(selectfont);
		g.setColor(Color.BLACK);
		g.drawString("Use up/down to navigate, and enter to change", 180, 130);
		if(!changingKey) g.drawString("Select the function you want to change", 200, 150);
		else g.drawString("Press the key you want to change it to", 200, 150);
						
		// draw menu options
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setFont(selectfont);
				g.setColor(Color.BLACK);
			} else {
				g.setFont(font);
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 275, (360 + i * 30) / 2);
		}	
	}
	
	private void select() {
		if(currentChoice != 6) {
			changeKeyConfig();
		} else {
			GameState.setState(SideScroller.optionsState);
		}
	}

	public void keyPressed(int k) {
		if(!changingKey) {
			if(k == KeyEvent.VK_ENTER) {
				select();
			} if(k == KeyEvent.VK_ESCAPE) {
				GameState.setState(SideScroller.optionsState);
			} if(k == KeyEvent.VK_UP) {
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
		} else {
			keyBinding[currentChoice] = k;
			changingKey = false;
		}
	}
	
	public void changeKeyConfig() {
		changingKey = true;
	}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}
