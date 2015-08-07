package net.masterzach32.sidescroller.entity.living.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.EntityPlayer;
import net.masterzach32.sidescroller.entity.living.HealthBar;
import net.masterzach32.sidescroller.entity.living.effects.Effect;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;
public class Boss extends Enemy {
	
	private BufferedImage[] sprites;
	
	private int attackRange;
	private int attack;
	
	private boolean hit = false;
	
	private Random r = new Random();

	public Boss(TileMap tm, int level) {
		super(tm, level);
		moveSpeed = 0.2;
		setMaxSpeed(0.3);
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		sight = 250;
		hsight = 160;
		
		health = maxHealth = (250) + (75*level);
		damage = (6) + (3*level);
		attackRange = 50;
		
		exp = 0;
		
		armor = -40;
		damageMultiplier = (double) (100) / (100 + armor);
		
		healthBar = new HealthBar(this, 60, 4, new Color(255, 0, 0));
		
		// load sprites
			
		try {
			BufferedImage spritesheet = Assets.getImageAsset("boss");
			
			sprites = new BufferedImage[1];
				
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}
	
	/**
	 * Checks to see if the attack succeeded
	 * @param enemies
	 */
	public void checkAttack() {
		if(hit) return;
		EntityPlayer p = LevelState.getPlayer();
		// scratch attack
		if(facingRight) {
			if(p.intersects(new Rectangle((int) (x), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
				hit = p.hit(damage, false, false, "Wither Bite", this);
			}
		} else {
			if(p.intersects(new Rectangle((int) (x - attackRange), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
				hit = p.hit(damage, false, false, "Wither Bite", this);
			}
		}
		if(hit) {
			p.addEffect(this, Effect.WITHER, 1 + 3 * level, 3);
		}
	}
	
	public void getNextPosition() {
		if(!LevelState.getPlayer().intersects(new Rectangle((int) (x - sight / 2), (int) (y - hsight / 2), sight, hsight))) return;
		attack++;
		// find player direction (0-60)
		if(attack <= 60){
			if(LevelState.getPlayer().getx() < this.getx()) {
				right = false;
				left = true;
				facingRight = false;
			} else if (LevelState.getPlayer().getx() > this.getx()) {
				right = true;
				left = false;
				facingRight = true;
			}
			// movement
			if(left) {
				dx -= moveSpeed;
				if(dx < -getMaxSpeed()) {
					dx = -getMaxSpeed();
				}
			}
			else if(right) {
				dx += moveSpeed;
				if(dx > getMaxSpeed()) {
					dx = getMaxSpeed();
				}
			}
			
			// falling
			if(falling) {
				dy += fallSpeed;
			}
		}
		// wind up attack (61 - 150)
		else if(attack <= 150) {
			dx = 0;
			dy = 0;
		}
		// attack (151 - 160)
		else if(attack <= 160) {
			checkAttack();
		}
		// cooldown (161 - 270)
		else if(attack <= 270) {
			
		} else {
			hit = false;
			attack = 0;
		}
	}
	
	public void tick() {
		if(dead) return;
		super.tick();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		healthBar.render(g);
		
		if(MapObject.isHitboxEnabled()) {
			g.setColor(Color.WHITE);
			g.draw(new Rectangle((int) (x + xmap - sight / 2), (int) (y + ymap - hsight / 2), sight, hsight));
		}
		
		// find player direction (0-60)
		if(attack <= 60) {
			
		}
		// wind up attack (61 - 150)
		else if(attack <= 150) {
			//j = 0;
			int i0 = r.nextInt(height);
			g.setColor(Color.RED);
			if(facingRight) {
				g.drawLine((int)(x + xmap - width / 2), (int)(y + ymap - height / 2) + i0, (int)(x + xmap - width / 2) + width + attackRange, (int)(y + ymap - height / 2) + i0);
			} else {
				g.drawLine((int)(x + xmap - width / 2) + width, (int)(y + ymap - height / 2) + i0, (int)(x + xmap - width / 2) - attackRange, (int)(y + ymap - height / 2) + i0);
			}
		}
		// attack (151 - 160)
		else if(attack <= 160) {
			g.setColor(Color.RED);
			if(facingRight) {
				g.drawRect((int)(x + xmap), (int)(y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
			} else {
				g.drawRect((int)(x + xmap - attackRange), (int)(y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
			}
		}
		// cooldown (161 - 270)
		else if(attack <= 270) {
			
		} else {
					
		}
		
		super.render(g);
	}
}