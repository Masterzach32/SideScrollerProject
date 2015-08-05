package net.masterzach32.sidescroller.entity.living;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.enemy.Enemy;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.TileMap;
import net.masterzach32.sidescroller.util.Utilities;

public class Soldier extends MapObject {
	
	private EntityPlayer player;
	private boolean moving, attacking, remove;
	private int attackDelay, attackTimer, moveX, /*moveY,*/ time;
	
	private int attackRange;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	
	// animation actions
	//private static final int IDLE = 0, MOVING = 1, ATTACKING = 2, DECAY = 3;
	private static final int IDLE = 0, MOVING = 1, ATTACKING = 6;
	
	private ArrayList<Enemy> hits;

	protected Soldier(TileMap tm, int level, EntityPlayer player) {
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.75;
		setMaxSpeed(3.5);
		stopSpeed = 0.5;
		fallSpeed = 1;
		maxFallSpeed = 10.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		attackRange = 40;
		attackDelay = 90 - (15 * level);
		attackTimer = 0;
		
		time = 9 * SideScroller.FPS;
		this.player = player;
		
		moving = false;
		attacking = false;
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("player");
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					if(i != ATTACKING) {
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
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
	}
	
	protected boolean checkAttack(ArrayList<Enemy> enemies, int damage) {
		boolean hit = false;
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(isHit(e)) return false;
			if(facingRight) {
				if(e.intersects(new Rectangle((int) (x), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
					hit = e.hit(damage, false, true, "Soldier Attack", this);
					addToHitList(e);
				}
			} else {
				if(e.intersects(new Rectangle((int) (x - attackRange), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
					hit = e.hit(damage, false, true, "Soldier Attack", this);
					addToHitList(e);
				}
			}
		}
		return hit;
	}
	
	protected void attack() {
		if(attackTimer > 0) return;
		attackTimer = attackDelay;
		attacking = true;
		hits = null;
		hits = new ArrayList<Enemy>();
	}
	
	protected boolean isAttacking() {
		return attacking;
	}
	
	protected boolean isMoving() {
		return moving;
		
	}
	
	protected void move(int x, int y) {
		moving = true;
		moveX = x;
		//moveY = y;
		if(moveX < this.getx()) {
			facingRight = false;
			left = true;
			right = false;
		} else if (moveX > this.getx()) {
			facingRight = true;
			left = false;
			right = true;
		}
	}
	
	protected boolean shouldRemove() {
		return remove;
	}
	
	protected int getTimeLeft() {
		return time;
	}
	
	private void getNextPosition() {
		// movement
		Point p = Utilities.getMousePosition();
		int x = (int) (p.x / SideScroller.SCALE - xmap);
		
		if(this.x < x) {
			facingRight = true;
		} else if(this.x > x) {
			facingRight = false;
		}
		
		if(moving) {
			if(this.x < moveX) {
				facingRight = true;
			} else if(this.x > moveX) {
				facingRight = false;
			}
			if(facingRight) {
				dx += moveSpeed;
				if(dx > getMaxSpeed()) {
					dx = getMaxSpeed();
				}
				if((moveX - this.x) < 10) {
					moving = false;
				}
			} else {
				dx -= moveSpeed;
				if(dx < -getMaxSpeed()) {
					dx = -getMaxSpeed();
				}
				if((this.x - moveX) < 10) {
					moving = false;
				}
			}
			/*if(this.y < moveY) {
				facingRight = true;
			} else if(this.y > moveY) {
				facingRight = false;
			}
			if(facingRight) {
				dy += moveSpeed;
				if(dy > getMaxSpeed()) {
					dy = getMaxSpeed();
				}
				if((moveY - this.y) < 4) {
					moving = false;
				}
			} else {
				dy -= moveSpeed;
				if(dy < -getMaxSpeed()) {
					dy = -getMaxSpeed();
				}
				if((this.y - moveY) < 4) {
					moving = false;
				}
			}*/
		}
		
		if(!moving) {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			} else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// falling
		if(falling) {
			if(dy > 0) dy += fallSpeed;
			else dy += fallSpeed;
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void tick() {
		getNextPosition();
		if(moving) {
			setPosition(x + dx, y + dy);
		} else {
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
		}
		
		// check attack has stopped
		if(currentAction == ATTACKING) {
			if(animation.hasPlayedOnce()) attacking = false;
		}
		
		// set animation
		if(attacking) {
			if(currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(50);
				width = 60;
			}
		} else if(moving && (left || right)) {
			if(currentAction != MOVING) {
				currentAction = MOVING;
				animation.setFrames(sprites.get(MOVING));
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
		
		if(time > 0) time--;
		if(time <= 0) remove = true;
		
		if(attackTimer > 0) attackTimer--;
		
		animation.tick();
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
		Point p = player.getScreenLocation();
		int x = p.x;
		int y = p.y;
		
		g.setColor(new Color(218, 165, 32));
		g.drawLine(x, y, (int) (this.x + xmap), (int) (this.y + ymap));
		
		for(int i = time / 60 + 1; i > 0; i--) {
			g.fillRect((int) (this.x + xmap - 30 / 2 + 3 * i), (int) (this.y + ymap - height / 2) + 2, 2, 2);
		}
		
		if(MapObject.isHitboxEnabled()) {
			if(attacking) {
				g.setColor(Color.YELLOW);
				if(facingRight) {
					g.drawRect((int) (this.x + xmap), (int) (this.y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
				} else {
					g.drawRect((int) (this.x + xmap  - attackRange), (int) (this.y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
				}
			}
		}
		g.setColor(Color.WHITE);
	}

	protected static int getOldest(ArrayList<Soldier> s) {
		int oldest = 0;
		int min = Integer.MAX_VALUE;
		int[] soldiers = new int[s.size()];
		for(int j = 0; j < s.size(); j++) {
			soldiers[j] = s.get(j).getTimeLeft();
		}
		
		int i = 0;
		while (i < soldiers.length) {
			if(soldiers[i] < min) {
				min = soldiers[i];
				oldest = i;
			}
			i++;
		}
		
		return oldest;
	}
	
	public void addToHitList(Enemy entity) {
		hits.add(entity);
	}
	
	public boolean isHit(Enemy entity) {
		for(int i = 0; i < hits.size(); i++) {
			if(hits.get(i).equals(entity)) {
				return true;
			}
		}
		return false;
	}
}