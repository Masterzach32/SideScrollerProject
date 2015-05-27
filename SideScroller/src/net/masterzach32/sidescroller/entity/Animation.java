package net.masterzach32.sidescroller.entity;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	private boolean playedOnce;
	
	public Animation() {
		playedOnce = false;
	}
	
	/**
	 * Sets the amount of frames there are in the animation
	 * @param frames
	 */
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	/**
	 * Sets the delay between each frame, set to -1 for no delay.
	 * @param d
	 */
	public void setDelay(long d) { 
		delay = d; 
	}
	
	/**
	 * Sets the current frame to i
	 * @param i
	 */
	public void setFrame(int i) { 
		currentFrame = i; 
	}
	
	public void tick() {
		if(delay == -1) return;
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
	}
	
	/**
	 * Returns the current animation frame
	 * @return currentFrame
	 */
	public int getFrame() { 
		return currentFrame; 
	}
	
	/**
	 * Returns the image of the current frame
	 * @return currentFrame
	 */
	public BufferedImage getImage() {
		return frames[currentFrame]; 
	}
	
	/**
	 * Checks to see if the animation has finished
	 * @return
	 */
	public boolean hasPlayedOnce() { 
		return playedOnce; 
	}
	
	public void setPlayedOnce(boolean b) {
		playedOnce = b;
	}
}