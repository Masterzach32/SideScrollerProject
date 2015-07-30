package net.masterzach32.sidescroller.entity.packs;

import java.awt.Graphics2D;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class HealthPack extends StemPacks {

	public HealthPack(TileMap tm, int strength) {
		super(tm, HEAL, strength);
	}
	
	public void tick() {
		if(player.intersects(this)) {
			player.heal((float) (2 * strength));
			remove = true;
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
	}
}