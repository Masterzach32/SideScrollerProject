package net.masterzach32.sidescroller.levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.masterzach32.sidescroller.SideScrollerProject;
import net.masterzach32.sidescroller.state.GameScreen;

public class Level1 extends Level {

	public Level1(SideScrollerProject game, String levelName) {
		super(game, levelName, 0, -10, true);
		// Create our body definition
		BodyDef groundBodyDef =new BodyDef();  
		// Set its world position
		groundBodyDef.position.set(new Vector2(0, 15));  

		// Create a body from the defintion and add it to the world
		Body groundBody = world.createBody(groundBodyDef);  

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(GameScreen.camera.viewportWidth, 10.0f);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		// Clean up after ourselves
		groundBox.dispose();
	}

	protected void createEntities() {
		
	}

	public void load() {
		
	}

	public void unload() {
		
	}

	public void render(float delta) {
		debugRenderer.render(world, GameScreen.camera.combined);
	}

	protected void onLevelComplete() {
		
	}
}