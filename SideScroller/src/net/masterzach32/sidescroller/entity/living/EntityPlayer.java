package net.masterzach32.sidescroller.entity.living;

import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.enemy.Enemy;
import net.masterzach32.sidescroller.tilemap.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class EntityPlayer extends EntityLiving {
	
	// player stuff
	private boolean inCombat;
	private int combatTimer;
	private double exp;
	private double maxExp;
	private int level;
	private int orbCurrentCd;
	private int orbCd;
	public int rewindCd;
	
	// fireball
	private boolean spawning;
	private int soldierDamage;
	private ArrayList<Soldier> soldiers;
	
	// scratch
	private boolean attacking;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	
	// animation actions
	private static final int IDLE = 0, WALKING = 1, JUMPING = 2, FALLING = 3, GLIDING = 4, SOLDIER = 5, ATTACKING = 6;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public EntityPlayer(TileMap tm) {
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.4;
		setMaxSpeed(1.6);
		stopSpeed = 0.5;
		fallSpeed = 0.15;
		maxFallSpeed = 10.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 21;
		healthRegen = (double) (maxHealth * 0.0002);
		shield = maxShield = 9;
		shieldRegen = (double)  (maxShield * 0.004);
		level = 1;
		maxExp = 100;
		rewindCd = 300;
		
		damage = 6;
		
		orbCd = 360;
		orbCurrentCd = 0;
		soldiers = new ArrayList<Soldier>();
		
		scratchRange = 50;
		
		resetStats(false);
		
		dead = false;
		
		healthBar = new HealthBar(this, 40, 4, new Color(0, 170, 0));
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("player");
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					if(i != ATTACKING) {
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
		sfx.put("spawn", new AudioPlayer(Assets.getAudioAsset("spawn")));
	}
	
	public boolean isInCombat() {
		return inCombat;
	}
	
	public double getExp() {
		return exp;
	}

	public void setExp(double exp) {
		this.exp = exp;
	}

	public double getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(double maxExp) {
		this.maxExp = maxExp;
	}

	public int getLevel() {
		return level;
	}

	public int getOrbCurrentCd() { 
		return orbCurrentCd; 
	}
	
	public int getOrbCd() { 
		return orbCd; 
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setDead(MapObject source) {
		this.dead = true;
		currentAction = IDLE;
		attacking = false;
		flinching = false;
	}
	
	public void setSpawning() { 
		spawning = true;
	}
	
	public void setAttacking() {
		attacking = true;
	}
	
	public void setGliding(boolean b) { 
		gliding = b;
	}
	
	/**
	 * Spawns the player at the beginning of the current level with full health.
	 */
	public void respawn() {
		this.dead = false;
		health = maxHealth;
		if(rewindCd < 250) rewindCd = 250;
		setPosition(100, 100);
		setLeft(false);
		setRight(false);
		setUp(false);
		setDown(false);
		setJumping(false);
		setGliding(false);
	}
	
	/**
	 * Checks to see if the attack succeeded
	 * @param enemies
	 */
	public void checkAttack(ArrayList<Enemy> enemies) {
		scratchDamage = (int)(8 + damage * 0.9);
		soldierDamage = (int)(4 + damage * 1.2);
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			// scratch attack
			if(attacking) {
				if(facingRight) {
					if(e.intersects(new Rectangle((int) (x), (int) (y - height / 2 + (height - cheight) / 2), scratchRange, cheight))) {
						combatTimer = 300;
						e.hit(scratchDamage, false, false, "Scratch", this);
					}
				} else {
					if(e.intersects(new Rectangle((int) (x - scratchRange), (int) (y - height / 2 + (height - cheight) / 2), scratchRange, cheight))) {
						combatTimer = 300;
						e.hit(scratchDamage, false, false, "Scratch", this);
					}
				}
			}
			
			// soldiers
			for(int j = 0; j < soldiers.size(); j++) {
				if(soldiers.get(j).isAttacking()) {
					combatTimer = 300;
					e.hit(soldierDamage, false, false, "Orb", soldiers.get(i));
				}
			}
						
			// check enemy collision
			if(intersects(e)) {
				combatTimer = 300;
				hit(e.getDamage() / 2, false, false, "Collision", e);
			}	
		}
	}
	
	/**
	 * Deals damage to the entity
	 * @param damage
	 * @param source (should always be <code>this</code>)
	 * @return true if attack succeeded
	 */
	public boolean hit(double damage, boolean ignoreShield, boolean ignoreFlinching, String type, MapObject source) {
		combatTimer = 300;
		return super.hit(damage, ignoreShield, ignoreFlinching, type, source);
	}
	
	/**
	 * Creates a new effect and adds it to the player
	 * @param type
	 * @param strength
	 * @param duration
	 */
	public void addEffect(EntityLiving source, int type, int strength, int duration) {
		super.addEffect(source, type, strength, duration);
		resetStats(false);
	}
	
	private void getNextPosition() {
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
		if((currentAction == ATTACKING || currentAction == SOLDIER) && !(jumping || falling)) {
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
		if(!isDead()) {
			// update position
			getNextPosition();
			super.tick();
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
		
			// check attack has stopped
			if(currentAction == ATTACKING) {
				if(animation.hasPlayedOnce()) attacking = false;
			} if(currentAction == SOLDIER) {
				if(animation.hasPlayedOnce()) spawning = false;
			}
		
			// orb attack
			if(spawning && currentAction != SOLDIER) {
				if(orbCurrentCd == 0) {
					Soldier soldier = null;
					if(facingRight)
						soldier = new Soldier(tileMap, 0, 0, level, this);
					if(!facingRight)
						soldier = new Soldier(tileMap, 0, 0, level, this);
					if(soldier != null) {
						soldier.setPosition(x, y);
						soldiers.add(soldier);
					}
				} else {
					
				}
			}
		
			// check done flinching
			if(flinching) {
				long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
				if(elapsed > 500) {
					flinching = false;
				}
			}
		
			// set animation
			if(attacking) {
				if(currentAction != ATTACKING) {
					sfx.get("scratch").play();
					currentAction = ATTACKING;
					animation.setFrames(sprites.get(ATTACKING));
					animation.setDelay(50);
					width = 60;
				}
			} else if(spawning) {
				if(currentAction != SOLDIER) {
					sfx.get("spawn").play();
					currentAction = SOLDIER;
					animation.setFrames(sprites.get(SOLDIER));
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
		
			// set direction
			if(currentAction != ATTACKING && currentAction != SOLDIER) {
				if(right) facingRight = true;
				if(left) facingRight = false;
			}
			
			// check to see if the player is dead or not
			if(health == 0) this.setDead(null);
			if(health > 0) {
				dead = false;
				if(health < maxHealth) {
					health += healthRegen;
				}
				if(shield < maxShield) {
					if(!inCombat) shield += shieldRegen;
				}
			}
			if(health > maxHealth) {
				health = maxHealth;
			}
			if(shield > maxShield) {
				shield = maxShield;
			}
		}
		
		// update cooldowns
		if(combatTimer > 0) combatTimer--;
		if(combatTimer > 0) inCombat = true;
		if(combatTimer == 0) inCombat = false;
		
		// update orbs
		for(int i = 0; i < soldiers.size(); i++) {
			soldiers.get(i).tick();
			if(soldiers.get(i).shouldRemove()) {
				soldiers.remove(i);
				i--;
			}
		}
		
		if(exp >= maxExp) {
			resetStats(true);
		}
		
		resetStats(false);
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		// draw orbs
		for(int i = 0; i < soldiers.size(); i++) {
			soldiers.get(i).render(g);
		}
		
		super.render(g);
		
		if(MapObject.isHitboxEnabled()) {
			if(attacking) {
				g.setColor(Color.YELLOW);
				if(facingRight) {
					g.drawRect((int)(x + xmap), (int)(y + ymap - height / 2 + (height - cheight) / 2), scratchRange, cheight);
				} else {
					g.drawRect((int)(x + xmap  - scratchRange), (int)(y + ymap - height / 2 + (height - cheight) / 2), scratchRange, cheight);
				}
			}
		}
		g.setColor(Color.WHITE);
	}
	
	private void resetStats(boolean levelUp) {
		if(levelUp) {
			level += 1;
			maxExp = 75 + 25 * level;
			exp = 0;
			damage += 4;
			orbCd -= 15;
			maxHealth += 7;
			health += 7;
			healthRegen = (double) (maxHealth * 0.0001);
			maxShield += 3;
			shieldRegen = (double) (maxShield * 0.004);
		}
	}
}