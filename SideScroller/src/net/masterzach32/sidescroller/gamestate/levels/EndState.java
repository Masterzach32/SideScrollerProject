package net.masterzach32.sidescroller.gamestate.levels;

import java.awt.Graphics2D;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.util.LogHelper;

public class EndState extends GameState {
	
	private AudioPlayer bgMusic;
	private Background bg;

	public EndState(SideScroller game) {
		super(game);
		init();
	}

	@Override
	public void init() {
		bg = new Background(Assets.getImageAsset("zaunbg"), 1);
		bg.setVector(-0.25, 0);
		
		bgMusic = new AudioPlayer(Assets.getAudioAsset("warriors"));
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
		g.drawString("Game Completed! Press any key to exit", 100, 100);
	}

	@Override
	public void keyPressed(int k) {
		SideScroller.stop();
	}

	@Override
	public void keyReleased(int k) {}

	@Override
	public void mousePressed(int k) {}

	@Override
	public void mouseReleased(int k) {}

}
