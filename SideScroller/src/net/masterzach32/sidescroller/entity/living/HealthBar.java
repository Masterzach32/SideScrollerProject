package net.masterzach32.sidescroller.entity.living;

import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar {
	
	private EntityLiving e;
	private float health, maxHealth, hlength, percent, length, shield = 0, slength;
	private float dlength;
	private int width, height, owidth, oheight;
	
	private Color healthBar, damageBar = new Color(200, 0, 0), border = Color.GRAY, shieldBar = Color.BLUE;

	/**
	 * Creates a new health bar for the Entity passed.
	 * @param e
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
		
		if(shield == 0) {
			percent = health / maxHealth;
			hlength = percent * width;
		}
		
		if(shield > 0) {
			float total = health + shield;
			float healthPercent = health / total;
			float shieldPercent = shield / total;
			
			hlength = healthPercent * width;
			slength = shieldPercent * width;
		}
		
		// damage bar
		if(length >= dlength) dlength = length;
		if(length < dlength) dlength -= .7;
	}
	
	public void render(Graphics2D g) {
		tick();
		
		// health bar
		g.setColor(border);
		g.drawRect((int)(e.getx() + e.getxmap() - owidth / 2), (int)(e.gety() + e.getymap() - oheight / 2), (int) width, height);
		g.setColor(damageBar);
		g.fillRect((int)(e.getx() + e.getxmap() - owidth / 2) + 1, (int)(e.gety() + e.getymap() - oheight / 2) + 1, (int) dlength - 1, height - 1);
		g.setColor(healthBar);
		g.fillRect((int)(e.getx() + e.getxmap() - owidth / 2) + 1, (int)(e.gety() + e.getymap() - oheight / 2) + 1, (int) hlength - 1, height - 1);
		g.setColor(shieldBar);
		g.fillRect((int)((e.getx() + e.getxmap() - owidth / 2) + hlength), (int)(e.gety() + e.getymap() - oheight / 2) + 1, (int) slength, height - 1);
	}
	
	public void setShield(float shieldValue) {
		shield = shieldValue;
	}
}
