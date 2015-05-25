package net.masterzach32.sidescroller.assets;

import java.net.URL;

import net.masterzach32.sidescroller.util.LogHelper;

public class MapLoader {
	
	private String s = "[ASSETS] ";
	
	public String load(String path) {
		URL imageLocation = getClass().getResource(path);
		if(imageLocation != null) {
			LogHelper.logInfo(s + "Loaded Map: " + path);
			return path;
		} else {
			LogHelper.logWarning(s + "Missing Asset: " + path + ". Will attempt to download.");
			Assets.grabMissingAsset(path);
			return null;
		}
	}
}
