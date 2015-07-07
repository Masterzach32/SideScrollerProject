package net.masterzach32.sidescroller.entity.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.EntityPlayer;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Swordman extends Enemy {
	
	private double b0 = 30;
	
	private boolean attacking;
	
	private int attackRange;
	private int attackCd = 0;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	private static final int IDLE = 0, WALKING = 1, ORB = 5, SCRATCHING = 6;

	public Swordman(TileMap tm, int level) {
		super(tm);
		moveSpeed = 0.3;
		maxSpeed = 1.1;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		sight = 250;
		hsight = 36;
		
		health = maxHealth = (10) + (8*level);
		damage = (2) + (4*level);
		
		exp = (20);
		
		armor = 40 + (5*level);
		damageMultiplier = (double) (100) / (100 + armor);
		
		attacking = false;
		
		attackRange = 30;
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("player");
					
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					if(i != SCRATCHING) {
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					} else {
						bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}
				}
				sprites.add(bi);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		explosions = new ArrayList<Explosion>();
				
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);

		facingRight = true;
		right = true;
	}
	
	/**
	 * Checks to see if the attack succeeded
	 * @param enemies
	 */
	public void checkAttack() {
		EntityPlayer p = LevelState.getPlayer();
		// scratch attack
		if(facingRight) {
			if(p.intersects(new Rectangle((int) (x), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
				p.hit(damage, "Scratch", this);
			}
		} else {
			if(p.intersects(new Rectangle((int) (x - attackRange), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
				p.hit(damage, "Scratch", this);
			}
		}
	}

	private void getNextPosition() {
		// movement
		if(LevelState.getPlayer().intersects(new Rectangle((int) (x - attackRange / 2), (int) (y - hsight / 2), attackRange, hsight))) {
			// player within attacking range
			dx = 0;
			if(attackCd == 0) {
				attacking = true;
				attackCd = 120;
				left = false;
				right = false;
			}
		} else if(LevelState.getPlayer().intersects(new Rectangle((int) (x - sight / 2), (int) (y - hsight / 2), sight, hsight))) {
			// player within sight
			if(LevelState.getPlayer().getx() < this.getx()) {
				right = false;
				left = true;
				facingRight = false;
			} else if (LevelState.getPlayer().getx() > this.getx()) {
				right = true;
				left = false;
				facingRight = true;
			} 	
			if(left) {
				dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
			} else if(right) {
				dx += moveSpeed;
				if(dx > maxSpeed) {
					dx = maxSpeed;
				}
			}
		} else {
			// player cannot be seen
			// if it hits a wall, go other direction
			if(right && dx == 0) {
				right = false;
				left = true;
				facingRight = false;
			} else if(left && dx == 0) {
				right = true;
				left = false;
				facingRight = true;
			}
			
			if(left) {
				dx = -moveSpeed;
			} else if(right) {
				dx = moveSpeed;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
		
		// cannot move while attacking, except in air
		if((currentAction == SCRATCHING || currentAction == ORB) && !(jumping || falling)) {
			dx = 0;
		}
		
		if(attackCd > 0) attackCd--;
	}
	
	public void tick() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) attacking = false;
		}
		
		// set animation
		if(attacking) {
			checkAttack();
			if(currentAction != SCRATCHING) {
				//sfx.get("scratch").play();
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		} else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		} else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		// set direction
		if(currentAction != SCRATCHING && currentAction != ORB) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).tick();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		// update animation
		animation.tick();
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		
		super.render(g);
		
		if(MapObject.isHitboxEnabled()) {
			g.setColor(Color.WHITE);
			g.draw(new Rectangle((int) (x + xmap - sight / 2), (int) (y + ymap - hsight / 2), sight, hsight));
			g.draw(new Rectangle((int) (x + xmap - attackRange*2 / 2), (int) (y + ymap - hsight / 2), attackRange*2, hsight));
			if(attacking) {
				g.setColor(Color.YELLOW);
				if(facingRight) {
					g.drawRect((int)(x + xmap), (int)(y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
				} else {
					g.drawRect((int)(x + xmap - attackRange), (int)(y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
				}
			}
		}
		g.setColor(Color.WHITE);
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			explosions.get(i).render(g);
		}
		
		double h0 = health / maxHealth;
		double h1 = h0 * 30;
		
		if(h1 >= b0) b0 = h1;
		if(h1 < b0) b0 -= .7;
		
		// health bar
		g.setColor(new Color(200, 0, 0));
		g.fillRect((int)(x + xmap - 30 / 2), (int)(y + ymap - height / 2), (int) b0, 2);
		g.setColor(new Color(255, 0, 0));
		g.fillRect((int)(x + xmap - 30 / 2), (int)(y + ymap - height / 2), (int) h1, 2);
	}
}