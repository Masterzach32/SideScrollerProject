package net.masterzach32.sidescroller.entity.packs;

import java.awt.Graphics2D;
import net.masterzach32.sidescroller.entity.living.effects.Effect;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class SpeedPack extends StemPacks {

	public SpeedPack(TileMap tm, int strength) {
		super(tm, SPEED, strength);
	}
	
	public void tick() {
		if(player.intersects(this)) {
			player.addEffect(null, Effect.SPEED, strength, 8);
			remove = true;
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
	}
}