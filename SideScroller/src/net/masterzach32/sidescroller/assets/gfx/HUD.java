package net.masterzach32.sidescroller.assets.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.EntityPlayer;

public class HUD {
	
	private EntityPlayer player;
	
	private BufferedImage image;
	private Font font, healthFont, shieldFont;
	
	double b0 = 51, b1 = 51;
	
	public HUD(EntityPlayer p) {
		player = p;
		try {
			image = Assets.getImageAsset("hud");
			
			font = new Font("Arial", Font.PLAIN, 14);
			healthFont = new Font("Arial", Font.PLAIN, 11);
			shieldFont = new Font("Arial", Font.PLAIN, 10);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics2D g) {
		g.drawImage(image, 0, 15, null);
		
		double h0 = player.getHealth() / player.getMaxHealth();
		double h1 = h0 * 51;
		double m0 = player.getShield() / player.getMaxShield();
		double m1 = m0 * 51;
		
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
		g.fillRect(17, 26, (int) b1, 5);
		g.setColor(Color.BLUE);
		g.fillRect(17, 26, (int) m1, 5);
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(player.getOrbCurrentCd() > 0) g.drawString("" + (player.getOrbCurrentCd() / 60 + 1), 0, 12);
		else g.drawString("" + (player.getOrbCurrentCd() / 60), 0, 12);
		g.drawString(player.getLevel() + " - " + (int) player.getExp() + "/" + (int) player.getMaxExp(), 1, 70);
		g.setFont(font);
		g.drawString((int) (player.getHealth()) + "/" + (int) (player.getMaxHealth()), 16, 29);
		//g.setFont(shieldFont);
		//g.drawString((int) (player.getShield()) + "/" + (int) (player.getMaxShield()), 42, 30);	
	}
}