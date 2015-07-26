package net.masterzach32.sidescroller.assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;

import net.masterzach32.sidescroller.gamestate.menus.LoadingState;
import net.masterzach32.sidescroller.main.SideScroller;

public class Assets {
	
	// Anything with placeholder in front of it is a placeholder art
	
	private static HashMap<String, BufferedImage> images;
	private static HashMap<String, AudioInputStream> sounds;
	private static HashMap<String, String> maps;
	
	public static void preinit() {
		// initialize hashmaps
		images = new HashMap<String, BufferedImage>(1024);
		sounds = new HashMap<String, AudioInputStream>(1024);
		maps = new HashMap<String, String>(1024);
		
		// loading screen assets
		images.put("zaunbg", SideScroller.al.loadImage("/backgrounds/zaun_bg.jpg"));
	}
	
	public static void init() {
		// load into hashmap
		// Images
		LoadingState.setInfo("Loading Images...", 20);
		
		images.put("explosion", SideScroller.al.loadImage("/sprites/enemies/explosion.gif"));
		images.put("fireball", SideScroller.al.loadImage("/sprites/player/fireball.gif"));
		images.put("orb", SideScroller.al.loadImage("/sprites/player/orb.png"));
		images.put("health_pack", SideScroller.al.loadImage("/sprites/packs/healthPack.png"));
		images.put("spawn_animation", SideScroller.al.loadImage("/sprites/player/spawn_animation_base.png"));
		images.put("spawn_animation_p", SideScroller.al.loadImage("/sprites/player/spawn_animation_placeholder.png"));
		images.put("player", SideScroller.al.loadImage("/sprites/player/playersprites.gif"));
		images.put("player_blue", SideScroller.al.loadImage("/sprites/player/player.png"));
		images.put("slugger", SideScroller.al.loadImage("/sprites/enemies/slugger.gif"));
		images.put("boss", SideScroller.al.loadImage("/sprites/enemies/boss.png"));
		
		images.put("mouse", SideScroller.al.loadImage("/hud/mouse.png"));
		images.put("hud", SideScroller.al.loadImage("/hud/hud.gif"));
		images.put("hud2", SideScroller.al.loadImage("/hud/placeholder_hud.png"));
		
		// Tile Sets
		images.put("grasstileset", SideScroller.al.loadImage("/tilesets/grasstileset.png"));
		images.put("metaltileset", SideScroller.al.loadImage("/tilesets/placeholder_metaltileset.png"));
		
		// Backgrounds
		images.put("grassbg", SideScroller.al.loadImage("/backgrounds/grassbg1.gif"));
		images.put("zaunbg", SideScroller.al.loadImage("/backgrounds/zaun_bg.jpg"));
		images.put("level1_1bg", SideScroller.al.loadImage("/backgrounds/placeholder_level1_1.png"));
		
		LoadingState.setInfo("Loading Audio...", 40);
		
		// Audio
		sounds.put("gamebosstheme", SideScroller.al.loadAudio("/music/gamebosstheme.mp3"));
		sounds.put("warriors", SideScroller.al.loadAudio("/music/warriors.mp3"));
		sounds.put("spawn", SideScroller.al.loadAudio("/music/spawn.mp3"));
		sounds.put("spawn2", SideScroller.al.loadAudio("/music/spawn_new.mp3"));
		sounds.put("level1_1", SideScroller.al.loadAudio("/music/level1_1theme.mp3"));
		sounds.put("level1_2", SideScroller.al.loadAudio("/music/level1_2theme.mp3"));
		
		sounds.put("jump", SideScroller.al.loadAudio("/sfx/jump.mp3"));
		sounds.put("scratch", SideScroller.al.loadAudio("/sfx/scratch.mp3"));
		
		LoadingState.setInfo("Loading Maps...", 60);
		
		// Maps
		maps.put("level1_1", SideScroller.al.loadMap("/maps/level1-1.map"));
		maps.put("level1_2", SideScroller.al.loadMap("/maps/level1-2.map"));
		maps.put("level1_3", SideScroller.al.loadMap("/maps/level1-3.map"));
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
