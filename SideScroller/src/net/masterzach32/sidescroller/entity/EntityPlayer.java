package net.masterzach32.sidescroller.entity;

import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.enemy.Enemy;
import net.masterzach32.sidescroller.entity.enemy.Slugger;
import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.*;
import net.masterzach32.sidescroller.util.LogHelper;
import net.masterzach32.sidescroller.util.Save;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class EntityPlayer extends MapObject {
	
	// player stuff
	private float health;
	private float maxHealth;
	private float exp;
	private float maxExp;
	private int damage;
	private int level;
	//private double levelMultiplier = 0.1;
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
	
	public EntityPlayer(TileMap tm) {
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
		level = 1;
		maxExp = 100;
		fire = maxFire = 2500;
		
		damage = 10;
		
		fireCost = 400;
		fireBallDamage = (int)(damage * 0.8);
		fireBalls = new ArrayList<FireBall>();
		explosions = new ArrayList<Explosion>();
		
		scratchDamage = (int)(damage * 1.5);
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
		sfx.put("jump", new AudioPlayer(Assets.getAudioAsset("jump")));
		sfx.put("scratch", new AudioPlayer(Assets.getAudioAsset("scratch")));
		sfx.put("fire", new AudioPlayer(Assets.getAudioAsset("fire")));
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
	
	public float getExp() {
		return exp;
	}

	public void setExp(float exp) {
		this.exp = exp;
	}

	public float getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(float maxExp) {
		this.maxExp = maxExp;
	}

	public int getLevel() {
		return level;
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
					if(e instanceof Slugger)
						e.hit((int) (fireBallDamage * 1.5), "FireBall", this);
					else
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
		//LogHelper.logInfo("[COMBAT] " + this.getClass().getSimpleName() + " hit for " + damage + " damage from " + type + " by " + source.getClass().getSimpleName());
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
		
		// cannot move while attacking, except in air
		if((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			sfx.get("jump").play();
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
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		// fireball attack
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) {
				if(SideScroller.isMouseOnScreen()) {
					//fire -= fireCost;
					Point p = Game.getFrame().getMousePosition();
					double px = p.x;
					double py = p.y;
					//LogHelper.logInfo(px + "-" + py);
					FireBall fb = new FireBall(tileMap, px, py, facingRight);
					fb.setPosition(x, y);
					fireBalls.add(fb);
				}
			} else {
				LogHelper.logInfo("Not Enough Fire!");
			}
		}
		
		// update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).tick();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
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
		
		// check done flinching
		if(flinching) {
			long elapsed =(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// set animation
		if(scratching) {
			if(currentAction != SCRATCHING) {
				sfx.get("scratch").play();
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		} else if(firing) {
			if(currentAction != FIREBALL) {
				sfx.get("fire").play();
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		} else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			} else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		} else if(dy < 0) {
			if(currentAction != JUMPING) {
				sfx.get("jump").play();
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
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
		
		animation.tick();
		
		// set direction
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
		
		// check to see if the player is dead or not
		if(health == 0) this.dead = true;
		if(health > 0) {
			this.dead = false;
			if(health < maxHealth){
				health += 0.01;
			}
		}
		
		if(exp >= maxExp) {
			levelUp();
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		// draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).render(g);
		}
			
		// draw player
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				// draw explosions
				for(int i = 0; i < explosions.size(); i++) {
					explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
					explosions.get(i).render(g);
				}
				return;
			}
		}
		super.render(g);
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).render(g);
		}
	}
	
	private void levelUp() {
		level += 1;
		maxExp += maxExp*1.1;
		damage = (int)(damage*1.25);
		scratchDamage = (int)(damage*1.5);
		fireBallDamage = (int)(damage*0.8);
	}
	
	public void writeSaveFile() {
		String path = "/Assets/saves/player.txt";
		String[] save = new String[16];
		save[0] = "" + health;
		save[1] = "" + maxHealth;
		save[2] = "" + exp;
		save[3] = "" + maxExp;
		save[4] = "" + level;
		save[5] = "" + fire;
		save[6] = "" + maxFire;
		Save.writeToSave(path, save);
	}
	
	public void loadSave() {
		String path = "/Assets/saves/player.txt";
		String[] save = Save.readFromSave(path);
		if(save == null) return;
		health = Integer.parseInt(save[0]);
		maxHealth = Integer.parseInt(save[1]);
		exp = Integer.parseInt(save[2]);
		maxExp = Integer.parseInt(save[3]);
		level = Integer.parseInt(save[4]);
		fire = Integer.parseInt(save[5]);
		maxFire = Integer.parseInt(save[6]);
	}
}