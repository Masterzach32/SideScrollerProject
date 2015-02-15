package net.masterzach32.sidescroller.gamestate;

import java.awt.*;
import java.awt.event.KeyEvent;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.tilemap.Background;

public class MenuState extends GameState {
	
	protected Background bg;
	
	public static int currentChoice = 0;
	public static String[] options = {"Play", "Help", "About", "Options", "Quit"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	private Font selectfont;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		init();		
	}
	
	public void init() {
		try {
			bg = new Background(Assets.menubg, 1);
			bg.setVector(-0.5, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 64);
			
			font = new Font("Arial", Font.PLAIN, 24);
			selectfont = new Font("Arial", Font.PLAIN, 28);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
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
		g.drawString("SideScroller RPG", 390, 90);
		g.drawString("", 450, 150);
						
		// draw menu options
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setFont(selectfont);
				g.setColor(Color.BLACK);
			} else {
				g.setFont(font);
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 10, 545 + i * 30);
		}
	}
	
	private void select() {
		if(currentChoice == 0)
			gsm.setState(GameStateManager.LEVEL1STATE);
		if(currentChoice == 1)
			gsm.setState(GameStateManager.MENUSTATE);
		if(currentChoice == 2)
			gsm.setState(GameStateManager.MENUSTATE); // About
		if(currentChoice == 3)
			gsm.setState(GameStateManager.MENUSTATE); // Options
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
}