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
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.TileMap;
import net.masterzach32.sidescroller.util.Utilities;

@SuppressWarnings("unused")
public class Soldier extends MapObject {
	
	private EntityPlayer player;
	private boolean moving, attacking, remove;
	private int attackDelay, timer, moveLocation, time;
	
	private int attackRange;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	
	// animation actions
	//private static final int IDLE = 0, MOVING = 1, ATTACKING = 2, DECAY = 3;
	private static final int IDLE = 0, MOVING = 1, JUMPING = 2, FALLING = 3, GLIDING = 4, SOLDIER = 5, ATTACKING = 6;

	protected Soldier(TileMap tm, int level, EntityPlayer player) {
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.5;
		setMaxSpeed(2.5);
		stopSpeed = 0.5;
		fallSpeed = 0.5;
		maxFallSpeed = 10.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		attackRange = 40;
		attackDelay = 95 - (5 * level);
		timer = 0;
		
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
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			boolean hit;
			if(facingRight) {
				if(e.intersects(new Rectangle((int) (x), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
					e.hit(damage, false, false, "Soldier Attack", this);
					return true;
				}
			} else {
				if(e.intersects(new Rectangle((int) (x - attackRange), (int) (y - height / 2 + (height - cheight) / 2), attackRange, cheight))) {
					e.hit(damage, false, false, "Soldier Attack", this);
					return true;
				}
			}
		}
		return false;
	}
	
	protected void attack() {
		if(timer > 0) return;
		timer = attackDelay;
		attacking = true;
	}
	
	protected boolean isAttacking() {
		return attacking;
	}
	
	protected boolean isMoving() {
		return moving;
		
	}
	
	protected void move(int x) {
		moving = true;
		moveLocation = x;
		if(x < this.getx()) {
			facingRight = false;
			left = true;
			right = false;
		} else if (x > this.getx()) {
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
		
		if(moving) {
			if(this.x < moveLocation) {
				if(moveLocation - this.x < 6) {
					moving = false;
					left = false;
					right = false;
				}
			} else if (x > moveLocation) {
				if(x - moveLocation < 6)
					moving = false;
					left = false;
					right = false;
			}
		}
		
		if(x < this.getx()) {
			facingRight = false;
		} else if (x > this.getx()) {
			facingRight = true;
		}
		if(left) {
			dx -= moveSpeed;
			if(dx < -getMaxSpeed()) {
				dx = -getMaxSpeed();
			}
		} else if(right) {
			dx += moveSpeed;
			if(dx > getMaxSpeed()) {
				dx = getMaxSpeed();
			}
		} else {
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
		
		// cannot move while attacking
		if(attacking) {
			dx = 0;
		}
		
		// falling
		if(falling) {
			if(dy > 0) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void tick() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
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
		} else if(left || right) {
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
		
		if(timer > 0) timer--;
		
		animation.tick();
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
		Point p = player.getScreenLocation();
		int x = p.x;
		int y = p.y;
		
		g.setColor(new Color(218, 165, 32));
		g.drawLine(p.x, p.y, (int) (this.x + xmap), (int) (this.y + ymap));
		
		if(MapObject.isHitboxEnabled()) {
			if(attacking) {
				g.setColor(Color.YELLOW);
				if(facingRight) {
					g.drawRect((int)(this.x + xmap), (int)(this.y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
				} else {
					g.drawRect((int)(this.x + xmap  - attackRange), (int)(this.y + ymap - height / 2 + (height - cheight) / 2), attackRange, cheight);
				}
			}
		}
		g.setColor(Color.WHITE);
	}
}