package net.masterzach32.sidescroller.entity.enemy;

import java.awt.Graphics2D;

import net.masterzach32.sidescroller.tilemap.TileMap;

public class Boss extends Enemy {

	public Boss(TileMap tm, int level) {
		super(tm);
		moveSpeed = 0.1;
		maxSpeed = 0.1;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 60;
		height = 60;
		cwidth = 60;
		cheight = 60;
		
		health = maxHealth = (275) + (25*level);
		damage = (10) + (5*level);
		
		exp = (25) + (30*level);
		
		armor = 20 + (5*level);
		damageMultiplier = (double) (100) / (100 + armor);
	}
	
	private void getNextPosition() {
		
	}
	
	public void tick() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		super.render(g);
	}
}
