package net.masterzach32.sidescroller.entity.living;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class HealthBar {
	
	private EntityLiving e;
	private float health, maxHealth, hlength, length, shield = 0, maxShield = 0, slength;
	private float dlength;
	private int width, height, owidth, oheight;
	
	private Color healthBar, damageBar = new Color(200, 0, 0), border = Color.GRAY, shieldBar = Color.BLUE;

	/**
	 * Creates a new health bar for the Entity passed.
	 * @param entity
	 * @param width
	 * @param height
	 * @param color
	 */
	public HealthBar(EntityLiving entity, int width, int height, Color color) {
		e = entity;
		healthBar = color;
		this.width = width;
		this.height = height;
		dlength = width;
		owidth = e.getWidth();
		oheight = e.getHeight();
	}
	
	private void tick() {
		// health bar
		health = e.health;
		maxHealth = e.maxHealth;
		shield = e.shield;
		maxShield = e.maxShield;
		
		float maxTotal = maxHealth + shield;
		float healthPercent = health / maxTotal;
		float shieldPercent = shield / maxTotal;
			
		hlength = healthPercent * width;
		slength = shieldPercent * width;
		length = hlength + slength;
			
		// damage bar
		if(length >= dlength) dlength = length;
		if(length < dlength) dlength -= .7;
	}
	
	public void render(Graphics2D g) {
		tick();
		
		Point p = e.getScreenLocation();
		int x = p.x;
		int y = p.y;
		
		// FIXME: Fix render bugs - float to int rounding differences.
		
		// health bar
		g.setColor(border);
		g.drawRect((int) (x - owidth / 2), (int) (y - oheight / 2), (int) width, height);
		g.setColor(damageBar);
		g.fillRect((int) (x - owidth / 2) + 1, (int) (y - oheight / 2) + 1, (int) dlength - 1, height - 1);
		g.setColor(healthBar);
		g.fillRect((int) (x - owidth / 2) + 1, (int) (y - oheight / 2) + 1, (int) hlength - 1, height - 1);
		g.setColor(shieldBar);
		g.fillRect((int) (x - owidth / 2 + hlength) + 1, (int) (y - oheight / 2) + 1, (int) slength - 1, height - 1);
	}
}
