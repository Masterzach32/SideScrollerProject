package net.masterzach32.sidescroller.entity.living;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

@SuppressWarnings("unused")
public class HealthBar {
	
	private EntityLiving entity;
	private double health, maxHealth, hlength, length, shield = 0, maxShield = 0, slength;
	private double dhlength;
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
		this.entity = entity;
		healthBar = color;
		this.width = width;
		this.height = height;
		dhlength = width;
		owidth = this.entity.getWidth();
		oheight = this.entity.getHeight();
	}
	
	private void tick() {
		// health bar
		health = entity.health;
		maxHealth = entity.maxHealth;
		shield = entity.shield;
		maxShield = entity.maxShield;
		
		double maxTotal;
		if(health + shield < maxHealth) maxTotal = maxHealth;
		else maxTotal = maxHealth + (health + shield - maxHealth);
		double healthPercent = health / maxTotal;
		double shieldPercent = shield / maxTotal;
			
		hlength = healthPercent * width;
		slength = shieldPercent * width;
		length = hlength + slength;
		
		//if(shield > 0) slength += 1;
			
		// damage bar
		if(length >= dhlength) dhlength = (double) (length - 1);
		if(length < dhlength) dhlength -= .7;
	}
	
	public void render(Graphics2D g) {
		tick();
		
		Point p = entity.getScreenLocation();
		int x = p.x;
		int y = p.y;
		
		// FIXME: Fix render bugs - double to int rounding differences.
		
		// health bar
		g.setColor(border);
		g.drawRect((int) (x - width / 2), (int) (y - oheight / 2), (int) width, height);
		g.setColor(damageBar);
		g.fillRect((int) (x - width / 2) + 1, (int) (y - oheight / 2) + 1, (int) dhlength - 1, height - 1);
		g.setColor(healthBar);
		g.fillRect((int) (x - width / 2) + 1, (int) (y - oheight / 2) + 1, (int) hlength - 1, height - 1);
		g.setColor(shieldBar);
		g.fillRect((int) (x - width / 2 + hlength), (int) (y - oheight / 2) + 1, (int) slength, height - 1);
		
		// render effects HUD
		for(int i = 0; i < entity.effects.size(); i++) {
			entity.effects.get(i).render(g, (int) (x - width / 2), (int) (y - oheight / 2), i);
		}
	}
}
