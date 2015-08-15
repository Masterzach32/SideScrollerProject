package net.masterzach32.sidescroller.entity.living.enemy;

import java.util.ArrayList;

import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.EntityLiving;
import net.masterzach32.sidescroller.tilemap.TileMap;
import net.masterzach32.sidescroller.util.Stats;

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
	
	public boolean hit(double damage, boolean ignoreShield, boolean ignoreFlinching, String type, MapObject source) {
		damage = (double) (damage * damageMultiplier);
		boolean hit = super.hit(damage, ignoreShield, ignoreFlinching, type, source);
		Stats.setStat("damageDelt", (int) damage);
		return hit;
	}
	
	public void getNextPosition() {}
	
}