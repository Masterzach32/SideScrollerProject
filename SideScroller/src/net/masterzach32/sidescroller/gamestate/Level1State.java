package net.masterzach32.sidescroller.gamestate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.*;
import net.masterzach32.sidescroller.entity.enemy.*;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.*;
import net.masterzach32.sidescroller.util.LogHelper;

public class Level1State extends LevelState {
	
	private Background bg;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private AudioPlayer bgMusic;
	
	public Level1State(SideScroller game) {
		super(game);
		init();
	}
	
	public void init() {
		levelComplete = false;
		bg = new Background(Assets.getImageAsset("grassbg"), 0.1);
		
		explosions = new ArrayList<Explosion>();
		
		// load enemies
		populateEnemies();
		
		bgMusic = new AudioPlayer(Assets.getAudioAsset("level1_1"));
	}
	
	protected void load() {
		spawnSound.play();
		player.setPosition(100, 100);
		
		// load map
		tileMap.loadTiles(Assets.getImageAsset("grasstileset"));
		tileMap.loadMap(Assets.getMapAsset("level1_1"));
		tileMap.setPosition(0, 0);
		
		bgMusic.play();
		if(levelComplete) levelCompleted();
	}
	
	protected void unload() {
		bgMusic.stop();
		animation.setPlayedOnce(false);
		i = 0;
	}
	
	public void levelCompleted() {
		levelComplete = true;
		LogHelper.logInfo("Level 1 Completed!");
		GameState.setState(SideScroller.level1_2);
	}
	
	protected void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		Slugger s;
		Point[] points = new Point[] {new Point(200, 100), new Point(860, 300), new Point(1525, 300), new Point(1680, 300), new Point(1800, 300)};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap, 1);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		Boss boss = new Boss(tileMap, 1);
		boss.setPosition(3046, 320);
		enemies.add(boss);
	}
	
	public void tick() {
		// update player
		player.tick();
		player.checkAttack(enemies);
		if(player.isDead()) explosions.add(new Explosion(player.getx(), player.gety()));
		
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
	}
	
	public void keyPressed(int k) {
		KeyConfigState kc = SideScroller.keyConfigState;
		if(k == kc.keyBinding[1]) player.setLeft(true);
		if(k == kc.keyBinding[0]) player.setRight(true);
		if(k == KeyEvent.VK_W) player.setUp(true);
		if(k == KeyEvent.VK_S) player.setDown(true);
		if(k == kc.keyBinding[2]) player.setJumping(true);
		if(k == kc.keyBinding[5]) player.setGliding(true);
		if(k == kc.keyBinding[3]) player.setScratching();
		if(k == kc.keyBinding[4]) player.setFiring();
		if(k == KeyEvent.VK_F5) GameState.setState(SideScroller.menuState);
	}
	
	public void keyReleased(int k) {
		KeyConfigState kc = SideScroller.keyConfigState;
		if(k == kc.keyBinding[1]) player.setLeft(false);
		if(k == kc.keyBinding[0]) player.setRight(false);
		if(k == KeyEvent.VK_W) player.setUp(false);
		if(k == KeyEvent.VK_S) player.setDown(false);
		if(k == kc.keyBinding[2]) player.setJumping(false);
		if(k == kc.keyBinding[5]) player.setGliding(false);
	}

	public void mousePressed(int k) {
		if(k == MouseEvent.BUTTON1_DOWN_MASK) player.setScratching();
		if(k == MouseEvent.BUTTON3_DOWN_MASK) player.setFiring();
	}

	public void mouseReleased(int k) {}
	
}