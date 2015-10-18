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

/**
 * The SideScroller Project is an open source solution for people wanting to create an open source SideScrolling / RPG game.<br>
 * If you want to use my code, your code must be open source too. Thanks!<br>
 *
 * Remember this is an in-development game. Expect things to be hilariously over-powered or under-powered.
 * 
 * @author Zachary Kozar
 * @version 0.1.6 Beta
 */
public class SideScroller implements Runnable, KeyListener, MouseListener, IUpdatable {
	
	// dimensions
	public static int WIDTH = 640;
	public static int HEIGHT = 360;
	public static int TOP = 0;
	public static int LEFT = 0;
	public static int SCALE = 2;
	public static final String TYPE = "Pre-Release", VERSION = "0.1.6.286";
	public static final boolean isUpdateEnabled = true;
	public static boolean isSoundEnabled = true;
	
	// game thread
	private Thread tickAndRender;
	private JPanel panel;
	public static SideScroller game;
	private boolean running;
	public static int FPS = 60;
	private long targetTime = 1000 / FPS;
	
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
		panel = new JPanel();
		panel.setFocusable(true);
		panel.requestFocus();
		if(tickAndRender == null) {
			tickAndRender = new Thread(this);
			panel.addKeyListener(this);
			panel.addMouseListener(this);
			tickAndRender.start();
		}
	}
	
	public synchronized void stop() {
		OptionsFile.save();
		System.exit(0);
	}

	/**
	 * Called once before the game runs, Initializes objects and assets
	 */
	private void init() {
		game = this;
		Game.startConsole();
		LogHelper.logger.logInfo("Launching SideScroller Project - " + TYPE + " Build " + VERSION);
		LogHelper.logger.logInfo("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")");
		LogHelper.logger.logInfo("OS Archetecture: " + System.getProperty("os.arch") + " - " + System.getProperty("sun.arch.data.model"));
		LogHelper.logger.logInfo("Java Version: " + System.getProperty("java.version") + " distributed by " + System.getProperty("java.vendor"));
		
		LogHelper.logger.logInfo("Loading Java Graphics ");
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		LogHelper.logger.logInfo("Starting pre-initialzation");
		Assets.preinit();
		
		Game.getConsole().getFrame().setIconImage(Assets.getImageAsset("icon_console"));
		
		LogHelper.logger.logInfo("Creating Window");
		Game.getFrame().setVisible(true);
		
		LogHelper.logger.logInfo("Creating Loading Screen");
		load = new LoadingState(this);
		GameState.setState(load);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LoadingState.setInfo("Checking for Updates...", 0);
		
		IUpdatable.checkForUpdates();
		
		running = true;
		
		LoadingState.setInfo("Loading Assets...", 20);
		
		LogHelper.logger.logInfo("Loading Assets");
		Assets.init();
		LoadingState.setInfo("Loading GameStates...", 60);
		
		LogHelper.logger.logInfo("Loading Menus");
		menuState = new MenuState(this);
		LogHelper.logger.logInfo("Menu State Created");
		aboutState = new AboutState(this);
		helpState = new HelpState(this);
		LogHelper.logger.logInfo("Help State Created");
		optionsState = new OptionsState(this);
		LogHelper.logger.logInfo("Options State Created");
		keyConfigState = new KeyConfigState(this);
		LogHelper.logger.logInfo("Key Config State Created");
		LoadingState.setInfo("Loading Player...", 70);
		
		LogHelper.logger.logInfo("Creating Player");
		LevelState.loadLevels();
		LoadingState.setInfo("Loading Levels...", 80);
		
		LogHelper.logger.logInfo("Loading Levels");
		level1_1 = new Level1State(this);
		LoadingState.setInfo("Loading Levels...", 90);
		
		LogHelper.logger.logInfo("Level 1 Loaded");
		level1_2 = new Level2State(this);
		
		LogHelper.logger.logInfo("Level 2 Loaded");
		LoadingState.setInfo("Finishing Up...", 100);
		endgame = new EndState(this);
		
		OptionsFile.load();
		GameState.setState(menuState);
		LogHelper.logger.logInfo("Loading Complete");
		MenuState.bgMusic.play();
	}
	
	/**
	 * Game loop
	 */
	public void run() {
		try{
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

				elapsed = System.nanoTime() - start;

				wait = targetTime - elapsed / 1000000;
				if(wait < 0) wait = 5;
				Thread.sleep(wait);
			}
		}
		catch(Exception e) {
			Utilities.createErrorDialog("Error", "An unexpected error occured: " + e.toString(), e);
		}
	}
	
	public void tick() {
		if(GameState.getState() != null)
			GameState.getState().tick();
	}
	
	public void render() {
		if(GameState.getState() != null)
			GameState.getState().render(g);
		Font f = new Font("Arial", Font.PLAIN, 9);
		g.setFont(f);
		FontMetrics fontMetrics = g.getFontMetrics();
		g.setColor(Color.LIGHT_GRAY);
		String s = new String("SideScroller Project " + TYPE + " Build " + VERSION);
		g.drawString(s, WIDTH - fontMetrics.stringWidth(s) - 3, 8);
		g.setColor(Color.WHITE);
		renderToScreen();
	}
	
	public void renderToScreen() {
		Graphics g = panel.getGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
	}
	
	public Thread getThread() {
		return tickAndRender;
	}
	
	public JPanel getPane() {
		return panel;
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
		return "http://masterzach32.net/sidescroller/SideScroller_";
	}
}