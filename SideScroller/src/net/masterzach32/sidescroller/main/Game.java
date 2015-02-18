package net.masterzach32.sidescroller.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Game {
	
	private static JFrame window;
	
	public static void main(String[] args) {
		window = new JFrame("SideScrollerRPG " + SideScroller.VERSION);
		window.setContentPane(new SideScroller());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(dim.width/2-(window.getSize().width * SideScroller.SCALE)/2, dim.height/2-(window.getSize().height * SideScroller.SCALE)/2);
		window.setVisible(true);		
	}
	
	public static JFrame getFrame() {
		return window;
	}
	
}
