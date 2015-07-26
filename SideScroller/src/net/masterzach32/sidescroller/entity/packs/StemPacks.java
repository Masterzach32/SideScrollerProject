package net.masterzach32.sidescroller.entity.packs;

import net.masterzach32.sidescroller.entity.MapObject;
import net.masterzach32.sidescroller.entity.living.EntityPlayer;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class StemPacks extends MapObject {
	
	protected static EntityPlayer player;
	
	protected boolean remove;
	
	protected int type, strength;
	
	public static final int HEAL          = 0;
	public static final int ATTACK        = 1;
	public static final int SPEED         = 2;
	public static final int HEALTHREGEN   = 3;


	protected StemPacks(TileMap tm, int type, int strength) {
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		this.strength = strength;
		this.type = type;
		
		player = LevelState.getPlayer();
	}
	
	public boolean shouldRemove() {
		return remove;
	}

	public void tick() {}
	
}