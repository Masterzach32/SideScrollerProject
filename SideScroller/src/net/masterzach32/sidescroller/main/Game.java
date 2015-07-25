package net.masterzach32.sidescroller.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.masterzach32.sidescroller.util.Console;
import net.masterzach32.sidescroller.util.LogHelper;

public class Game {
	
	private static JFrame window;
	private static Console console;
	
	public static void main(String[] args) {
		System.out.println("Launching SideScroller Project - Build " + SideScroller.VERSION);
		System.out.println("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")");
		System.out.println("OS Archetecture: " + System.getProperty("os.arch"));
		System.out.println("Java Version: " + System.getProperty("java.version"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		window = new JFrame("SideScrollerRPG " + SideScroller.VERSION);
		resizeGameFrame(false);
		window.setContentPane(new SideScroller());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
	}
	
	public static void resizeGameFrame(boolean forceResize) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int) (SideScroller.WIDTH * SideScroller.SCALE), (int) (SideScroller.HEIGHT * SideScroller.SCALE + 20));
		int x = (int) ((screenSize.width/2)-(frameSize.width/2));
		int y = (int) ((screenSize.height/2)-(frameSize.height/2));
		if (forceResize) window.setSize(frameSize);
		else window.setPreferredSize(frameSize);
		window.setLocation(x, y);
	}

	public static JFrame getFrame() {
		return window;
	}
	
	public static Console getConsole() {
		return console;
	}
	
	public static void startConsole() {
		System.out.println("Rerouting Console");
		console = new Console();
		console.setVisible(true);
		LogHelper.logInfo("STDOUT and STDERR rerouted sucesfully");
	}
}
