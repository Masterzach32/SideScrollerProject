package net.masterzach32.sidescroller.entity.packs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.entity.Animation;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class HealthPack extends StemPacks {

	public HealthPack(TileMap tm, int strength) {
		super(tm, HEAL, strength);
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("pack_" + type);
			
			sprites = new BufferedImage[1];
			sprites[0] = spritesheet;
			
			animation = new Animation();
			animation.setFrames(sprites);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
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