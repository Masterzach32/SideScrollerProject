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
	
	private float dhlength = 51;
	
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
		
		float health = player.getHealth();
		float maxHealth = player.getMaxHealth();
		float shield = player.getShield();
		
		float maxTotal;
		if(health + shield < maxHealth) maxTotal = maxHealth;
		else maxTotal = maxHealth + (health + shield - maxHealth);
		float healthPercent = health / maxTotal;
		float shieldPercent = shield / maxTotal;
			
		float hlength = healthPercent * 51;
		float slength = shieldPercent * 51;
		float length = hlength + slength;
		
		//if(h1 >= b0) b0 = h1;
		//if(h1 < b0) b0 -= .7;
		//if(m1 >= b1) b1 = m1;
		//if(m1 < b1) b1 -= .7;
		
		// damage bar
		if(length >= dhlength) dhlength = (float) (length - 1);
		if(length < dhlength) dhlength -= .7;
		
		// health bar
		g.setColor(new Color(200, 0, 0));
		g.fillRect(17, 18, (int) dhlength, 13);
		g.setColor(new Color(0, 170, 0));
		g.fillRect(17, 18, (int) hlength, 13);
		// mana bar
		g.setColor(Color.BLUE);
		g.fillRect((int) (17 + hlength), 18, (int) slength, 13);
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(!player.isDead()) g.drawString("Orb: " + Utilities.getCooldown(player.getOrbCurrentCd()) + " Rewind: " + Utilities.getCooldown(player.rewindCd), 0, 12);
		if(player.isDead()) g.drawString("Death Timer: " + Utilities.getCooldown(LevelState.j) + " Orb: " + Utilities.getCooldown(player.getOrbCurrentCd()) + " Rewind: " + Utilities.getCooldown(player.rewindCd), 0, 12);
		g.drawString(player.getLevel() + " - " + (int) player.getExp() + "/" + (int) player.getMaxExp(), 1, 70);
		g.setFont(font);
		g.drawString((int) (player.getHealth()) + "/" + (int) (player.getMaxHealth()), 16, 29);
	}
}