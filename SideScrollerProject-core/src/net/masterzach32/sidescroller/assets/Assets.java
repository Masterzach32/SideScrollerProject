package net.masterzach32.sidescroller.assets;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

	private static HashMap<String, Texture> textures = new HashMap<String, Texture>(1024);
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>(1024);
	private static HashMap<String, Music> music = new HashMap<String, Music>(1024);
	private static HashMap<String, FileHandle> fonts = new HashMap<String, FileHandle>(1024);
	
	public static void preinit() {
		// window assets
		textures.put("icon_console", new Texture(Gdx.files.internal("icons/console.png")));
		textures.put("icon_game", new Texture(Gdx.files.internal("icons/game.png")));
		
		// loading screen assets
		textures.put("shurima_bg", new Texture(Gdx.files.internal("backgrounds/shurima_bg.jpg")));
	}
	
	public static void init() {
		// Images
		
		// Particles
		textures.put("explosion", new Texture(Gdx.files.internal("sprites/enemies/explosion.gif")));
		textures.put("fireball", new Texture(Gdx.files.internal("sprites/player/fireball.gif")));
		textures.put("orb", new Texture(Gdx.files.internal("sprites/player/orb.png")));
		// Stem Packs
		textures.put("pack_" + 0, new Texture(Gdx.files.internal("sprites/packs/health_pack.png")));
		textures.put("pack_" + 1, new Texture(Gdx.files.internal("sprites/packs/damage_pack.png")));
		textures.put("pack_" + 2, new Texture(Gdx.files.internal("sprites/packs/speed_pack.png")));
		textures.put("pack_" + 3, new Texture(Gdx.files.internal("sprites/packs/health_pack.png")));
		// Effects Animations
		//textures.put("effects", new Texture(Gdx.files.internal("sprites/particles/effect_sprites.png")));
		// Spawn Animation
		textures.put("spawn_animation", new Texture(Gdx.files.internal("sprites/player/spawn_animation_base.png")));
		textures.put("spawn_animation_p", new Texture(Gdx.files.internal("sprites/player/spawn_animation_placeholder.png")));
		// Player
		textures.put("player", new Texture(Gdx.files.internal("sprites/player/playersprites.gif")));
		//textures.put("soldier", new Texture(Gdx.files.internal("sprites/player/soldier_sprites.png")));
		textures.put("player_blue", new Texture(Gdx.files.internal("sprites/player/player.png")));
		// Enemies
		textures.put("slugger", new Texture(Gdx.files.internal("sprites/enemies/slugger.gif")));
		textures.put("boss", new Texture(Gdx.files.internal("sprites/enemies/arachnik.gif")));
		// HUD
		textures.put("hud", new Texture(Gdx.files.internal("hud/hud.gif")));
		
		// Tile Sets
		textures.put("grasstileset", new Texture(Gdx.files.internal("tilesets/grasstileset.png")));
		textures.put("metaltileset", new Texture(Gdx.files.internal("tilesets/placeholder_metaltileset.png")));
		//textures.put("shurimatileset", new Texture(Gdx.files.internal("tilesets/shurimatileset.png")));
			
		// Backgrounds
		textures.put("grassbg", new Texture(Gdx.files.internal("backgrounds/grassbg1.gif")));
		textures.put("zaunbg", new Texture(Gdx.files.internal("backgrounds/zaun_bg.jpg")));
		textures.put("shurima_bright", new Texture(Gdx.files.internal("backgrounds/shurima_bright.png")));
		textures.put("shurima_dark", new Texture(Gdx.files.internal("backgrounds/shurima_dark.jpg")));
		textures.put("end_splash_normal", new Texture(Gdx.files.internal("backgrounds/azir_endGame.png")));
		textures.put("end_splash_galactic", new Texture(Gdx.files.internal("backgrounds/azir_galactic_endGame.jpg")));
		
		// Audio
		music.put("gamebosstheme", Gdx.audio.newMusic(Gdx.files.internal("music/gamebosstheme.mp3")));
		music.put("warriors", Gdx.audio.newMusic(Gdx.files.internal("music/warriors.mp3")));
		music.put("shurima", Gdx.audio.newMusic(Gdx.files.internal("music/shurima.mp3")));
		music.put("spawn", Gdx.audio.newMusic(Gdx.files.internal("music/spawn.mp3")));
		music.put("spawn2", Gdx.audio.newMusic(Gdx.files.internal("music/spawn_new.mp3")));
		music.put("level1_1", Gdx.audio.newMusic(Gdx.files.internal("music/level1_1theme.mp3")));
		music.put("level1_2", Gdx.audio.newMusic(Gdx.files.internal("music/level1_2theme.mp3")));
		
		// SFX
		sounds.put("jump", Gdx.audio.newSound(Gdx.files.internal("sfx/jump.mp3")));
		sounds.put("scratch", Gdx.audio.newSound(Gdx.files.internal("sfx/scratch.mp3")));
		sounds.put("complete", Gdx.audio.newSound(Gdx.files.internal("sfx/level_complete.mp3")));
		sounds.put("movement", Gdx.audio.newSound(Gdx.files.internal("sfx/soldier_movement.mp3")));
		sounds.put("start", Gdx.audio.newSound(Gdx.files.internal("sfx/start_quote.mp3")));
		sounds.put("soldier_spawn_0", Gdx.audio.newSound(Gdx.files.internal("sfx/soldier_spawn_0.mp3")));
		sounds.put("soldier_spawn_1", Gdx.audio.newSound(Gdx.files.internal("sfx/soldier_spawn_1.mp3")));
		
		// Fonts
		fonts.put("atari", Gdx.files.internal("fonts/atari.ttf"));
		fonts.put("galaxy", Gdx.files.internal("fonts/galaxyn.ttf"));
		fonts.put("gbb", Gdx.files.internal("fonts/gbb.ttf"));
	}
	
	/**
	 * Returns the given texture asset
	 * @param s
	 * @return BufferedImage
	 */
	public static Texture getTexutreAsset(String s) {
		return textures.get(s);
	}
	
	/**
	 * Returns the given sound asset
	 * @param s
	 * @return AudioInputStream
	 */
	public static Sound getAudioAsset(String s) {
		return sounds.get(s);
	}
	
	/**
	 * Returns the given music asset
	 * @param s
	 * @return String
	 */
	public static Music getMusicAsset(String s) {
		return music.get(s);
	}
	
	/**
	 * Returns the given font asset
	 * @param s
	 * @return BufferedImage
	 */
	public static FileHandle getFontAsset(String s) {
		return fonts.get(s);
	}
}