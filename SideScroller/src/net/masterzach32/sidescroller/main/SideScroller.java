package net.masterzach32.sidescroller.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import net.masterzach32.sidescroller.assets.Assets;
import net.masterzach32.sidescroller.assets.gfx.ImageLoader;
import net.masterzach32.sidescroller.assets.sfx.AudioLoader;
import net.masterzach32.sidescroller.gamestate.*;
import net.masterzach32.sidescroller.util.*;

@SuppressWarnings("serial")
public class SideScroller extends JPanel implements Runnable, KeyListener, MouseListener {
	
	// dimensions
	public static int WIDTH = 640;
	public static int HEIGHT = 360;
	public static int SCALE = 1;
	public static final String VERSION = "0.0.2.090";
	
	// game thread
	private Thread thread;
	private boolean running;
	public static int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	private boolean mouseOnScreen = false;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// states
	public static MenuState menuState;
	public static HelpState helpState;
	public static OptionsState optionsState;
	public static Level1State level1;
	public static Level2State level2;
	public static EndState endgame;
	
	public static ImageLoader im = new ImageLoader();
	public static AudioLoader am = new AudioLoader();
	
	public SideScroller() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
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

	/**
	 * Called once before the game runs, Initializes objects and assets
	 */
	private void init() {
		LogHelper.logInfo("Launching SideScroller Game - Version " + VERSION);
		LogHelper.logInfo("Date: " + Utilities.getTime());
		LogHelper.logInfo("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
		LogHelper.logInfo("OS Archetecture: " + System.getProperty("os.arch"));
		LogHelper.logInfo("Java Version: " + System.getProperty("java.version"));
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		
		Assets.init(); 
		
		menuState = new MenuState(this);
		helpState = new HelpState(this);
		optionsState = new OptionsState(this);
		level1 = new Level1State(this);
		level2 = new Level2State(this);
		endgame = new EndState(this);
		GameState.setState(menuState);
		
		LogHelper.logInfo("Loading Complete!");
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
			start = System.nanoTime();
			
			tick();
			render();
			renderToScreen();
			if(mouseOnScreen) renderMouse();
		
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
	
	private void renderMouse() {
		Point p = new Point(0, 0); //e.getPoint();
		int x = p.x;
		int y = p.y;
		g.drawImage(Assets.getImageAsset("mouse"), x, y, null);
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

	public void mouseEntered(MouseEvent e) {
		mouseOnScreen = true;
	}

	public void mouseExited(MouseEvent e) {
		mouseOnScreen = false;
	}	
}