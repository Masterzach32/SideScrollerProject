package net.masterzach32.sidescroller.entity.living.effects;

import java.awt.Color;
import java.awt.Graphics2D;

import net.masterzach32.sidescroller.entity.living.EntityLiving;
import net.masterzach32.sidescroller.main.SideScroller;

public class Effect {
	
	private int type, strength, timer;
	private EntityLiving entity, source;
	
	private boolean remove;
	
	public static final int HEAL          = 0;
	public static final int ATTACK        = 1;
	public static final int SPEED         = 2;
	public static final int HEALTHREGEN   = 3;
	public static final int POISION       = 4;
	public static final int WITHER        = 5;
	
	/**
	 * Creates a new effect on the entity
	 * @param entity
	 * @param type
	 * @param strength
	 * @param duration in seconds
	 */
	public Effect(EntityLiving target, EntityLiving source, int type, int strength, int duration) {
		this.type = type;
		this.strength = strength;
		this.timer = duration * SideScroller.FPS;
		this.entity = target;
		this.source = source;
		this.addToEntity();
	}
	
	private void addToEntity() {
		if(this.type == ATTACK) entity.damage += 2 * strength;
		if(this.type == SPEED) entity.setMaxSpeed(entity.getMaxSpeed() + 0.5 * strength);
		if(this.type == HEALTHREGEN) return;
		if(this.type == POISION) return;
		if(this.type == WITHER) entity.setMaxSpeed(entity.getMaxSpeed() - 0.3 * strength);
	}
	
	private void removeFromEntity() {
		if(this.type == ATTACK) entity.damage -= 2 * strength;
		if(this.type == SPEED) entity.setMaxSpeed(entity.getMaxSpeed() - 0.5 * strength);
		if(this.type == HEALTHREGEN) return;
		if(this.type == POISION) return;
		if(this.type == WITHER) entity.setMaxSpeed(entity.getMaxSpeed() + 0.3 * strength);;
	}
	
	public void tick() {
		if(timer > 0) timer--;
		if(timer == 0) {
			removeFromEntity();
			remove = true;
		}
		
		if(this.type == ATTACK) return;
		if(this.type == SPEED) return;
		if(this.type == HEALTHREGEN) entity.heal((float) (0.0005 * strength));
		if(this.type == POISION) entity.hit((int) (1.5 * strength), "Poision", source);
		if(this.type == WITHER) entity.hit((int) (strength), "Wither", source);
	}
	
	public void render(Graphics2D g, int x, int y, int space) {
		g.setColor(Color.WHITE);
		if(this.type == ATTACK) g.setColor(Color.ORANGE);
		if(this.type == SPEED) g.setColor(Color.GREEN);
		if(this.type == HEALTHREGEN) g.setColor(Color.PINK);
		if(this.type == POISION) g.setColor(new Color(0, 200, 0));
		if(this.type == WITHER) g.setColor(Color.BLACK);
		g.fillRect(x + (5 * space), y - 5, 4, 4);
	}
	
	public boolean shouldRemove() {
		return remove;
	}
}