package net.masterzach32.sidescroller.gamestate.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;

public class LoadingState extends GameState {
	
	protected Background bg;
	
	protected Color titleColor;
	protected Font titleFont;
	
	protected Font font;
	protected Font selectfont;

	public LoadingState(SideScroller game) {
		super(game);
	}

	public void init() {
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.BOLD, 32);
	}

	protected void load() {}

	protected void unload() {}

	public void tick() {}

	public void render(Graphics2D g) {
		g.drawImage(Assets.getImageAsset("zaunbg"), 0, 0, SideScroller.WIDTH, SideScroller.HEIGHT, null);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Loading...", 10, 17);	
	}

	public void keyPressed(int k) {}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}
