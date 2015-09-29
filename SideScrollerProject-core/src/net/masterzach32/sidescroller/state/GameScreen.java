package net.masterzach32.sidescroller.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.masterzach32.sidescroller.SideScrollerProject;
import net.masterzach32.sidescroller.levels.Level;
import net.masterzach32.sidescroller.levels.Level1;

public class GameScreen implements Screen {
	
    public SideScrollerProject game;
    public static OrthographicCamera camera;
    
    public Music bg_music;

	public GameScreen(SideScrollerProject game) {
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        
        new Level1(game, "Level 1");
     }

	public void show() {
		
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        
        game.batch.begin();
        Level.getLevel(0).render(delta);
        game.batch.end();
		
	}

	public void resize(int width, int height) {
		
	}

	public void pause() {
		
	}

	public void resume() {
		
	}

	public void hide() {
		
	}

	public void dispose() {
		bg_music.dispose();
	}
}