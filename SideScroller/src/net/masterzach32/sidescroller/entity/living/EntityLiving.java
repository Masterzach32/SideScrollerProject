package net.masterzach32.sidescroller.entity.living;

import java.awt.Graphics2D;
import java.util.ArrayList;

import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.effects.Effect;
import net.masterzach32.sidescroller.tilemap.TileMap;
import net.masterzach32.sidescroller.util.LogHelper;

public abstract class EntityLiving extends MapObject {
	
	public double health, maxHealth, shield = 0, maxShield = 0;
	public double healthRegen;
	public double shieldRegen;
	public int exp, damage;
	
	public boolean stunned, knockedUp;
	public double stunTimer, backup;
	
	public int level;
	public boolean dead;
	
	public boolean flinching;
	public long flinchTimer;
	
	public ArrayList<Effect> effects;
	public ArrayList<Explosion> explosions;
	
	public HealthBar healthBar;

	public EntityLiving(TileMap tm) {
		super(tm);
		effects = new ArrayList<Effect>();
		explosions = new ArrayList<Explosion>();
	}

	public double getHealth() { 
		return health; 
	}
	
	public double getMaxHealth() {
		return maxHealth; 
	}
	
	public double getShield() {
		return shield;
	}
	
	public double getMaxShield() {
		return maxShield;
	}
	
	public void setHealth(double health) {
		this.health = health;
	}
	
	public void heal(double health) {
		this.setHealth(this.getHealth() + health);
		if(this.getHealth() > this.getMaxHealth()) this.setHealth(this.getMaxHealth());
	}

	/**
	 * Deals damage to the entity
	 * @param damage
	 * @param source (should always be <code>this</code>)
	 * @return true if attack succeeded
	 */
	public boolean hit(double damage, boolean ignoreShield, boolean ignoreFlinching, String type, MapObject source) {
		if(!ignoreFlinching) {
			if(flinching) return false;
			flinching = true;
			flinchTimer = System.nanoTime();
		}
		if(ignoreShield) {
			health -= damage;
			if(health < 0) health = 0;
		} else {
			double s = shield;
			shield -= damage;
			if(shield < 0) shield = 0;
			damage -= (s - shield);
			health -= damage;
		}
		explosions.add(new Explosion(this.getx(), this.gety()));
		if(health < 0) health = 0;
		if(health == 0) this.setDead(source);
		LogHelper.logInfo("[COMBAT] " + this.getClass().getSimpleName() + " was hit by " + source.getClass().getSimpleName() + " with " + type + " for " + damage + " damage");
		return true;
	}
	
	/**
	 * Creates a new effect and adds it to the entity
	 * @param type
	 * @param strength
	 * @param duration
	 */
	public void addEffect(MapObject source, int type, double strength, double duration) {
		Effect e = new Effect(this, source, type, strength, duration);
		for(int i = 0; i < effects.size(); i++) {
			if(effects.get(i).getType() == type) {
				if(effects.get(i).getStrength() > strength) {
					effects.get(i).addDuration(duration / 2);
					return;
				} else {
					effects.get(i).removeFromEntity();
					effects.remove(i);
					effects.add(e);
					return;
				}
			}
		}
		effects.add(e);
	}
	
	public abstract void getNextPosition();
	
	public void tick() {
		getNextPosition();
		// update entity animation
		animation.tick();
		
		// update effects
		for(int i = 0; i < effects.size(); i++) {
			effects.get(i).tick();
			if(effects.get(i).shouldRemove()) effects.remove(i);
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).tick();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		if(stunned) {
			stunTimer--;
			dx = 0;
			if(stunTimer == 0) {
				knockedUp = false;
				dy = 0.1 * backup;
			}
		}
		
		if(knockedUp) {
			dy = -0.1;
			if(stunTimer == 0) {
				knockedUp = false;
				dy = 0.1 * backup;
			}
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		// draw entity
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				
			} else {
				if(!dead) super.render(g);
			}
		} else {
			if(!dead) super.render(g);
		}
		
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			explosions.get(i).render(g);
		}
		
		healthBar.render(g);
	}
	
	public void setDead(MapObject source) {
		this.dead = true;
		flinching = false;
	}

	public void knockUp(double duration) {
		stunned = true;
		knockedUp = true;
		stunTimer = duration;
	}
	
	public void stun(double duration) {
		stunned = true;
		stunTimer = duration;
	}
	
	public ArrayList<Effect> getEffects() {
		return effects;
	}
}