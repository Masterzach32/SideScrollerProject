package net.masterzach32.sidescroller.entity.living;

import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.Orb;
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
	
	private int[] x4 = new int[240], y4 = new int[240], health4 = new int[240];
	
	// fireball
	private boolean firing;
	private int orbDamage;
	private ArrayList<Orb> orbs;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	private boolean rewind = false;
	int r = 0;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	
	// animation actions
	private static final int IDLE = 0, WALKING = 1, JUMPING = 2, FALLING = 3, GLIDING = 4, ORB = 5, SCRATCHING = 6;
	
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
		orbs = new ArrayList<Orb>();
		
		scratchRange = 30;
		
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
		scratching = false;
		flinching = false;
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
		scratchDamage = (int)(10 + damage * 0.9);
		orbDamage = (int)(4 + damage * 1.2);
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			// scratch attack
			if(scratching) {
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
			
			// orbs
			for(int j = 0; j < orbs.size(); j++) {
				if(orbs.get(j).intersects(e)) {
					if(orbs.get(j).isHit(e)) return;
					orbs.get(j).addToHitList(e);
					combatTimer = 300;
					e.hit(orbDamage, false, true, "Orb", this);
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
	 * Executes the player's rewind ability
	 */
	public void rewind() {
		if(rewindCd > 0) return;
		rewind = true;
		flinching = true;
		flinchTimer = System.nanoTime();
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
		if((currentAction == SCRATCHING || currentAction == ORB) && !(jumping || falling)) {
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
			if(currentAction == SCRATCHING) {
				if(animation.hasPlayedOnce()) scratching = false;
			} if(currentAction == ORB) {
				if(animation.hasPlayedOnce()) firing = false;
			}
		
			// orb attack
			if(firing && currentAction != ORB) {
				if(orbCurrentCd == 0) {
					orbCurrentCd = orbCd;
					Orb orb = null;
					if(facingRight)
						orb = new Orb(tileMap, true);
					if(!facingRight)
						orb = new Orb(tileMap, false);
					if(orb != null) {
						orb.setPosition(x, y);
						orbs.add(orb);
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
			if(scratching) {
				if(currentAction != SCRATCHING) {
					sfx.get("scratch").play();
					currentAction = SCRATCHING;
					animation.setFrames(sprites.get(SCRATCHING));
					animation.setDelay(50);
					width = 60;
				}
			} else if(firing) {
				if(currentAction != ORB) {
					sfx.get("fire").play();
					currentAction = ORB;
					animation.setFrames(sprites.get(ORB));
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
			if(currentAction != SCRATCHING && currentAction != ORB) {
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
		
		if(rewindCd > 0) rewindCd--;
		
		if(orbCurrentCd > 0) orbCurrentCd--;
		if(orbCurrentCd > orbCd) orbCurrentCd = orbCd;
		
		// update orbs
		for(int i = 0; i < orbs.size(); i++) {
			orbs.get(i).tick();
			if(orbs.get(i).removeOrb()) {
				orbs.remove(i);
				i--;
			}
		}
		
		if(exp >= maxExp) {
			resetStats(true);
		}
		
		// stores the players x, y, and health for the past 4 seconds
		for(int i = 239; i > 0; i--) {
			if(rewindCd >= 240) {
				x4[i] = (int) x;
				y4[i] = (int) y;
				health4[i] = (int) health;
			} else if(!rewind) {
				x4[i] = x4[i-1];
				y4[i] = y4[i-1];
				health4[i] = health4[i-1];
			}
		}
		
		// does the rewind ability while active
		if(rewind) {
			r += 4;
			if(r <= 239) {
				x = x4[r];
				y = y4[r];
				health = health4[r];
			}
			if(r >= 239) {
				rewind = false;
				r = 0;
				rewindCd = 2000;
			}
		}
		
		x4[0] = (int) x;
		y4[0] = (int) y;
		health4[0] = (int) health;
		
		resetStats(false);
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		g.setColor(Color.BLUE);
		if(rewindCd < 240) {
			g.drawRect((int)(x4[239] + xmap - 30 / 2), (int)(y4[239] + ymap - height / 2), 30, 30);
			for(int i = 0; i < 240; i++) g.drawLine((int)(x4[i] + xmap), (int)(y4[i] + ymap), (int)(x4[i] + xmap), (int)(y4[i] + ymap));
		}
		
		// draw orbs
		for(int i = 0; i < orbs.size(); i++) {
			orbs.get(i).render(g);
		}
		
		super.render(g);
		
		if(MapObject.isHitboxEnabled()) {
			if(scratching) {
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