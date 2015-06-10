package net.masterzach32.sidescroller.entity.enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.gamestate.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Boss extends Enemy {
	
	private BufferedImage[] sprites;

	public Boss(TileMap tm, int level) {
		super(tm);
		moveSpeed = 0.1;
		maxSpeed = 0.1;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 30;
		
		health = maxHealth = (275) + (25*level);
		damage = (10) + (5*level);
		
		exp = (25) + (30*level);
		
		armor = 20 + (10*level);
		damageMultiplier = (double) (100) / (100 + armor);
		
		// load sprites
			
		try {
			BufferedImage spritesheet = Assets.getImageAsset("boss");
			
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
	}
}
