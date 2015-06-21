package net.masterzach32.sidescroller.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import net.masterzach32.sidescroller.main.Handler;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;

public class LoadingState extends GameState {
	
	protected Background bg;
	
	protected Color titleColor;
	protected Font titleFont;
	
	protected Font font;
	protected Font selectfont;

	public LoadingState(Handler handler) {
		super(handler);
	}

	public void init() {
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.BOLD, 32);
	}

	protected void load() {}

	protected void unload() {}

	public void tick() {}

	public void render(Graphics2D g) {
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Loading...", 10, 10);
	}

	public void keyPressed(int k) {}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}
