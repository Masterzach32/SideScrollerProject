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
	
	private static double percent = 0;

	public LoadingState(SideScroller game) {
		super(game);
	}

	public void init() {}

	protected void load() {}

	protected void unload() {}

	public void tick() {}

	public void render(Graphics2D g) {
		Font f = new Font("Arial", Font.PLAIN, 12);
		g.setFont(f);
		g.drawImage(Assets.getImageAsset("zaunbg"), 0, 0, SideScroller.WIDTH, SideScroller.HEIGHT, null);
		g.setColor(Color.WHITE);
		g.drawString(text, 10, 17);
		g.drawRect(((Game.getFrame().getWidth() / 4) - (200)), 300, 400, 20);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(((Game.getFrame().getWidth() / 4) - (200)) + 1, 300 + 1, (int) (percent * 4) - 1, 20 - 1);
	}
	
	public static void setInfo(String s, double i) {
		text = s;
		percent = i;
		SideScroller.getGame().render();
	}

	public void keyPressed(int k) {}

	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}
