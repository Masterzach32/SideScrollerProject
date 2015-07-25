package net.masterzach32.sidescroller.gamestate.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;

public class LoadingState extends GameState {
	
	protected Background bg;
	
	protected Color titleColor;
	protected Font titleFont;
	
	protected Font font;
	protected Font selectfont;
	
	private static String text = null;
	
	private static int percent = 0;

	public LoadingState(SideScroller game) {
		super(game);
	}

	public void init() {}

	protected void load() {}

	protected void unload() {}

	public void tick() {}

	public void render(Graphics2D g) {
		g.drawImage(Assets.getImageAsset("zaunbg"), 0, 0, SideScroller.WIDTH, SideScroller.HEIGHT, null);
		g.setColor(Color.WHITE);
		g.drawString(text, 10, 17);
		g.drawRect(((Game.getFrame().getWidth() / 4) - (200)) , 300, 400, 20);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(((Game.getFrame().getWidth() / 4) - (200)) , 300, percent * 4, 21);
	}
	
	public static void setInfo(String s, int i) {
		text = s;
		percent = i;
	}

	public void keyPressed(int k) {}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}
