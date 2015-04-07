package net.masterzach32.sidescroller.assets.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.masterzach32.sidescroller.util.LogHelper;

public class ImageLoader {
	
	private String s = "[ASSETS] ";

	/**
	 * Loads an image from the assets folder
	 * @param path
	 * @return {@link BufferedImage}
	 */
	public BufferedImage load(String path) {
		BufferedImage bi;
		try {
			URL imageLocation = getClass().getResource(path);
			if(imageLocation != null) {
				bi = ImageIO.read(imageLocation);
				LogHelper.logInfo(s + "Loaded Asset: " + path);
				return bi;
			} else {
				LogHelper.logWarning(s + "Missing image: " + path);
				return null;
			}
		} catch (IOException e) {
			LogHelper.logError(s + "Missing image: " + path);
			e.printStackTrace();
			return null;
		}
	}
}
