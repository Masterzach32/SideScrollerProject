package net.masterzach32.sidescroller.entity;

import net.masterzach32.sidescroller.entity.living.EntityPlayer;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class Packs extends MapObject {
	
	protected static EntityPlayer player;
	
	protected boolean remove;
	
	protected int type, strength;
	
	public static final int HEAL          = 0;
	public static final int ATTACK        = 1;
	public static final int SPEED         = 2;
	public static final int HEALTHREGEN   = 3;


	public Packs(TileMap tm, int strength) {
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		this.strength = strength;
		
		player = LevelState.getPlayer();
	}
	
	public boolean shouldRemove() {
		return remove;
	}
}