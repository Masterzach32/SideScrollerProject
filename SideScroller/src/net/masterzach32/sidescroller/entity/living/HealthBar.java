package net.masterzach32.sidescroller.entity.living;

import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar {
	
	private EntityLiving e;
	private float health, maxHealth, percent, length;
	private float dlength;
	private int width, height;
	
	private Color healthBar, damageBar = new Color(200, 0, 0), border = Color.GRAY;

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
	}
	
	private void tick() {
		// health bar
		health = e.health;
		maxHealth = e.maxHealth;
		percent = health / maxHealth;
		length = percent * width;
		
		// damage bar
		if(length >= dlength) dlength = length;
		if(length < dlength) dlength -= .7;
	}
	
	public void render(Graphics2D g) {
		tick();
		
		// health bar
		g.setColor(border);
		g.drawRect((int)(e.getx() + e.getxmap() - e.getWidth() / 2), (int)(e.gety() + e.getymap() - e.getHeight() / 2), (int) width, height);
		g.setColor(damageBar);
		g.fillRect((int)(e.getx() + e.getxmap() - e.getWidth() / 2) + 1, (int)(e.gety() + e.getymap() - e.getHeight() / 2) + 1, (int) dlength - 1, height - 1);
		g.setColor(healthBar);
		g.fillRect((int)(e.getx() + e.getxmap() - e.getWidth() / 2) + 1, (int)(e.gety() + e.getymap() - e.getHeight() / 2) + 1, (int) length - 1, height - 1);
	}
}
