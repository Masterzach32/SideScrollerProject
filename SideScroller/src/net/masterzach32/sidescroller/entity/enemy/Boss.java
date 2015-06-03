package net.masterzach32.sidescroller.entity.enemy;

import net.masterzach32.sidescroller.tilemap.TileMap;

public class Boss extends Enemy {

	public Boss(TileMap tm, int level) {
		super(tm);
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = (10) + (6*level);
		damage = (3) + (3*level);
		
		exp = (5) + (15*level);
		
		armor = 70 + (5*level);
		damageMultiplier = (double) (100) / (100 + armor);
	}

}
