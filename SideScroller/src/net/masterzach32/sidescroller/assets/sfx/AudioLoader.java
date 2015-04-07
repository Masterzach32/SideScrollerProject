package net.masterzach32.sidescroller.assets.sfx;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.masterzach32.sidescroller.util.LogHelper;

public class AudioLoader {
	
	private String s = "[ASSETS] ";

	/**
	 * Loads the given audio file
	 * @param path
	 * @return ais
	 */
	public AudioInputStream load(String path) {
		AudioInputStream ais;
		try {
			URL imageLocation = getClass().getResource(path);
			if(imageLocation != null) {
				ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
				LogHelper.logInfo(s + "Loaded Asset: " + path);
				return ais;
			} else {
				LogHelper.logWarning(s + "Missing Audio File: " + path);
				return null;
			}
		} catch (UnsupportedAudioFileException e) {
			LogHelper.logError(s + "Unsupported Audio File: " + path);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			LogHelper.logError(s + "Missing Audio File: " + path);
			e.printStackTrace();
			return null;
		}
	}
}