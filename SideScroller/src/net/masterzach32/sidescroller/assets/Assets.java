package net.masterzach32.sidescroller.assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;

import net.masterzach32.sidescroller.main.SideScroller;

public class Assets {
	
	// Anything with placeholder in front of it is a placeholder art
	
	private static HashMap<String, BufferedImage> images;
	private static HashMap<String, AudioInputStream> sounds;
	private static HashMap<String, String> maps;
	
	public static void init() {
		// initialize hashmaps
		images = new HashMap<String, BufferedImage>(1024);
		sounds = new HashMap<String, AudioInputStream>(1024);
		maps = new HashMap<String, String>(1024);
		// load into hashmap
		// Images
		images.put("explosion", SideScroller.il.load("/sprites/enemies/explosion.gif"));
		images.put("fireball", SideScroller.il.load("/sprites/player/fireball.gif"));
		images.put("orb", SideScroller.il.load("/sprites/player/orb.png"));
		images.put("spawn_animation", SideScroller.il.load("/sprites/player/spawn_animation_base.png"));
		images.put("spawn_animation_p", SideScroller.il.load("/sprites/player/spawn_animation_placeholder.png"));
		images.put("player", SideScroller.il.load("/sprites/player/playersprites.gif"));
		images.put("player_blue", SideScroller.il.load("/sprites/player/player.png"));
		images.put("slugger", SideScroller.il.load("/sprites/enemies/slugger.gif"));
		images.put("boss", SideScroller.il.load("/sprites/enemies/boss.png"));
		
		images.put("mouse", SideScroller.il.load("/hud/mouse.png"));
		images.put("hud", SideScroller.il.load("/hud/hud.gif"));
		images.put("hud2", SideScroller.il.load("/hud/placeholder_hud.png"));
		
		// Tile Sets
		images.put("grasstileset", SideScroller.il.load("/tilesets/grasstileset.png"));
		images.put("metaltileset", SideScroller.il.load("/tilesets/placeholder_metaltileset.png"));
		
		// Backgrounds
		images.put("grassbg", SideScroller.il.load("/backgrounds/grassbg1.gif"));
		images.put("zaunbg", SideScroller.il.load("/backgrounds/zaun_bg.jpg"));
		images.put("level1_1bg", SideScroller.il.load("/backgrounds/placeholder_level1_1.png"));
		
		// Audio
		sounds.put("gamebosstheme", SideScroller.al.load("/music/gamebosstheme.mp3"));
		sounds.put("warriors", SideScroller.al.load("/music/warriors.mp3"));
		sounds.put("spawn", SideScroller.al.load("/music/spawn.mp3"));
		sounds.put("spawn2", SideScroller.al.load("/music/spawn_new.mp3"));
		sounds.put("level1_1", SideScroller.al.load("/music/level1_1theme.mp3"));
		sounds.put("level1_2", SideScroller.al.load("/music/level1_2theme.mp3"));
		
		sounds.put("jump", SideScroller.al.load("/sfx/jump.mp3"));
		sounds.put("scratch", SideScroller.al.load("/sfx/scratch.mp3"));
		
		// Maps
		maps.put("level1_1", SideScroller.ml.load("/maps/level1-1.map"));
		maps.put("level1_2", SideScroller.ml.load("/maps/level1-2.map"));
		maps.put("level1_3", SideScroller.ml.load("/maps/level1-3.map"));
	}
	
	/**
	 * Returns the given image asset
	 * @param s
	 * @return BufferedImage
	 */
	public static BufferedImage getImageAsset(String s) {
		return images.get(s);
	}
	
	/**
	 * Returns the given audio asset
	 * @param s
	 * @return AudioInputStream
	 */
	public static AudioInputStream getAudioAsset(String s) {
		return sounds.get(s);
	}
	
	/**
	 * Returns the given map asset
	 * @param s
	 * @return String
	 */
	public static String getMapAsset(String s) {
		return maps.get(s);
	}
}
