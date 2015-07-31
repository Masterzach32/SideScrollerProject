package net.masterzach32.sidescroller.entity.living.enemy;

import java.util.ArrayList;

import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.EntityLiving;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Enemy extends EntityLiving {
	
	protected int armor; 
	protected double damageMultiplier;
	
	protected int sight, hsight;
	
	public Enemy(TileMap tm, int level) {
		super(tm);
		this.level = level;
	}
	
	public boolean isDead() {
		if(dead) {
			return true;
		} 
		return false;
	}
	
	public int getDamage() { 
		return damage; 
	}
	
	public void tick() {
		super.tick();
	}
	
	protected ArrayList<Explosion> getExplosions() {
		return explosions;
	}
	
	public int getXpGain() {
		return exp;
	}
	
	public boolean hit(float damage, boolean ignoreShield, String type, MapObject source) {
		if(flinching) return false;
		explosions.add(new Explosion(this.getx(), this.gety()));
		damage = (float) (damage * damageMultiplier);
		if(ignoreShield || shield == 0) {
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
}