package net.masterzach32.sidescroller.assets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.masterzach32.sidescroller.util.LogHelper;

public class AssetLoader {

	private String s = "[ASSETS] ";
	
	/**
	 * Loads an image from the assets folder
	 * @param path
	 * @return {@link BufferedImage}
	 */
	public BufferedImage loadImage(String path) {
		BufferedImage bi;
		try {
			URL imageLocation = getClass().getResource(path);
			if(imageLocation != null) {
				bi = ImageIO.read(imageLocation);
				LogHelper.logInfo(s + "Loaded Image: " + path);
				return bi;
			}
		} catch (IOException e) {
			LogHelper.logWarning(s + "Missing Image: " + path + ". Will attempt to download.");
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * Loads the given audio file
	 * @param path
	 * @return ais
	 */
	public AudioInputStream loadAudio(String path) {
		AudioInputStream ais;
		try {
			URL imageLocation = getClass().getResource(path);
			if(imageLocation != null) {
				ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
				LogHelper.logInfo(s + "Loaded Audio File: " + path);
				return ais;
			}
		} catch (UnsupportedAudioFileException e) {
			LogHelper.logError(s + "Unsupported Audio File: " + path);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			LogHelper.logWarning(s + "Missing Audio File: " + path);
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public String loadMap(String path) {
		URL imageLocation = getClass().getResource(path);
		if(imageLocation != null) {
			LogHelper.logInfo(s + "Loaded Map: " + path);
			return path;
		} else {
			LogHelper.logWarning(s + "Missing Asset: " + path);
			return null;
		}
	}
}