package net.masterzach32.sidescroller.tilemap;

import java.awt.*;
import java.awt.image.*;

import net.masterzach32.sidescroller.main.SideScroller;

public class Background {
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(BufferedImage i, double ms) {
		try {
			image = i;
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the backgrounds position on the screen
	 * @param x
	 * @param y
	 */
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % SideScroller.WIDTH;
		this.y = (y * moveScale) % SideScroller.HEIGHT;
	}
	
	/**
	 * Sets the speed and direction that the background moves
	 * @param dx
	 * @param dy
	 */
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x += dx;
		y += dy;
	}
	
	public void render(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, SideScroller.WIDTH, SideScroller.HEIGHT,  null);
		
		if(x < 0) {
			g.drawImage(image, (int)x + SideScroller.WIDTH, (int)y, SideScroller.WIDTH, SideScroller.HEIGHT, null);
		}
		if(x > 0) {
			g.drawImage(image, (int)x - SideScroller.WIDTH, (int)y, SideScroller.WIDTH, SideScroller.HEIGHT, null);
		}
	}
}