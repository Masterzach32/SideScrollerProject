package net.masterzach32.sidescroller.entity.living.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.EntityPlayer;
import net.masterzach32.sidescroller.entity.living.HealthBar;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Swordman extends Enemy {
	
	private boolean attacking;
	
	private int attackRange;
	private int attackCd = 0;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	private static final int IDLE = 0, WALKING = 1, ORB = 5, SCRATCHING = 6;

	public Swordman(TileMap tm, int level) {
		super(tm, level);
		moveSpeed = 0.3;
		setMaxSpeed(1.1);
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		sight = 250;
		hsight = 36;
		
		health = maxHealth = (8) + (8*level);
		damage = (3) + (4*level);
		
		exp = (15);
		
		armor = 35 + (5*level);
		damageMultiplier = (double) (100) / (100 + armor);
		
		attacking = false;
		
		attackRange = 30;
		
		healthBar = new HealthBar(this, 30, 3, new Color(255, 0, 0));
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("player");
					
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					if(i != SCRATCHING) {
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					} else {
						bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}
				}
				sprites.add(bi);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
				
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);

		facingRight = true;
		right = true;
	}
	
	/**
	 * Checks to see if the attack succeeded
	 * @param enemies
	 */
	public void checkAttack() {
		EntityPlayer p = LevelState.getPlayer();
		if(facingRight) {
			if(p.intersects(new Rectangle((int) (x), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
				p.hit(damage, false, false, "Sword Swing", this);
			}
		} else {
			if(p.intersects(new Rectangle((int) (x - attackRange), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
				p.hit(damage, false, false, "Sword Swing", this);
			}
		}
	}

	public void getNextPosition() {
		// movement
		if(LevelState.getPlayer().intersects(new Rectangle((int) (x - attackRange * 1.5 / 2), (int) (y - hsight / 2), (int) (attackRange * 1.5), hsight))) {
			// player within attacking range
			dx = 0;
			if(attackCd == 0) {
				attacking = true;
				attackCd = 90;
				left = false;
				right = false;
			}
		} else if(LevelState.getPlayer().intersects(new Rectangle((int) (x - sight / 2), (int) (y - hsight / 2), sight, hsight))) {
			// player within sight
			if(LevelState.getPlayer().getx() < this.getx()) {
				right = false;
				left = true;
				facingRight = false;
			} else if (LevelState.getPlayer().getx() > this.getx()) {
				right = true;
				left = false;
				facingRight = true;
			}
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
		} else {
			// player cannot be seen
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
			
			if(left) {
				dx = -moveSpeed;
			} else if(right) {
				dx = moveSpeed;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
		
		// cannot move while attacking, except in air
		if((currentAction == SCRATCHING || currentAction == ORB) && !(jumping || falling)) {
			dx = 0;
		}
		
		if(attackCd > 0) attackCd--;
	}
	
	public void tick() {
		if(dead) return;
		// update position
		super.tick();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) attacking = false;
		}
		
		// set animation
		if(attacking) {
			checkAttack();
			if(currentAction != SCRATCHING) {
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		} else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		} else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		// set direction
		if(currentAction != SCRATCHING && currentAction != ORB) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
	}
	
	public void render(Graphics2D g) {
		super.render(g);
		
		if(MapObject.isHitboxEnabled()) {
			g.setColor(Color.WHITE);
			g.draw(new Rectangle((int) (x + xmap - sight / 2), (int) (y + ymap - hsight / 2), sight, hsight));
			g.draw(new Rectangle((int) (x + xmap - attackRange * 1.5 / 2), (int) (y + ymap - hsight / 2), (int) (attackRange * 1.5), hsight));
			if(attacking) {
				g.setColor(Color.YELLOW);
				if(facingRight) {
					g.drawRect((int)(x + xmap), (int)(y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
				} else {
					g.drawRect((int)(x + xmap - attackRange), (int)(y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
				}
			}
		}
		g.setColor(Color.WHITE);
	}
}