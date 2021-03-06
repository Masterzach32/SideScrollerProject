package net.masterzach32.sidescroller.entity.living.enemy;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.*;
import net.masterzach32.sidescroller.entity.living.HealthBar;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Slugger extends Enemy {
	
	private BufferedImage[] sprites;

	public Slugger(TileMap tm, int level) {
		super(tm, level);
		
		moveSpeed = 0.3;
		setMaxSpeed(0.3);
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
		
		healthBar = new HealthBar(this, 30, 3, new Color(255, 0, 0));
		
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
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}
	
	public void getNextPosition() {
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -getMaxSpeed()) {
				dx = -getMaxSpeed();
			}
		} else if(right) {
			dx += moveSpeed;
			if(dx > getMaxSpeed()) {
				dx = getMaxSpeed();
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	public void tick() {
		if(dead) return;
		super.tick();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
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
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		super.render(g);
	}
}