package net.masterzach32.sidescroller.gamestate.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.util.Utilities;

public class KeyConfigState extends MenuState {
	
	public static final int KEY_RIGHT   = 0;
	public static final int KEY_LEFT    = 1;
	public static final int KEY_JUMP    = 2;
	public static final int KEY_SCRATCH = 3;
	public static final int KEY_ORB     = 4;
	public static final int KEY_GLIDE   = 5;
	public static final int KEY_REWIND  = 6;
	
	public static String[] options = new String[9]; 
	
	private boolean changingKey;
	public static int[] keyBinding = new int[7];

	public KeyConfigState(SideScroller game) {
		super(game);
	}

	public void init() {
		changingKey = false;
		
		resetKeyBindings();
	}

	protected void load() {
		currentChoice = 0;
	}

	protected void unload() {}

	public void tick() {
		bg.tick();
	}

	public void render(Graphics2D g) {
		options[0] = "Right: " + KeyEvent.getKeyText(keyBinding[KEY_RIGHT]); 
		options[1] = "Left: " + KeyEvent.getKeyText(keyBinding[KEY_LEFT]);
		options[2] = "Jump: " + KeyEvent.getKeyText(keyBinding[KEY_JUMP]);
		options[3] = "Scratch: " + KeyEvent.getKeyText(keyBinding[KEY_SCRATCH]);
		options[4] = "Orb: " + KeyEvent.getKeyText(keyBinding[KEY_ORB]);
		options[5] = "Glide: " + KeyEvent.getKeyText(keyBinding[KEY_GLIDE]);
		options[6] = "Rewind: " + KeyEvent.getKeyText(keyBinding[KEY_REWIND]);
		options[7] = "Reset KeyConfig";
		options[8] = "Back";
		// draw bg
		bg.render(g);						
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		Utilities.drawCenteredString(g, "SideScroller Project", 45);
		g.setFont(subtitleFont);
		Utilities.drawCenteredString(g, "Key Config", 75);
		
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(info, 290, 355);
		g.setFont(selectfont);
		if(!changingKey) Utilities.drawCenteredString(g, "Select the function you want to change", 110);
		else Utilities.drawCenteredString(g, "Press the key you want to change it to", 110);
						
		// draw menu options
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setFont(selectfont);
				g.setColor(Color.BLACK);
			} else {
				g.setFont(font);
				g.setColor(Color.RED);
			}
			Utilities.drawCenteredString(g, options[i], (300 + i * 30) / 2);
		}
	}
	
	private void select() {
		if(currentChoice < 7) {
			changeKeyConfig();
		} else if(currentChoice == 7) {
			resetKeyBindings();
		} else if(currentChoice == 8) {
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
	
	private void changeKeyConfig() {
		changingKey = true;
	}
	
	private void resetKeyBindings() {
		keyBinding[KEY_RIGHT] = KeyEvent.VK_D;
		keyBinding[KEY_LEFT] = KeyEvent.VK_A;
		keyBinding[KEY_JUMP] = KeyEvent.VK_SPACE;
		keyBinding[KEY_SCRATCH] = KeyEvent.VK_R;
		keyBinding[KEY_ORB] = KeyEvent.VK_F;
		keyBinding[KEY_GLIDE] = KeyEvent.VK_E;
		keyBinding[KEY_REWIND] = KeyEvent.VK_Q;
	}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}
