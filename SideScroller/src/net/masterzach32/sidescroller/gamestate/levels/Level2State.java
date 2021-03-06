package net.masterzach32.sidescroller.gamestate.levels;

import java.awt.Point;
import java.util.ArrayList;

import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Explosion;
import net.masterzach32.sidescroller.entity.living.enemy.Boss;
import net.masterzach32.sidescroller.entity.living.enemy.Enemy;
import net.masterzach32.sidescroller.entity.living.enemy.Mage;
import net.masterzach32.sidescroller.entity.living.enemy.Slugger;
import net.masterzach32.sidescroller.entity.living.enemy.Swordman;
import net.masterzach32.sidescroller.entity.packs.DamagePack;
import net.masterzach32.sidescroller.entity.packs.HealthPack;
import net.masterzach32.sidescroller.entity.packs.RegenPack;
import net.masterzach32.sidescroller.entity.packs.SpeedPack;
import net.masterzach32.sidescroller.entity.packs.StemPacks;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.util.LogHelper;

public class Level2State extends LevelState {
	
	public Level2State(SideScroller game) {
		super(game);
	}
	
	public void init() {
		levelComplete = false;
		bg = new Background(Assets.getImageAsset("shurima_bg"), 0.1);
		
		explosions = new ArrayList<Explosion>();
		
		// load enemies
		populateTileMap();
		
		bgMusic = new AudioPlayer(Assets.getAudioAsset("level1_2"));
	}
	
	protected void load() {
		spawnSound.play();
		player.setPosition(100, 100);
		
		// load map
		tileMap.loadTiles(Assets.getImageAsset("grasstileset"));
		tileMap.loadMap(Assets.getMapAsset("level1_2"));
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
		LogHelper.logger.logInfo("Level 2 Completed!");
		GameState.setState(SideScroller.endgame);
	}
	
	protected void populateTileMap() {
		enemies = new ArrayList<Enemy>();
		Slugger s;
		Point[] points = new Point[] {new Point(200, 100), new Point(860, 300), new Point(1700, 250), new Point(2000, 110)};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap, 2);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
		Swordman sw;
		Point[] swp = new Point[] {new Point(1000, 320), new Point(1500, 100), new Point(2400, 320),};
		for(int i = 0; i < swp.length; i++) {
			sw = new Swordman(tileMap, 2);
			sw.setPosition(swp[i].x, swp[i].y);
			enemies.add(sw);
		}
		
		Mage m;
		Point[] mp = new Point[] {new Point(1800, 100), new Point(2700, 320)};
		for(int i = 0; i < mp.length; i++) {
			m = new Mage(tileMap, 2);
			m.setPosition(mp[i].x, mp[i].y);
			enemies.add(m);
		}
		
		Boss boss = new Boss(tileMap, 2);
		boss.setPosition(3046, 320);
		enemies.add(boss);
		
		stemPacks = new ArrayList<StemPacks>();
		
		HealthPack sp1 = new HealthPack(tileMap, 4);
		sp1.setPosition(2800, 320);
		
		DamagePack sp2 = new DamagePack(tileMap, 4);
		sp2.setPosition(2750, 320);
		
		SpeedPack sp3 = new SpeedPack(tileMap, 4);
		sp3.setPosition(150, 110);
		
		RegenPack sp4 = new RegenPack(tileMap, 4);
		sp4.setPosition(180, 110);
		
		stemPacks.add(sp1);
		stemPacks.add(sp2);
		stemPacks.add(sp3);
		stemPacks.add(sp4);
	}
}