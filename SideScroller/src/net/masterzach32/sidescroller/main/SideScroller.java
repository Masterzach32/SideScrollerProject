package net.masterzach32.sidescroller.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.JPanel;

import net.masterzach32.sidescroller.api.IUpdatable;
import net.masterzach32.sidescroller.assets.AssetLoader;
import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.gamestate.*;
import net.masterzach32.sidescroller.gamestate.levels.EndState;
import net.masterzach32.sidescroller.gamestate.levels.Level1State;
import net.masterzach32.sidescroller.gamestate.levels.Level2State;
import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.gamestate.menus.AboutState;
import net.masterzach32.sidescroller.gamestate.menus.HelpState;
import net.masterzach32.sidescroller.gamestate.menus.KeyConfigState;
import net.masterzach32.sidescroller.gamestate.menus.LoadingState;
import net.masterzach32.sidescroller.gamestate.menus.MenuState;
import net.masterzach32.sidescroller.gamestate.menus.OptionsState;
import net.masterzach32.sidescroller.util.*;

@SuppressWarnings("serial")
public class SideScroller extends JPanel implements Runnable, KeyListener, MouseListener, IUpdatable {
	
	// dimensions
	public static int WIDTH = 640;
	public static int HEIGHT = 360;
	public static int SCALE = 2;
	public static final String VERSION = "0.1.5.198";
	
	// game thread
	private Thread thread;
	private static SideScroller game;
	private boolean running;
	public static int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	public static boolean isSoundEnabled = false;
	
	// image 
	private BufferedImage image;
	private Graphics2D g;
	
	// states
	public static LoadingState load;
	public static MenuState menuState;
	public static AboutState aboutState;
	public static HelpState helpState;
	public static KeyConfigState keyConfigState;
	public static OptionsState optionsState;
	public static Level1State level1_1;
	public static Level2State level1_2;
	public static EndState endgame;
	
	public static AssetLoader al = new AssetLoader();
	
	public SideScroller() {
		super();
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			thread.start();
		}
	}
	
	public static void stop() {
		Game.getConsole().saveAs();
		System.exit(0);
	}

	/**
	 * Called once before the game runs, Initializes objects and assets
	 */
	private void init() {
		game = this;
		Game.startConsole();
		LogHelper.logInfo("Launching SideScroller Game - Build " + VERSION);
		LogHelper.logInfo("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")");
		LogHelper.logInfo("OS Archetecture: " + System.getProperty("os.arch") + " - " + System.getProperty("sun.arch.data.model"));
		LogHelper.logInfo("Java Version: " + System.getProperty("java.version") + " distributed by " + System.getProperty("java.vendor"));
		
		Utilities.checkForUpdates();
		
		LogHelper.logInfo("Loading Java Graphics");
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		LogHelper.logInfo("Creating Window");
		Game.getFrame().setVisible(true);
		
		running = true;
		
		LogHelper.logInfo("Loading Assets");
		Assets.init();
		
		LogHelper.logInfo("Creating Loading Screen");
		load = new LoadingState(this);
		GameState.setState(load);
		render();
		renderToScreen();
		LogHelper.logInfo("Loading Menus");
		menuState = new MenuState(this);
		render();
		renderToScreen();
		LogHelper.logInfo("Menu State Created");
		aboutState = new AboutState(this);
		render();
		renderToScreen();
		helpState = new HelpState(this);
		render();
		renderToScreen();
		LogHelper.logInfo("Help State Created");
		optionsState = new OptionsState(this);
		render();
		renderToScreen();
		LogHelper.logInfo("Options State Created");
		keyConfigState = new KeyConfigState(this);
		render();
		renderToScreen();
		LogHelper.logInfo("Key Config State Created");
		LogHelper.logInfo("Creating Player");
		LevelState.loadLevels();
		render();
		renderToScreen();
		LogHelper.logInfo("Loading Levels");
		level1_1 = new Level1State(this);
		render();
		renderToScreen();
		LogHelper.logInfo("Level 1 Loaded");
		level1_2 = new Level2State(this);
		render();
		renderToScreen();
		LogHelper.logInfo("Level 2 Loaded");
		endgame = new EndState(this);
		render();
		renderToScreen();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		GameState.setState(menuState);
		LogHelper.logInfo("Loading Complete");
		MenuState.bgMusic.play();
	}
	
	/**
	 * Game loop
	 */
	public void run() {
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(running) {
			targetTime = 1000 / FPS;
			start = System.nanoTime();
			
			tick();
			render();
			renderToScreen();
		
			elapsed = System.nanoTime() - start;
		
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			try {
				Thread.sleep(wait);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void tick() {
		if(GameState.getState() != null)
			GameState.getState().tick();
	}
	
	private void render() {
		if(GameState.getState() != null)
			GameState.getState().render(g);
	}
	
	private void renderToScreen() {
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
	}
	
	public static SideScroller getGame() {
		return game;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public void keyPressed(KeyEvent e) {
		if(GameState.getState() != null) 
			GameState.getState().keyPressed(e.getKeyCode());
	}
	
	public void keyReleased(KeyEvent e) {
		if(GameState.getState() != null) 
			GameState.getState().keyReleased(e.getKeyCode());
	}
	
	public void mousePressed(MouseEvent e) {
		if(GameState.getState() != null) 
			GameState.getState().mousePressed(e.getModifiersEx());
	}

	public void mouseReleased(MouseEvent e) {
		if(GameState.getState() != null) 
			GameState.getState().mouseReleased(e.getModifiersEx());
	}
	
	public void keyTyped(KeyEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public String getLocalVersion() {
		return VERSION;
	}
	
	public String getServerVersionURL() {
		return "http://masterzach32.net/sidescroller/updates.txt";
	}

	public String getUpdateURL() {
		return "http://masterzach32.net/sidescroller/";
	}

	public String getDownloadURL() {
		return "http://masterzach32.net/sidescroller/latest.jar";
	}
}