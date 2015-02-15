package net.masterzach32.sidescroller.main;

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame("SideScrollerRPG " + SideScroller.VERSION);
		window.setContentPane(new SideScroller());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
	}
	
}
