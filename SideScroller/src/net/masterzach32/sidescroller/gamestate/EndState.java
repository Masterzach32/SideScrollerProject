package net.masterzach32.sidescroller.gamestate;

import java.awt.Graphics2D;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;

public class EndState extends GameState {
	
	private AudioPlayer bgMusic;
	private Background bg;

	public EndState(SideScroller game) {
		super(game);
	}

	@Override
	public void init() {
		bg = new Background(Assets.getImageAsset("grassbg"), 1);
		bg.setVector(-0.25, 0);
		
		bgMusic = new AudioPlayer(Assets.getSoundAsset("warriors"));
	}

	@Override
	protected void load() {
		bgMusic.play();
	}

	@Override
	protected void unload() {
		bgMusic.stop();
	}

	@Override
	public void tick() {
		bg.tick();
	}

	@Override
	public void render(Graphics2D g) {
		bg.render(g);		
	}

	@Override
	public void keyPressed(int k) {}

	@Override
	public void keyReleased(int k) {}

	@Override
	public void mousePressed(int k) {}

	@Override
	public void mouseReleased(int k) {}

}
