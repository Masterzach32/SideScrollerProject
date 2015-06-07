package net.masterzach32.sidescroller.assets.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.EntityPlayer;
import net.masterzach32.sidescroller.gamestate.LevelState;

public class HUD {
	
	private EntityPlayer player;
	
	private BufferedImage image;
	private Font font;
	
	public HUD(EntityPlayer p) {
		player = p;
		try {
			image = Assets.getImageAsset("hud");
			
			font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics2D g) {
		g.drawImage(image, 0, 15, null);
		g.setColor(new Color(0, 170, 0));

		double h0 = LevelState.getPlayer().getHealth();
		double h1 = LevelState.getPlayer().getMaxHealth();
		double h2 = h0 / h1;
		double h3 = h2 * 51;
		g.fillRect(17, 18, (int) h3, 13);
		
		g.setColor(Color.BLUE);
		double m0 = LevelState.getPlayer().getShield();
		double m1 = LevelState.getPlayer().getMaxShield();
		double m2 = m0 / m1;
		double m3 = m2 * 51;	
		g.fillRect(17, 39, (int) m3, 13);
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(player.getOrbCurrentCd() > 0) g.drawString("" + (player.getOrbCurrentCd() / 60 + 1), 0, 12);
		else g.drawString("" + (player.getOrbCurrentCd() / 60), 0, 12);
		g.drawString((int) (player.getHealth()) + "/" + (int) (player.getMaxHealth()), 20, 30);
		g.drawString((int) (player.getShield()) + "/" + (int) (player.getMaxShield()), 20, 50);
		g.drawString(player.getLevel() + " - " + (int) player.getExp() + "/" + (int) player.getMaxExp(), 1, 70);
	}
}