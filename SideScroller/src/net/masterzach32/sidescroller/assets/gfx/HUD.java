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
		
		double h0 = player.getHealth() / player.getMaxHealth();
		double h1 = h0 * 51;
		g.fillRect(17, 18, (int) h1, 13);
		
		g.setColor(Color.BLUE);
		double m0 = player.getShield() / player.getMaxShield();
		double m1 = m0 * 51;	
		g.fillRect(17, 39, (int) m1, 13);
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(player.getOrbCurrentCd() > 0) g.drawString("" + (player.getOrbCurrentCd() / 60 + 1), 0, 12);
		else g.drawString("" + (player.getOrbCurrentCd() / 60), 0, 12);
		g.drawString((int) (player.getHealth()) + "/" + (int) (player.getMaxHealth()), 20, 30);
		g.drawString((int) (player.getShield()) + "/" + (int) (player.getMaxShield()), 20, 50);
		g.drawString(player.getLevel() + " - " + (int) player.getExp() + "/" + (int) player.getMaxExp(), 1, 70);
		
		/*if(LevelState.getPlayer().isInCombat()) {
			g.setColor(new Color(0, 170, 0));

			double h2 = player.getHealth() / player.getMaxHealth();
			double h3 = h2 * 35;
			g.fillRect(player.getx(), player.gety(), (int) h3, 5);
			
			g.setColor(Color.BLUE);
			double m2 = player.getShield() / player.getShield();
			double m3 = m2 * 35;	
			g.fillRect(player.getx() - 5, player.gety() - 5, (int) m3, 13);
			g.setFont(font);
			g.setColor(Color.WHITE);
		}*/
	}
}