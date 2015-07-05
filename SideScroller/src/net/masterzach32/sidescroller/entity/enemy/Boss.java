package net.masterzach32.sidescroller.entity.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.EntityPlayer;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;
public class Boss extends Enemy {
	
	private BufferedImage[] sprites;
	
	private double b0 = 60;
	
	private int attackRange;
	private int attack;
	
	private Random r = new Random();

	public Boss(TileMap tm, int level) {
		super(tm);
		moveSpeed = 0.1;
		maxSpeed = 0.2;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = (250) + (75*level);
		damage = (10) + (5*level);
		attackRange = 50;
		
		exp = (25) + (30*level);
		
		armor = -40;
		damageMultiplier = (double) (100) / (100 + armor);
		
		// load sprites
			
		try {
			BufferedImage spritesheet = Assets.getImageAsset("slugger");
			
			sprites = new BufferedImage[3];
				
			for(int i = 0; i < sprites.length; i++) {
			sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
		}
	
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		explosions = new ArrayList<Explosion>();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}
	
	public void hit(int damage, String type, MapObject source) {
		if(dead || flinching) return;
		explosions.add(new Explosion(this.getx(), this.gety()));
		damage = (int) (damage * damageMultiplier);
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	/**
	 * Checks to see if the attack succeeded
	 * @param enemies
	 */
	public void checkAttack() {
		EntityPlayer p = LevelState.getPlayer();
		// scratch attack
		if(facingRight) {
			if(p.getx() > x && p.getx() < x + width + attackRange && p.gety() > y - height / 2 && p.gety() < y + height / 2) {
				p.hit(damage, "Scratch", this);
			}
		} else {
			if(p.getx() < x && p.getx() > x - width - attackRange && p.gety() > y - height / 2 && p.gety() < y + height / 2) {
				p.hit(damage, "Scratch", this);
			}
		}
	}
	
	private void getNextPosition() {
		if((this.getx() - LevelState.getPlayer().getx()) >= 100) return;
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
				if(dx < -maxSpeed) {
					dx = -maxSpeed;
				}
			}
			else if(right) {
				dx += moveSpeed;
				if(dx > maxSpeed) {
					dx = maxSpeed;
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
		// attack (151 - 180)
		else if(attack <= 160) {
			checkAttack();
		}
		// cooldown (161 - 270)
		else if(attack <= 270) {
			
		} else {
			attack = 0;
		}
	}
	
	public void tick() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
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
		
		double h0 = health / maxHealth;
		double h1 = h0 * 60;
		
		if(h1 >= b0) b0 = h1;
		if(h1 < b0) b0 -= .7;
		
		// health bar
		g.setColor(new Color(200, 0, 0));
		g.fillRect((int)(x + xmap - width / 2) - 15, (int)(y + ymap - height / 2) - 4, (int) b0, 8);
		g.setColor(new Color(255, 0, 0));
		g.fillRect((int)(x + xmap - width / 2) - 15, (int)(y + ymap - height / 2) - 4, (int) h1, 8);
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				// draw explosions
				for(int i = 0; i < explosions.size(); i++) {
					explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
					explosions.get(i).render(g);
				}
				return;
			}
		}	
		
		// find player direction (0-60)
		if(attack <= 60) {
			/*j++;
			g.setColor(Color.WHITE);
			if(j < 30) {
				j = 1;
				if(facingRight) {
					g.drawLine((int)(x + xmap - width / 2), (int)(y + ymap - height / 2) + 5, (int)(x + xmap - width / 2) + width + 7, (int)(y + ymap - height / 2) + 5);
					g.drawLine((int)(x + xmap - width / 2), (int)(y + ymap - height / 2) + 15, (int)(x + xmap - width / 2) + width + 7, (int)(y + ymap - height / 2) + 15);
					g.drawLine((int)(x + xmap - width / 2), (int)(y + ymap - height / 2) + 25, (int)(x + xmap - width / 2) + width + 7, (int)(y + ymap - height / 2) + 25);
				} else {
					g.drawLine((int)(x + xmap - width / 2) + width, (int)(y + ymap - height / 2) + 5, (int)(x + xmap - width / 2) - 7, (int)(y + ymap - height / 2) + 5);
					g.drawLine((int)(x + xmap - width / 2) + width, (int)(y + ymap - height / 2) + 15, (int)(x + xmap - width / 2) - 7, (int)(y + ymap - height / 2) + 15);
					g.drawLine((int)(x + xmap - width / 2) + width, (int)(y + ymap - height / 2) + 25, (int)(x + xmap - width / 2) - 7, (int)(y + ymap - height / 2) + 25);
				}
			} else {
				j = 0;
				if(facingRight) {
					g.drawLine((int)(x + xmap - width / 2) + 5, (int)(y + ymap - height / 2) + 5, (int)(x + xmap - width / 2) + width + 7 + 5, (int)(y + ymap - height / 2) + 5);
					g.drawLine((int)(x + xmap - width / 2) + 5, (int)(y + ymap - height / 2) + 15, (int)(x + xmap - width / 2) + width + 7 + 5, (int)(y + ymap - height / 2) + 15);
					g.drawLine((int)(x + xmap - width / 2) + 5, (int)(y + ymap - height / 2) + 25, (int)(x + xmap - width / 2) + width + 7 + 5, (int)(y + ymap - height / 2) + 25);
				} else {
					g.drawLine((int)(x + xmap - width / 2) + width - 5, (int)(y + ymap - height / 2) + 5, (int)(x + xmap - width / 2) - 7 - 5, (int)(y + ymap - height / 2) + 5);
					g.drawLine((int)(x + xmap - width / 2) + width - 5, (int)(y + ymap - height / 2) + 15, (int)(x + xmap - width / 2) - 7 - 5, (int)(y + ymap - height / 2) + 15);
					g.drawLine((int)(x + xmap - width / 2) + width - 5, (int)(y + ymap - height / 2) + 25, (int)(x + xmap - width / 2) - 7 - 5, (int)(y + ymap - height / 2) + 25);
				}
			}*/
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
			if(facingRight) {
				g.drawRect((int)(x + xmap - width / 2), (int)(y + ymap - height / 2), width + attackRange,  height);
			} else {
				g.drawRect((int)(x + xmap - width / 2) - attackRange, (int)(y + ymap - height / 2), width + attackRange, height);
			}
		}
		// cooldown (161 - 270)
		else if(attack <= 270) {
			
		} else {
					
		}
		
		super.render(g);
		
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			explosions.get(i).render(g);
		}
	}
}
