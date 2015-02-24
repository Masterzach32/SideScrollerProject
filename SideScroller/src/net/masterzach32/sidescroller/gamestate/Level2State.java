package net.masterzach32.sidescroller.gamestate;

import java.awt.Graphics2D;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.gfx.HUD;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.Player;
import net.masterzach32.sidescroller.entity.enemy.Enemy;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.tilemap.TileMap;

@SuppressWarnings("unused")
public class Level2State extends LevelState {
	
	private TileMap tileMap;
	private Background bg;
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private int levelcomplete;
	
	private AudioPlayer bgMusic;

	public Level2State(SideScroller game) {
		super(game);
		init();
	}

	public void init() {
		// load enemies
		populateEnemies();
	}

	protected void load() {}

	protected void unload() {}
	
	public void levelCompleted() {}
	
	protected void populateEnemies() {}

	public void tick() {}

	public void render(Graphics2D g) {}

	public void keyPressed(int k) {}
	
	public void keyReleased(int k) {}

	public void mousePressed(int k) {}

	public void mouseReleased(int k) {}
	
}