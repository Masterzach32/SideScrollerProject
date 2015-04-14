package net.masterzach32.sidescroller.gamestate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.util.LogHelper;

public class OptionsState extends MenuState {

	private Font subtitleFont;
	
	public static int currentChoice = 0;
	public static String[] options = {"Scale:", "Resolution:", "Controls: (WIP)", "FPS: (broken)"};
	
	public OptionsState(SideScroller game) {
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
		
		bgMusic = new AudioPlayer(Assets.getAudioAsset("warriors"));
	}

	protected void load() {
		bgMusic.play();
	}

	protected void unload() {
		bgMusic.stop();
	}

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
		g.setFont(subtitleFont);
		g.drawString("Options", 280, 75);
		
		g.setFont(selectfont);
		g.setColor(Color.BLACK);
		g.drawString("Use up/down to navigate, and enter to change", 180, 150);
						
		// draw menu options
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setFont(selectfont);
				g.setColor(Color.BLACK);
			} else {
				g.setFont(font);
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 215, (360 + i * 30) / 2);
			g.setFont(selectfont);
			g.setColor(Color.BLACK);
			g.drawString("(" + SideScroller.SCALE + "x)", 395, (360 + 0 * 30) / 2);
			g.drawString("(" + SideScroller.WIDTH * SideScroller.SCALE + "x" + SideScroller.HEIGHT * SideScroller.SCALE + ")", 356, (360 + 1 * 30) / 2);
		}	
	}
	
	private void select() {
		if(currentChoice == 0) {
			if(SideScroller.SCALE >= 4) {
				SideScroller.SCALE = 1;
			} else {
				SideScroller.SCALE += 1;
			}
			Game.getFrame().setBounds(0, 0, SideScroller.WIDTH * SideScroller.SCALE, SideScroller.HEIGHT * SideScroller.SCALE);
			LogHelper.logInfo("Scale changed to " + SideScroller.SCALE);
		}
		if(currentChoice == 1) {
			if(SideScroller.WIDTH == 640) {
				SideScroller.WIDTH = 512;
				SideScroller.HEIGHT = 384;
			} else {
				SideScroller.WIDTH = 640;
				SideScroller.HEIGHT = 360;
			}
			Game.getFrame().setBounds(0, 0, SideScroller.WIDTH * SideScroller.SCALE, SideScroller.HEIGHT * SideScroller.SCALE);
			LogHelper.logInfo("Resolution changed to (" + SideScroller.WIDTH + "x" + SideScroller.HEIGHT + ")");
		}
		if(currentChoice == 2) {
			// do somthing
		}
		if(currentChoice == 3) {
			// do somthing
		}
	}

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) {
			select();
		} if(k == KeyEvent.VK_ESCAPE) {
			GameState.setState(SideScroller.menuState);
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
	}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}
