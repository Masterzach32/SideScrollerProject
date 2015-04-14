package net.masterzach32.sidescroller.gamestate;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.gfx.HUD;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.EntityPlayer;
import net.masterzach32.sidescroller.entity.enemy.Enemy;
import net.masterzach32.sidescroller.entity.enemy.Slugger;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.tilemap.TileMap;
import net.masterzach32.sidescroller.util.LogHelper;

public class Level2State extends LevelState {
	
	private TileMap tileMap;
	private Background bg;
	private EntityPlayer player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private int levelcomplete = 3046;
	
	private AudioPlayer bgMusic;
	
	public Level2State(SideScroller game) {
		super(game);
		init();
	}
	
	public void init() {
		// load map
		tileMap = new TileMap(30);
		tileMap.loadTiles(Assets.getImageAsset("grasstileset"));
		tileMap.loadMap(Assets.getMapAsset("level1_2"));
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.7);
		
		bg = new Background(Assets.getImageAsset("grassbg"), 0.1);
		
		// load player
		player = new EntityPlayer(tileMap);
		
		// load enemies
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		// load assets
		hud = new HUD(player);
		
		bgMusic = new AudioPlayer(Assets.getAudioAsset("level1_2"));
	}
	
	protected void load() {
		player.setPosition(100, 100);
		bgMusic.play();
	}
	
	protected void unload() {
		bgMusic.stop();
	}
	
	public void levelCompleted() {
		LogHelper.logInfo("Level 2 Completed!");
		GameState.setState(SideScroller.endgame);
	}
	
	protected void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {new Point(200, 100), new Point(860, 300), new Point(1525, 300), new Point(1680, 300), new Point(1800, 300)};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
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
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getx(), e.gety()));
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
		
		if (player.getx() >= levelcomplete) levelCompleted();
	}
	
	public void render(Graphics2D g) {
		// draw bg
		bg.render(g);
		
		// draw tilemap
		tileMap.render(g);
		
		// draw player
		player.render(g);
		
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
		if(k == KeyEvent.VK_A) player.setLeft(true);
		if(k == KeyEvent.VK_D) player.setRight(true);
		if(k == KeyEvent.VK_W) player.setUp(true);
		if(k == KeyEvent.VK_S) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		if(k == KeyEvent.VK_SPACE) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching();
		if(k == KeyEvent.VK_F) player.setFiring();
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(false);
		if(k == KeyEvent.VK_D) player.setRight(false);
		if(k == KeyEvent.VK_W) player.setUp(false);
		if(k == KeyEvent.VK_S) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}

	public void mousePressed(int k) {
		if(k == MouseEvent.BUTTON1_DOWN_MASK) player.setScratching();
		if(k == MouseEvent.BUTTON3_DOWN_MASK) player.setFiring();
	}

	public void mouseReleased(int k) {}
	
}