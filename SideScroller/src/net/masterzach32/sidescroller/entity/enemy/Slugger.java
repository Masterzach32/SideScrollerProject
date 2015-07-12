package net.masterzach32.sidescroller.entity.enemy;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.*;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Slugger extends Enemy {
	
	private BufferedImage[] sprites;
	
	private double b0 = 30;

	public Slugger(TileMap tm, int level) {
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = (8) + (6*level);
		damage = (3) + (3*level);
		
		exp = (10);
		
		armor = 70 + (5*level);
		damageMultiplier = (double) (100) / (100 + armor);
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("slugger");
			
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		explosions = new ArrayList<Explosion>();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}
	
	private void getNextPosition() {
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	public void tick() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).tick();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		} else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		// update animation
		animation.tick();
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		super.render(g);
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			explosions.get(i).render(g);
		}
		
		double h0 = health / maxHealth;
		double h1 = h0 * 30;
		
		if(h1 >= b0) b0 = h1;
		if(h1 < b0) b0 -= .7;
		
		// health bar
		g.setColor(new Color(255, 0, 0));
		g.drawRect((int)(x + xmap - 30 / 2), (int)(y + ymap - height / 2) + 5, (int) 30, 3);
		g.setColor(new Color(200, 0, 0));
		g.fillRect((int)(x + xmap - 30 / 2), (int)(y + ymap - height / 2) + 5, (int) b0, 3);
		g.setColor(new Color(255, 0, 0));
		g.fillRect((int)(x + xmap - 30 / 2), (int)(y + ymap - height / 2) + 5, (int) h1, 3);
	}
}