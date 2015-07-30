package net.masterzach32.sidescroller.entity.packs;

import java.awt.Graphics2D;
import net.masterzach32.sidescroller.entity.living.effects.Effect;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class DamagePack extends StemPacks {

	public DamagePack(TileMap tm, int strength) {
		super(tm, ATTACK, strength);
	}
	
	public void tick() {
		if(player.intersects(this)) {
			player.addEffect(null, Effect.ATTACK, strength, 10);
			remove = true;
		}
	}
	
	public void render(Graphics2D g) {
		setMapPosition();
		super.render(g);
	}
}