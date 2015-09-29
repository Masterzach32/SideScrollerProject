package net.masterzach32.sidescroller.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import net.masterzach32.sidescroller.SideScrollerProject;
import net.masterzach32.sidescroller.state.GameScreen;

public abstract class Level {

	protected SideScrollerProject game;
	protected String levelName;
	
	protected World world;
	protected Box2DDebugRenderer debugRenderer;
	
	private static int currentLevel = 0;
	private static ArrayList<Level> levels = new ArrayList<Level>();
	
	protected Level(SideScrollerProject game, String levelName, float hgravity, float vgravity, boolean sleep) {
		this.game = game;
		this.levelName = levelName;
		
		world = new World(new Vector2(hgravity, vgravity), sleep);
		debugRenderer = new Box2DDebugRenderer();
		world.step(1/60f, 6, 2);
		debugRenderer.render(world, GameScreen.camera.combined);
		
		levels.add(this);
	}
	
	protected abstract void createEntities();
	
	public abstract void load();
	
	public abstract void unload();
	
	public abstract void render(float delta);
	
	protected abstract void onLevelComplete();
	
	public String getLevelName() {
		return levelName;
	}
	
	public static Level getNextLevel() {
		currentLevel++;
		return levels.get(currentLevel);
	}
	
	public static Level getLevel(int index) {
		return levels.get(index);
	}

	public static int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int level) {
		currentLevel = level;
	}
}