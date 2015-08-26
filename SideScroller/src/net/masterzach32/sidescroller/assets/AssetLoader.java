package net.masterzach32.sidescroller.assets;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.masterzach32.sidescroller.gamestate.menus.LoadingState;
import net.masterzach32.sidescroller.util.LogHelper;
import net.masterzach32.sidescroller.util.Utilities;

public class AssetLoader {

	private String s = "[ASSETS] ";
	
	/**
	 * Loads an image from the assets folder
	 * @param path
	 * @return {@link BufferedImage}
	 */
	public BufferedImage loadImage(String path) {
		LoadingState.setInfo("Loading Asset: " + path, 20);
		BufferedImage bi;
		try {
			URL imageLocation = getClass().getResource(path);
			bi = ImageIO.read(imageLocation);
			LogHelper.logInfo(s + "Loaded Image: " + path);
			return bi;
		} catch (Exception e) {
			LogHelper.logWarning(s + "Missing Image: " + path + ".");
			Utilities.createErrorDialog("Missing Image Asset!", "SideScroller Project can't seem to find this asset: " + path, e);
			return null;
		}
	}
	
	/**
	 * Loads the given audio file
	 * @param path
	 * @return ais
	 */
	public AudioInputStream loadAudio(String path) {
		LoadingState.setInfo("Loading Asset: " + path, 40);
		AudioInputStream ais;
		try {
			ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
			LogHelper.logInfo(s + "Loaded Audio File: " + path);
			return ais;
		} catch (UnsupportedAudioFileException e) {
			LogHelper.logError(s + "Unsupported Audio File: " + path);
			e.printStackTrace();
			Utilities.createErrorDialog("Unsupported Audio File!", "SideScroller Project can't use this asset: " + path, e);
			return null;
		} catch (Exception e) {
			LogHelper.logWarning(s + "Missing Audio File: " + path);
			Utilities.createErrorDialog("Missing Audio Asset!", "SideScroller Project can't seem to find this asset: " + path, e);
			return null;
		}
	}
	
	public String loadMap(String path) {
		LoadingState.setInfo("Loading Asset: " + path, 60);
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
