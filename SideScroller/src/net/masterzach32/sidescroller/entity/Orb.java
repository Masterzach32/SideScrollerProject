package net.masterzach32.sidescroller.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;
import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.living.*;

public class Orb extends Projectile {
	
	private boolean hit;
	private BufferedImage[] sprites;
	private double range, range2, r0, r1, r2;
	private int stage;
	
	private ArrayList<EntityLiving> hits;
	
	public Orb(TileMap tm, boolean right) {
		super(tm);
		
		x = LevelState.getPlayer().getx();
		y = LevelState.getPlayer().gety();
		
		range = 140;
		range2 = 30;
		stage = 1;
		
		facingRight = right;
		moveSpeed = 7.5;
		
		width = 30;
		height = 30;
		cwidth = 16;
		cheight = 16;
		
		hits = new ArrayList<EntityLiving>();
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("orb");
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Call this on an entity that has been hit
	 */
	public void setHit() {
		if(hit) return;
		hit = true;
	}

	private void getNextPosition() {
		if(facingRight){
			if(stage == 1) {
				dx = moveSpeed;
				r0 += moveSpeed;
				if(r0 >= range) {
					stage = 2;
					r0 = 0;
				}
			}
			if(stage == 2) {
				dx = (moveSpeed/22);
				r1 += (moveSpeed/22);
				if(r1 >= range2) {
					stage = 3;
					r1 = 0;
					removeHits();
				}
			}
			if(stage == 3) {
				dx = -moveSpeed;
				r2 += moveSpeed; 
				if(r2 >= (range + range2)) {
					stage = 0;
					r2 = 0;
				}
			}
		} else {
			if(stage == 1) {
				dx = -moveSpeed;
				r0 += moveSpeed;
				if(r0 >= range) {
					stage = 2;
					r0 = 0;
				}
			}
			if(stage == 2) {
				dx = -(moveSpeed/22);
				r1 += (moveSpeed/22);
				if(r1 >= range2) {
					stage = 3;
					r1 = 0;
					removeHits();
				}
			}
			if(stage == 3) {
				dx = moveSpeed;
				r2 += moveSpeed; 
				if(r2 >= (range + range2)) {
					stage = 0;
					r2 = 0;
				}
			}
		}
	}
	
	public int getStage() {
		return stage;
	}
	
	public void addToHitList(EntityLiving entity) {
		hits.add(entity);
	}
	
	public boolean isHit(EntityLiving entity) {
		for(int i = 0; i < hits.size(); i++) {
			if(hits.get(i).equals(entity)) {
				return true;
			}
		}
		return false;
	}
	
	private void removeHits() {
		for(int i = 0; i < hits.size(); i++)
			hits.remove(i);
	}
	
	public void tick() {
		getNextPosition();
		//checkTileMapCollision();
		setPosition(x + dx, y);
		
		if(dx == 0 && !hit) {
			setHit();
		}
		
		animation.tick();
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
	}
	
	public boolean removeOrb() {
		if(stage == 0) return true;
		else return false;
	}
}
