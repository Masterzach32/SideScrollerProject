package net.masterzach32.sidescroller.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.masterzach32.sidescroller.SideScrollerProject;
import net.masterzach32.sidescroller.assets.Assets;

public class MainMenu implements Screen {
	
    public SideScrollerProject game;
    public OrthographicCamera camera;
    
    public Music bg_music;
    
	public static String[] options = {
		"Play",
		"Help", 
		"About", 
		"Options", 
		"Quit"
	};
	
	protected static String info = "Use the Up and Down arrows to navigate, and ENTER to select.";
	
	public int currentChoice;
    
    public MainMenu(SideScrollerProject game) {
       this.game = game;
       
       bg_music = Assets.getMusicAsset("warriors");
       
       bg_music.setVolume(0.2f);
       bg_music.play();
       bg_music.setLooping(true);

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
        game.gbbLarge.setColor(Color.RED);
        game.gbbLarge.draw(game.batch, "SideScroller Project", 100, 150);
        game.gbbLarge.setColor(Color.WHITE);
        game.gbbMedium.draw(game.batch, info, 869, 15);
        
        for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
		        game.gbbLarge.setColor(Color.BLACK);
			} else {
				game.gbbLarge.setColor(Color.RED);
			}
			game.gbbLarge.draw(game.batch, options[i], 5, (150 - i * 30));
        }
        game.batch.end();

        if (Gdx.input.isKeyPressed(Keys.F)) {
        	Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
        } else if (Gdx.input.isKeyPressed(Keys.G)) {
        	Gdx.graphics.setDisplayMode(1280, 720, false);
        } else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
        } else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
        } else if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
        	select();
        }
	}
	
	private void select() {
		if(currentChoice == 0) {
			game.setScreen(new GameScreen(game));
			bg_music.stop();
		}
		if(currentChoice == 1)
			Gdx.app.log("SideScrollerProject", "help");
		if(currentChoice == 2)
			Gdx.app.log("SideScrollerProject", "about");
		if(currentChoice == 3)
			Gdx.app.log("SideScrollerProject", "options");
		if(currentChoice == 4)
			System.exit(0);
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