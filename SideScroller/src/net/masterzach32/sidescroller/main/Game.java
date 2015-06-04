package net.masterzach32.sidescroller.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import net.masterzach32.sidescroller.util.Console;

public class Game {
	
	private static JFrame window;
	private static Console console;
	
	public static void main(String[] args) {
		console = new Console();
		window = new JFrame("SideScrollerRPG " + SideScroller.VERSION);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int) (SideScroller.WIDTH * SideScroller.SCALE), (int) (SideScroller.HEIGHT * SideScroller.SCALE + 20));
		int x = (int) ((screenSize.width/2)-(frameSize.width/2));
		int y = (int) ((screenSize.height/2)-(frameSize.height/2));
		window.setPreferredSize(frameSize);
		window.setLocation(x, y);
		window.setContentPane(new SideScroller());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();	
		console.setVisible(true);
		window.setVisible(true);
	}
	
	public static JFrame getFrame() {
		return window;
	}
	
	public static Console getConsole() {
		return console;
	}
}
