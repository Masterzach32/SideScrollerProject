package net.masterzach32.sidescroller.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.tilemap.TileMap;

public class HealthPack extends Packs {

	public HealthPack(TileMap tm, int strength) {
		super(tm, strength);
		
		type = HEAL;
		
		// load sprites
		try {
			BufferedImage spritesheet = Assets.getImageAsset("health_pack");
			
			sprites = new BufferedImage[1];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		if(player.intersects(this)) {
			player.setHealth(player.getHealth() + 2 * strength);
			remove = true;
		}
	}
	
	public void render(Graphics2D g) {
		super.render(g);
	}
}