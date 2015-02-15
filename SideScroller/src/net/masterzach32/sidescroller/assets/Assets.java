package net.masterzach32.sidescroller.assets;

import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioInputStream;

import net.masterzach32.sidescroller.main.SideScroller;

public class Assets {

	/** Images */
	public static BufferedImage explosion, fireball, hud, player, slugger;
	/** Tile sets */
	public static BufferedImage grasstileset, metaltileset;
	/** Backgrounds */
	public static BufferedImage menubg, level1_1bg;
	/** Sfx and Music */
	public static AudioInputStream level1_1m, jump, scratch;
	/** Maps */
	public static String level1_1;
	
	public static void init() {
		// Images
		explosion = SideScroller.im.load("/sprites/enemies/explosion.gif");
		fireball = SideScroller.im.load("/sprites/player/fireball.gif");
		hud = SideScroller.im.load("/hud/hud.gif");
		player = SideScroller.im.load("/sprites/player/playersprites.gif");
		slugger = SideScroller.im.load("/sprites/enemies/slugger.gif");
		
		// Tile Sets
		grasstileset = SideScroller.im.load("/tilesets/grasstileset.gif");
		
		// Backgrounds
		menubg = SideScroller.im.load("/backgrounds/grassbg1.gif");
		level1_1bg = menubg;
		
		// Audio
		level1_1m = SideScroller.am.load("/music/gamebosstheme.mp3");
		//level1_1m = SideScroller.am.load("/music/level1-1.mp3");
		jump = SideScroller.am.load("/SFX/jump.mp3");
		scratch = SideScroller.am.load("/SFX/scratch.mp3");
		
		// Maps
		level1_1 = "/maps/level1-1.map";
	}
}
