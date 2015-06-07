package net.masterzach32.sidescroller.assets.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.EntityPlayer;

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
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(player.getOrbCurrentCd() > 0) g.drawString("" + (player.getOrbCurrentCd() / 60 + 1), 0, 12);
		else g.drawString("" + (player.getOrbCurrentCd() / 60), 0, 12);
		g.drawString((int) (player.getHealth()) + "/" + (int) (player.getMaxHealth()), 30, 30);
		g.drawString((int) (player.getShield()) + "/" + (int) (player.getMaxShield()), 30, 50);
		g.drawString(player.getLevel() + " - " + (int) player.getExp() + "/" + (int) player.getMaxExp(), 1, 70);
	}
}