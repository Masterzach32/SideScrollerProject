package net.masterzach32.sidescroller.entity.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.gamestate.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Boss extends Enemy {
	
	private BufferedImage[] sprites;
	
	private double b0 = 60;

	public Boss(TileMap tm, int level) {
		super(tm);
		moveSpeed = 0.1;
		maxSpeed = 0.1;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = (200) + (75*level);
		damage = (10) + (5*level);
		
		exp = (25) + (30*level);
		
		armor = -40;
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
	
	public void hit(int damage, String type, MapObject source) {
		if(dead || flinching) return;
		explosions.add(new Explosion(this.getx(), this.gety()));
		damage = (int) (damage * damageMultiplier);
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	private void getNextPosition() {
		if(LevelState.getPlayer().getx() < this.x) {
			right = false;
			left = true;
			facingRight = false;
		} else if (LevelState.getPlayer().getx() > this.x) {
			right = true;
			left = false;
			facingRight = true;
		}
	}
	
	public void tick() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
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
		
		double h0 = health / maxHealth;
		double h1 = h0 * 60;
		
		if(h1 >= b0) b0 = h1;
		if(h1 < b0) b0 -= .7;
		
		g.setColor(new Color(200, 0, 0));
		g.fillRect((int)(x + xmap - width / 2) - 15, (int)(y + ymap - height / 2) + 2, (int) b0, 8);
		// health bar
		g.setColor(new Color(255, 0, 0));
		g.fillRect((int)(x + xmap - width / 2) - 15, (int)(y + ymap - height / 2) + 2, (int) h1, 8);
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				// draw explosions
				for(int i = 0; i < explosions.size(); i++) {
					explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
					explosions.get(i).render(g);
				}
				return;
			}
		}	
		super.render(g);
		
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			explosions.get(i).render(g);
		}
	}
}
