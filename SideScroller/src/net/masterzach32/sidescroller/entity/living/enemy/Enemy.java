package net.masterzach32.sidescroller.entity.living.enemy;

import java.util.ArrayList;

import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.EntityLiving;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Enemy extends EntityLiving {
	
	protected boolean dead;
	protected int damage;
	protected int armor; 
	protected double damageMultiplier;
	protected int exp;
	
	protected int sight, hsight;
	
	protected boolean flinching;
	protected long flinchTimer;
	
	protected ArrayList<Explosion> explosions;
	
	public Enemy(TileMap tm) {
		super(tm);
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
	
	public void hit(int damage, String type, MapObject source) {
		if(dead || flinching) return;
		explosions.add(new Explosion(this.getx(), this.gety()));
		damage = (int) (damage * damageMultiplier);
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
		//LogHelper.logInfo("[COMBAT] " + this.getClass().getSimpleName() + " hit for " + damage + " damage from " + type + " by " + source.getClass().getSimpleName());
	}
	
	public void tick() {}
	
	protected ArrayList<Explosion> getExplosions() {
		return explosions;
	}
	
	public int getXpGain() {
		return exp;
	}
}