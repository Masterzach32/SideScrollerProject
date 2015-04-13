package net.masterzach32.sidescroller.assets.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.EntityPlayer;
import net.masterzach32.sidescroller.main.SideScroller;

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
		g.drawString("FPS: " + SideScroller.FPS, 1, 11);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString((int) (player.getHealth()) + "/" + (int) (player.getMaxHealth()), 30, 30);
		g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30, 50);
	}
}