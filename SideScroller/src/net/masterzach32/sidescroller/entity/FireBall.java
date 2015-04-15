package net.masterzach32.sidescroller.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class FireBall extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	private double px, py, sx, sy;
	private double slope;
	
	public FireBall(TileMap tm, double vx, double vy, boolean right) {
		super(tm);
		
		moveSpeed = 7.5;
		
		facingRight = right;
		if(facingRight) dx = moveSpeed;
		else dx = -moveSpeed;
		
		px = vx;
		py = vy;
		// calculate the slope
		sy = py - y;
		sx = px - x;
		slope = sy/sx;
		
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("fireball");
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spritesheet.getSubimage(i * width, height, width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Call this on an entity that has been hit
	 */
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	/**
	 * Should this be removed?
	 * @return
	 */
	public boolean shouldRemove() { 
		return remove; 
	}
	
	private void getNextPosition() {
		// use point slope formula to find next y value
		// (y-y1) = a(x-x1)
		// y = a(x-x1) + y1
		// x + dx = new x coordinate, px and py are original mouse point.
		dy = slope*(x+dx-px) + py;
	}
	
	public void tick() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) {
			setHit();
		}
		
		animation.tick();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
	}
}