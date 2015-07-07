package net.masterzach32.sidescroller.gamestate.levels;

import java.awt.*;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.entity.*;
import net.masterzach32.sidescroller.entity.enemy.*;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.*;
import net.masterzach32.sidescroller.util.LogHelper;

public class Level1State extends LevelState {
	
	public Level1State(SideScroller game) {
		super(game);
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
		Point[] points = new Point[] {new Point(200, 100), new Point(860, 300), new Point(1525, 300), new Point(1680, 300), new Point(1800, 300), new Point(2800, 300)};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap, 1);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
		Swordman sw;
		Point[] swp = new Point[] {new Point(1000, 320), new Point(2400, 320) };
		for(int i = 0; i < swp.length; i++) {
			sw = new Swordman(tileMap, 1);
			sw.setPosition(swp[i].x, swp[i].y);
			enemies.add(sw);
		}
		
		Boss boss = new Boss(tileMap, 1);
		boss.setPosition(3046, 320);
		enemies.add(boss);
	}
}