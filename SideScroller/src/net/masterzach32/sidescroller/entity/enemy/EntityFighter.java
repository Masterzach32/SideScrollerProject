package net.masterzach32.sidescroller.entity.enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.FireBall;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.tilemap.TileMap;
import net.masterzach32.sidescroller.util.LogHelper;

public class EntityFighter extends Enemy {
	
	// player stuff
	private float health;
	private float maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<Explosion> explosions;
	private ArrayList<FireBall> fireBalls;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	
	// animation actions
	private static final int IDLE = 0, WALKING = 1, JUMPING = 2, FALLING = 3, GLIDING = 4, FIREBALL = 5, SCRATCHING = 6;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public EntityFighter(TileMap tm) {
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.4;
		maxSpeed = 1.5;
		stopSpeed = 0.5;
		fallSpeed = 0.15;
		maxFallSpeed = 10.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 50;
		fire = maxFire = 2500;
		
		fireCost = 400;
		fireBallDamage = 8;
		fireBalls = new ArrayList<FireBall>();
		explosions = new ArrayList<Explosion>();
		
		scratchDamage = 12;
		scratchRange = 35;
		
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
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer(Assets.getSoundAsset("jump")));
		sfx.put("scratch", new AudioPlayer(Assets.getSoundAsset("scratch")));
		sfx.put("fire", new AudioPlayer(Assets.getSoundAsset("fire")));
	}
	
	public float getHealth() { 
		return health; 
	}
	
	public float getMaxHealth() {
		return maxHealth; 
	}
	
	public void setHealth(int h) {
		health = h;
	}
	
	public int getFire() { 
		return fire; 
	}
	
	public int getMaxFire() { 
		return maxFire; 
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setFiring() { 
		firing = true;
	}
	
	public void setScratching() {
		scratching = true;
	}
	
	public void setGliding(boolean b) { 
		gliding = b;
	}
	
	/**
	 * Checks to see if the attack succeeded
	 * @param enemies
	 */
	public void checkAttack(ArrayList<Enemy> enemies) {
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			// scratch attack
			if(scratching) {
				if(facingRight) {
					if(e.getx() > x && e.getx() < x + scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
						e.hit(scratchDamage, "Scratch", this);
					}
				} else {
					if(e.getx() < x && e.getx() > x - scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
						e.hit(scratchDamage, "Scratch", this);
					}
				}
			}
			// fireballs
			for(int j = 0; j < fireBalls.size(); j++) {
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage, "FireBall", this);
					fireBalls.get(j).setHit();
					break;
				}
			}
			// check enemy collision
			if(intersects(e)) {
				hit(e.getDamage(), "Collision", e);
			}	
		}
	}
	
	/**
	 * Deals damage to the entity
	 * @param damage
	 * @param source (should always be <code>this</code>)
	 */
	public void hit(int damage, String type, MapObject source) {
		if(flinching) return;
		explosions.add(new Explosion(this.getx(), this.gety()));
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
		LogHelper.logInfo("[COMBAT] " + this.getClass().getSimpleName() + " hit for " + damage + " damage from " + type + " by " + source.getClass().getSimpleName());
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
		} else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			} else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			} 
		} 
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
		} else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			} else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;			
				}
			}
		}		
		// cannot move while attacking, except in air
		if((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling)) {
			dx = 0;
		}
				
		// jumping
		if(jumping && !falling) {
			//sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling) {
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics2D g) {
		
	}
}