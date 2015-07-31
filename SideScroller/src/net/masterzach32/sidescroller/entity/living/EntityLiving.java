package net.masterzach32.sidescroller.entity.living;

import java.util.ArrayList;

import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.effects.Effect;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class EntityLiving extends MapObject {
	
	public float health, maxHealth, shield = 0, maxShield = 0;
	public float healthRegen;
	public float shieldRegen;
	public int exp, damage;
	
	public int level;
	public boolean dead;
	
	public boolean flinching;
	public long flinchTimer;
	
	public ArrayList<Effect> effects = new ArrayList<Effect>();
	public ArrayList<Explosion> explosions;
	
	public HealthBar healthBar;

	public EntityLiving(TileMap tm) {
		super(tm);
	}

	public float getHealth() { 
		return health; 
	}
	
	public float getMaxHealth() {
		return maxHealth; 
	}
	
	public float getShield() {
		return shield;
	}
	
	public float getMaxShield() {
		return maxShield;
	}
	
	public void setHealth(float health) {
		this.health = health;
	}
	
	public void heal(float health) {
		this.setHealth(this.getHealth() + health);
		if(this.getHealth() > this.getMaxHealth()) this.setHealth(this.getMaxHealth());
	}

	public boolean hit(float damage, boolean ignoreShield, String type, MapObject source) {
		if(flinching) return false;
		explosions.add(new Explosion(this.getx(), this.gety()));
		if(ignoreShield) {
			health -= damage;
			if(health < 0) health = 0;
		} else {
			float s = shield;
			shield -= damage;
			if(shield < 0) shield = 0;
			damage -= (s - shield);
			health -= damage;
		}
		if(health < 0) health = 0;
		if(health == 0) this.setDead(source);
		flinching = true;
		flinchTimer = System.nanoTime();
		return true;
		//LogHelper.logInfo("[COMBAT] " + this.getClass().getSimpleName() + " hit for " + damage + " damage from " + type + " by " + source.getClass().getSimpleName());
	}
	
	public void tick() {
		// update effects
		for(int i = 0; i < effects.size(); i++) {
			effects.get(i).tick();
			if(effects.get(i).shouldRemove()) effects.remove(i);
		}
	}
	
	public void setDead(MapObject source) {
		this.dead = true;
		flinching = false;
	}
}