package net.masterzach32.sidescroller.gamestate.levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.gfx.HUD;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.living.EntityPlayer;
import net.masterzach32.sidescroller.entity.living.enemy.Boss;
import net.masterzach32.sidescroller.entity.living.enemy.Enemy;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.gamestate.menus.KeyConfigState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.tilemap.TileMap;
import net.masterzach32.sidescroller.util.Utilities;

public abstract class LevelState extends GameState {
	
	protected static EntityPlayer player;
	protected static TileMap tileMap;
	protected static int i = 0, spawnTimer = 65;
	protected static HUD hud;
	protected static Animation animation;
	protected static int width, height;
	protected static BufferedImage[] sprites;
	protected static AudioPlayer spawnSound;
	protected boolean levelComplete = false;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<Explosion> explosions;
	protected Background bg;
	
	protected AudioPlayer bgMusic;
	
	private static LevelState[] levels;
	
	public static int j = 300;

	public LevelState(SideScroller game) {
		super(game);
	}
	
	public static void loadLevels() {
		tileMap = new TileMap(30);
		player = new EntityPlayer(tileMap);
		
		// load levels
		levels = new LevelState[2];
		levels[0] = new Level1State(game);
		levels[1] = new Level2State(game);
		
		// load assets
		hud = new HUD(player);
		spawnSound = new AudioPlayer(Assets.getAudioAsset("spawn2"));
		
		BufferedImage spritesheet = Assets.getImageAsset("spawn_animation_p");
		
		width = 30;
		height = 30;
		sprites = new BufferedImage[8];
		for(int i = 0; i < sprites.length; i++) {
			sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(160);
	}
	
	/**
	 * Only use this with game levels, called when the level is completed
	 */
	public abstract void levelCompleted();
	
	/**
	 * Spawns the enemies on the map
	 */
	protected abstract void populateEnemies();
	
	public static EntityPlayer getPlayer() {
		return player;
	}
	
	protected void renderSpawnAnimation(Graphics2D g) {
		if(i <= spawnTimer) i++;
		if(!animation.hasPlayedOnce()) {
			Point p = player.getScreenLocation();
			animation.tick();
			animation.render(g, p.x, p.y, width, height);
		} else {
			
		}
	}
	
	public void tick() {
		// update player
		player.tick();
		player.checkAttack(enemies);
		//LogHelper.logInfo(player.getx() + ", " + player.gety());
		
		
		// set background
		tileMap.setPosition(SideScroller.WIDTH / 2 - player.getx(), SideScroller.HEIGHT / 2 - player.gety());
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.tick();
			if(e.isDead()) {
				player.setExp(player.getExp() + e.getXpGain());
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getx(), e.gety()));
				if (e instanceof Boss) levelCompleted();
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
	}
	
	public void render(Graphics2D g) {
		// draw bg
		bg.render(g);
		
		// draw tilemap
		tileMap.render(g);
		
		// draw player
		renderSpawnAnimation(g);
		if(i >= spawnTimer) player.render(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).render(g);
		}
		
		// draw hud
		hud.render(g);
		
		if(player.isDead()) {
			Utilities.drawCenteredString(g, "You Died!", 180);
			if(j > 0) j--;
			if(j == 0) Utilities.drawCenteredString(g, "Press any key to respawn", 200);
		}
	}
	
	public void keyPressed(int k) {
		if(!player.isDead()) {
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_LEFT]) player.setLeft(true);
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_RIGHT]) player.setRight(true);
			if(k == KeyEvent.VK_W) player.setUp(true);
			if(k == KeyEvent.VK_S) player.setDown(true);
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_JUMP]) player.setJumping(true);
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_GLIDE]) player.setGliding(true);
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_SCRATCH]) player.setScratching();
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_ORB]) player.setFiring();
			if(k == KeyEvent.VK_ESCAPE) GameState.setState(SideScroller.menuState);
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_REWIND]) player.rewind();
		}
		if(player.isDead() && j == 0) { 
			player.respawn();
			j = 300;
		}
	}
	
	public void keyReleased(int k) {
		if(!player.isDead()) {
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_LEFT]) player.setLeft(false);
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_RIGHT]) player.setRight(false);
			if(k == KeyEvent.VK_W) player.setUp(false);
			if(k == KeyEvent.VK_S) player.setDown(false);
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_JUMP]) player.setJumping(false);
			if(k == KeyConfigState.keyBinding[KeyConfigState.KEY_GLIDE]) player.setGliding(false);
		}
	}
	
	public void mousePressed(int k) {
		if(!player.isDead()) {
			if(k == MouseEvent.BUTTON1_DOWN_MASK) player.setScratching();
			if(k == MouseEvent.BUTTON3_DOWN_MASK) player.setFiring();
		}
	}
	
	public void mouseReleased(int k) {}
	
}
