package net.masterzach32.sidescroller.entity.living;

import java.util.ArrayList;

import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.effects.Effect;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class EntityLiving extends MapObject {
	
	public float health, maxHealth, shield, maxShield;
	public float healthRegen;
	public float shieldRegen;
	public int exp, damage;
	
	public ArrayList<Effect> effects = new ArrayList<Effect>();
	
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
	
	public void setHealth(float h) {
		health = h;
	}
	
	public void heal(float health) {
		this.setHealth(this.getHealth() + health);
		if(this.getHealth() > this.getMaxHealth()) this.setHealth(this.getMaxHealth());
	}

	public boolean hit(float damage, String type, MapObject source) {
		this.health -= damage;
		if(this.health < 0) this.health = 0;
		return true;
	}
	
	public void tick() {
		// update effects
		for(int i = 0; i < effects.size(); i++) {
			effects.get(i).tick();
			if(effects.get(i).shouldRemove()) effects.remove(i);
		}
	}
}