package net.masterzach32.sidescroller.gamestate.levels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.sfx.AudioPlayer;
import net.masterzach32.sidescroller.gamestate.GameState;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.tilemap.Background;
import net.masterzach32.sidescroller.util.Stats;
import net.masterzach32.sidescroller.util.Utilities;

public class EndState extends GameState {
	
	private AudioPlayer bgMusic;
	private Background bg;
	
	public EndState(SideScroller game) {
		super(game);
	}

	@Override
	public void init() {
		bg = new Background(Assets.getImageAsset("end_splash_normal"), 1);
		
		bgMusic = new AudioPlayer(Assets.getAudioAsset("shurima"));
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
		Font f = new Font("Arial", Font.PLAIN, 12);
		g.setFont(f);
		bg.render(g);
		g.setColor(Color.BLACK);
		for(int i = 0; i < Stats.getNumberOfStats(); i++) {
			Utilities.drawCenteredString(g, Stats.getType(i) + ": " +  Integer.toString((int) Stats.getStat(i)), 100 + (i * 15));
		}
		g.setColor(Color.WHITE);
		g.drawString("Game Completed! Press any key to exit", 1, 354);
	}

	@Override
	public void keyPressed(int k) {
		SideScroller.getGame().stop();
	}

	@Override
	public void keyReleased(int k) {}

	@Override
	public void mousePressed(int k) {}

	@Override
	public void mouseReleased(int k) {}

}
