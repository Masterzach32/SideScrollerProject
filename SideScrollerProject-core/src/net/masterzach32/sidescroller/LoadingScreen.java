package net.masterzach32.sidescroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.masterzach32.sidescroller.assets.Assets;

public class LoadingScreen implements Screen {

    public SideScrollerProject game;
    public OrthographicCamera camera;

	public LoadingScreen(SideScrollerProject game) {
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
     }

	public void show() {
		
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(Assets.getTexutreAsset("shurima_bright"), 0, 0, 1280, 720);
        game.gbbMedium.setColor(Color.WHITE);
        game.gbbMedium.draw(game.batch, "Lodaing", 100, 150);
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
		
	}
}