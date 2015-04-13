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
		images = new HashMap<String, BufferedImage>();
		sounds = new HashMap<String, AudioInputStream>();
		maps = new HashMap<String, String>();
		// load into hashmap
		// Images
		images.put("explosion", SideScroller.im.load("/sprites/enemies/explosion.gif"));
		images.put("fireball", SideScroller.im.load("/sprites/player/fireball.gif"));
		images.put("player", SideScroller.im.load("/sprites/player/playersprites.gif"));
		images.put("slugger", SideScroller.im.load("/sprites/enemies/slugger.gif"));
		
		images.put("mouse", SideScroller.im.load("/hud/mouse.png"));
		images.put("hud", SideScroller.im.load("/hud/hud.gif"));
		images.put("hud2", SideScroller.im.load("hud/placeholder_hud.png"));
		
		// Tile Sets
		images.put("grasstileset", SideScroller.im.load("/tilesets/grasstileset.png"));
		images.put("metaltileset", SideScroller.im.load("/tilesets/placeholder_metaltileset.png"));
		
		// Backgrounds
		images.put("grassbg", SideScroller.im.load("/backgrounds/grassbg1.gif"));
		images.put("level1_1bg", SideScroller.im.load("/backgrounds/placeholder_level1_1.png"));
		
		// Audio
		sounds.put("gamebosstheme", SideScroller.am.load("/music/gamebosstheme.mp3"));
		sounds.put("warriors", SideScroller.am.load("/music/warriors.mp3"));
		sounds.put("level1_1", SideScroller.am.load("/music/level1_1theme.mp3"));
		
		sounds.put("jump", SideScroller.am.load("/SFX/jump.mp3"));
		sounds.put("scratch", SideScroller.am.load("/SFX/scratch.mp3"));
		sounds.put("fire", SideScroller.am.load("/SFX/fire.mp3"));
		
		// Maps
		maps.put("level1_1", "/maps/level1-1.map");
		maps.put("level1_2", "/maps/level1-2.map");
		maps.put("level1_3", "/maps/level1-3.map");
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
	 * Returns the given sound asset
	 * @param s
	 * @return AudioInputStream
	 */
	public static AudioInputStream getSoundAsset(String s) {
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
