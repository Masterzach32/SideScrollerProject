package net.masterzach32.sidescroller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.state.MainMenu;

public class SideScrollerProject extends Game {
	
	public SpriteBatch batch;
	public BitmapFont gbbSmall, gbbMedium, gbbLarge;
	
	FPSLogger fps = new FPSLogger();
	
	public void create() {
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/gbb.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 12;
		gbbSmall = generator.generateFont(parameter);
		parameter.size = 28;
		gbbMedium = generator.generateFont(parameter);
		parameter.size = 40;
		gbbLarge = generator.generateFont(parameter);
		generator.dispose(); 
		Assets.preinit();
		this.setScreen(new LoadingScreen(this));
		Assets.init();
		this.setScreen(new MainMenu(this));
	}

	public void render() {
		super.render();
		fps.log();
	}
	
    public void dispose() {
        batch.dispose();
    }
}