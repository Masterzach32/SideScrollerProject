package net.masterzach32.sidescroller.entity.packs;

import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.EntityPlayer;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;

public abstract class StemPacks extends MapObject {
	
	protected static EntityPlayer player;
	
	protected boolean remove;
	
	protected int type, strength;
	
	public static final int HEAL          = 0;
	public static final int ATTACK        = 1;
	public static final int SPEED         = 2;
	public static final int HEALTHREGEN   = 3;


	public StemPacks(TileMap tm, int type, int strength) {
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		this.strength = strength;
		this.type = type;
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("pack_" + type);
			
			sprites = new BufferedImage[1];
			sprites[0] = spritesheet;
			
			animation = new Animation();
			animation.setFrames(sprites);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		player = LevelState.getPlayer();
	}
	
	public boolean shouldRemove() {
		return remove;
	}

	public abstract void tick();
	
}