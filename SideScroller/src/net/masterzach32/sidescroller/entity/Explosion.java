package net.masterzach32.sidescroller.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;

public class Explosion {
	
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	
	private int width;
	private int height;
	
	private Animation animation;
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		
		try {
			BufferedImage spritesheet = Assets.getImageAsset("explosion");
			
			sprites = new BufferedImage[6];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(70);
	}
	
	public void tick() {
		animation.tick();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	/**
	 * Should this be removed?
	 * @return
	 */
	public boolean shouldRemove() { 
		return remove; 
	}
	
	/**
	 * Sets the map postition of the explosion
	 * @param x
	 * @param y
	 */
	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}
	
	public void render(Graphics2D g) {
		g.drawImage(animation.getImage(), x + xmap - width / 2, y + ymap - height / 2, null);
	}
}