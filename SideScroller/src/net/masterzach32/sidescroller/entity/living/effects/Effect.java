package net.masterzach32.sidescroller.entity.living.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.EntityLiving;
import net.masterzach32.sidescroller.main.SideScroller;

@SuppressWarnings("unused")
public class Effect {
	
	private int type, delay, delayTimer;
	private double strength, timer;
	private EntityLiving entity;
	private MapObject source;
	
	private double speed;
	
	private boolean remove;
	
	public static final int HEAL         = 0;
	public static final int ATTACK       = 1;
	public static final int SPEED        = 2;
	public static final int HEALTHREGEN  = 3;
	public static final int POISION      = 4;
	public static final int WITHER       = 5;
	public static final int FIRE         = 6;
	public static final int KNOCKUP      = 7;
	public static final int SLOW         = 8;
	public static final int STUN         = 9;
	private static final int[] numFrames = {3, 3, 3, 3, 3, 3, 3, 3};
	
	private static ArrayList<BufferedImage[]> sprites;
	private Animation animation;
	
	public static void loadSprites() {
		// load sprites
		try {
			//BufferedImage spritesheet = Assets.getImageAsset("effects");
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < numFrames.length; i++) {
				//BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					//bi[j] = spritesheet.getSubimage(j * 30, i * 30, 30, 30);
				}
				//sprites.add(bi);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new effect on the entity
	 * @param entity
	 * @param type
	 * @param strength
	 * @param duration in seconds
	 */
	public Effect(EntityLiving target, MapObject source, int type, double strength, double duration) {
		this.type = type;
		this.strength = strength;
		this.timer = duration * SideScroller.FPS;
		this.entity = target;
		this.source = source;
		delay = 30;
		delayTimer = 0;
		this.addToEntity();
		
		animation = new Animation();/*
		if(this.type == ATTACK) animation.setFrames(sprites.get(ATTACK));
		if(this.type == SPEED) animation.setFrames(sprites.get(SPEED));
		if(this.type == HEALTHREGEN) animation.setFrames(sprites.get(HEALTHREGEN));
		if(this.type == POISION) animation.setFrames(sprites.get(POISION));
		if(this.type == WITHER) animation.setFrames(sprites.get(WITHER));
		if(this.type == FIRE) animation.setFrames(sprites.get(FIRE));
		if(this.type == KNOCKUP) animation.setFrames(sprites.get(KNOCKUP));
		if(this.type == SLOW) animation.setFrames(sprites.get(SLOW));
		if(this.type == STUN) animation.setFrames(sprites.get(STUN));*/
		animation.setDelay(40);
	}
	
	public void addToEntity() {
		if(this.type == ATTACK) entity.damage += 2 * strength;
		if(this.type == SPEED) {
			speed = entity.getMaxSpeed() * (0.15 * strength);
			entity.setMaxSpeed(entity.getMaxSpeed() + speed);
		}
		if(this.type == HEALTHREGEN) return;
		if(this.type == POISION) return;
		if(this.type == WITHER) {
			if(0.05 * strength >= 0.9) speed = entity.getMaxSpeed() * (0.9);
			else speed = entity.getMaxSpeed() * (0.06 * strength);
			entity.setMaxSpeed(entity.getMaxSpeed() - speed);
		}
		if(this.type == FIRE) return;
		if(this.type == KNOCKUP) entity.knockUp(timer);
		if(this.type == SLOW) {
			if(0.07 * strength >= 0.9) speed = entity.getMaxSpeed() * (0.9);
			else speed = entity.getMaxSpeed() * (0.08 * strength);
			entity.setMaxSpeed(entity.getMaxSpeed() - speed);
		}
		if(this.type == STUN) entity.stun(timer);
	}
	
	public void removeFromEntity() {
		if(this.type == ATTACK) entity.damage -= 2 * strength;
		if(this.type == SPEED) entity.setMaxSpeed(entity.getMaxSpeed() - speed);
		if(this.type == HEALTHREGEN) return;
		if(this.type == POISION) return;
		if(this.type == WITHER) entity.setMaxSpeed(entity.getMaxSpeed() + speed);
		if(this.type == FIRE) return;
		if(this.type == KNOCKUP) return;
		if(this.type == SLOW) entity.setMaxSpeed(entity.getMaxSpeed() + speed);
		if(this.type == STUN) return;
	}
	
	/**
	 * Sets the delay between hits on dots, Default is 30 ticks (.5 seconds)<br>
	 * NOTE: This does not affect damage, so lower numbers WILL CAUSE THE EFFECT TO DEAL EXPONENTIAL DAMAGE
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void tick() {
		if(timer > 0) timer--;
		if(timer == 0) {
			removeFromEntity();
			remove = true;
		}
		if(delayTimer < delay) {
			delayTimer++;
			return;
		} else if(delayTimer == delay) {
			if(this.type == ATTACK) return;
			if(this.type == SPEED) return;
			if(this.type == HEALTHREGEN) entity.heal((float) (0.3 * strength));
			if(this.type == POISION) entity.hit((float) ((0.2 * strength) + (0.07 * (entity.getMaxHealth() - entity.getHealth()))), false, true, "Poision", source);
			if(this.type == WITHER) entity.hit((float) (0.35 * strength), false, true, "Wither", source);
			if(this.type == FIRE) entity.hit((float) (0.1 + (0.04 + 0.02 * strength) * entity.getHealth()), false, true, "Fire", source);
			if(this.type == KNOCKUP) return;
			if(this.type == SLOW) return;
			if(this.type == STUN) return;
			delayTimer = 0;
		}
		//animation.tick();
	}
	
	public void render(Graphics2D g, int x, int y, int space) {
		// render status HUD
		g.setColor(Color.WHITE);
		if(this.type == ATTACK) g.setColor(Color.RED);
		if(this.type == SPEED) g.setColor(Color.GREEN);
		if(this.type == HEALTHREGEN) g.setColor(Color.PINK);
		if(this.type == POISION) g.setColor(new Color(0, 200, 0));
		if(this.type == WITHER) g.setColor(new Color(51, 0, 51));
		if(this.type == FIRE) g.setColor(Color.ORANGE);
		if(this.type == KNOCKUP) g.setColor(Color.WHITE);
		if(this.type == SLOW) g.setColor(Color.BLACK);
		if(this.type == STUN) g.setColor(new Color(204, 0, 204));
		g.fillRect(x + (5 * space), y - 5, 4, 4);
		g.setColor(Color.WHITE);
		//animation.render(g, x, y, 30, 30);
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public int getType() {
		return type;
	}
	
	public double getStrength() {
		return strength;
	}
	
	public void addDuration(double duration) {
		timer += duration * SideScroller.FPS;
	}
}