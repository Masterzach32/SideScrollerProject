package net.masterzach32.sidescroller.entity.living.effects;

import java.awt.Color;
import java.awt.Graphics2D;

import net.masterzach32.sidescroller.entity.living.EntityLiving;

public class Effect {
	
	private int type, strength, timer;
	private EntityLiving entity;
	
	private boolean remove;
	
	public static final int HEAL          = 0;
	public static final int ATTACK        = 1;
	public static final int SPEED         = 2;
	public static final int HEALTHREGEN   = 3;
	
	/**
	 * Creates a new effect on the entity
	 * @param entity
	 * @param type
	 * @param strength
	 * @param duration in ticks
	 */
	public Effect(EntityLiving entity, int type, int strength, int duration) {
		this.type = type;
		this.strength = strength;
		this.timer = duration;
		this.entity = entity;
		this.addToEntity();
	}
	
	private void addToEntity() {
		if(this.type == ATTACK) entity.damage += strength;
		if(this.type == SPEED) entity.setMaxSpeed(entity.getMaxSpeed() + 0.5 * strength);
		if(this.type == HEALTHREGEN) entity.healthRegen += 0.05 * strength;
	}
	
	private void removeFromEntity() {
		if(this.type == ATTACK) entity.damage -= strength;
		if(this.type == SPEED) entity.setMaxSpeed(entity.getMaxSpeed() - 0.5 * strength);
		if(this.type == HEALTHREGEN) entity.healthRegen -= 0.05 * strength;
	}
	
	public void tick() {
		if(timer > 0) timer--;
		if(timer == 0) {
			removeFromEntity();
			remove = true;
		}
	}
	
	public void render(Graphics2D g, int x, int y, int space) {
		if(this.type == ATTACK) g.setColor(Color.ORANGE);
		if(this.type == SPEED) g.setColor(Color.GREEN);
		if(this.type == HEALTHREGEN) g.setColor(Color.PINK);;
		g.fillRect(x + (5 * space), y - 5, 4, 4);
	}
	
	public boolean shouldRemove() {
		return remove;
	}
}