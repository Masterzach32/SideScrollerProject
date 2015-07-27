package net.masterzach32.sidescroller.entity.living.effects;

import net.masterzach32.sidescroller.entity.living.EntityLiving;

public class Effect {
	
	protected int type, strength;
	
	public static final int ATTACK        = 0;
	public static final int SPEED         = 1;
	public static final int HEALTHREGEN   = 2;
	
	public Effect(int type, int strength) {
		this.type = type;
		this.strength = strength;
	}
	
	public void addToEntity(EntityLiving entity) {}
	
}