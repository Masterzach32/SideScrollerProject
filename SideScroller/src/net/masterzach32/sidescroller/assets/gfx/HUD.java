package net.masterzach32.sidescroller.assets.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.living.EntityPlayer;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.util.Utilities;

public class HUD {
	
	private EntityPlayer player;
	
	private BufferedImage image;
	private Font font;
	
	double b0 = 31, b1 = 20, hx = 31, mx = 20;
	
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
		
		double h0 = player.getHealth() / player.getMaxHealth();
		double h1 = h0 * hx;
		double m0 = player.getShield() / player.getMaxShield();
		double m1 = m0 * mx;
		
		if(player.getHealth() == player.getMaxHealth()) {
			if((int) (h1 + m1) <= 51) {
				int f = (int) (mx - m1);
				h1 += f;
			}
		}
		
		if(h1 >= b0) b0 = h1;
		if(h1 < b0) b0 -= .7;
		if(m1 >= b1) b1 = m1;
		if(m1 < b1) b1 -= .7;
		
		// health bar
		g.setColor(new Color(200, 0, 0));
		g.fillRect(17, 18, (int) b0, 13);
		g.setColor(new Color(0, 170, 0));
		g.fillRect(17, 18, (int) h1, 13);
		// mana bar
		g.setColor(new Color(200, 0, 0));
		g.fillRect((int) (17 + b0), 18, (int) b1, 13);
		g.setColor(Color.BLUE);
		g.fillRect((int) (17 + h1), 18, (int) m1, 13);
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(!player.isDead()) g.drawString("Orb: " + Utilities.getCooldown(player.getOrbCurrentCd()) + " Rewind: " + Utilities.getCooldown(player.rewindCd), 0, 12);
		if(player.isDead()) g.drawString("Death Timer: " + Utilities.getCooldown(LevelState.j) + " Orb: " + Utilities.getCooldown(player.getOrbCurrentCd()) + " Rewind: " + Utilities.getCooldown(player.rewindCd), 0, 12);
		g.drawString(player.getLevel() + " - " + (int) player.getExp() + "/" + (int) player.getMaxExp(), 1, 70);
		g.setFont(font);
		g.drawString((int) (player.getHealth()) + "/" + (int) (player.getMaxHealth()), 16, 29);
	}
}