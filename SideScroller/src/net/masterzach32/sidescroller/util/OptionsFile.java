package net.masterzach32.sidescroller.util;

import java.io.*;

import net.masterzach32.sidescroller.gamestate.levels.LevelState;
import net.masterzach32.sidescroller.gamestate.menus.KeyConfigState;
import net.masterzach32.sidescroller.gamestate.menus.OptionsState;
import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/** 
 * OptionsFile contains static methods that use the JSON.simple library
 * to manage writing and reading the game options to a file.
 */
@SuppressWarnings("unused")
public class OptionsFile {

	/** 
	 * OPTIONS_VERSION identifies the version of the options file format.
	 * Increase this value whenever incompatible changes are made to the 
	 * options file format. (Just adding a new JSON field will not
	 * break compatibility).
	 */
	public static final int OPTIONS_VERSION = 1;
	// FIXME: We should use an appropriate path, such as the user's preferences folder
	public static final String OPTIONS_FILENAME = "ssproject_game_options.json";
	
	@SuppressWarnings("unchecked")
	private static String optionsToJSON() {
		JSONObject gameOptions = new JSONObject();
		gameOptions.put("gameVersion", SideScroller.VERSION);
		gameOptions.put("buildType", new String(SideScroller.TYPE));
		gameOptions.put("optionsVersion", new Integer(OPTIONS_VERSION));
		gameOptions.put("ticksPerSec", new Integer(SideScroller.FPS));
		gameOptions.put("enableSound", new Boolean(SideScroller.isSoundEnabled));
		gameOptions.put("enableConsole", new Boolean(OptionsState.isConsoleEnabled()));
		gameOptions.put("enableDebug", new Boolean(OptionsState.isDebugEnabled()));

		JSONObject windowSettings = new JSONObject();
		windowSettings.put("width", new Integer(SideScroller.WIDTH));
		windowSettings.put("height", new Integer(SideScroller.HEIGHT));
		windowSettings.put("scale", new Integer(SideScroller.SCALE));
		windowSettings.put("top", new Integer(SideScroller.TOP));
		windowSettings.put("left", new Integer(SideScroller.LEFT));
		gameOptions.put("windowSettings", windowSettings);

		JSONObject keyBindings = new JSONObject();
		keyBindings.put("left", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_LEFT]));
		keyBindings.put("right", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_RIGHT]));
		keyBindings.put("jump", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_JUMP]));
		keyBindings.put("glide", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_GLIDE]));
		keyBindings.put("conquringSands", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_CONCSANDS]));
		keyBindings.put("arise", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_ARISE]));
		keyBindings.put("shiftingSands", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_SHIFTSANDS]));
		keyBindings.put("emperorsDivide", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_EMPERORSDIVIDE]));
		gameOptions.put("keyBindings", keyBindings);
		
		JSONObject playerStats = new JSONObject();
		playerStats.put("exp", LevelState.getPlayer().getExp());
		playerStats.put("level", LevelState.getPlayer().getLevel());
		gameOptions.put("playerStats", playerStats);
		
		return gameOptions.toString();
	}
	
	/** 
	 * Attempts to retrieve an integer value from obj with the given key/name.
	 * Returns null if the key is not present or has a non-numeric value.
	 * (Floating-point values will be rounded or truncated).
	 */
	private static Integer getInteger(JSONObject obj, String key) {
		Object keyobj = obj.get(key);
		if (keyobj instanceof java.lang.Number) {
			return new Integer(((Number) keyobj).intValue());
		}
		else return null;
	}
	
	/** 
	 * Attempts to retrieve a boolean value from obj with the given key/name.
	 * Returns null if the key is not present or has a non-boolean value.
	 */
	private static Boolean getBoolean(JSONObject obj, String key) {
		Object keyobj = obj.get(key);
		if (keyobj instanceof java.lang.Boolean) {
			return (Boolean) keyobj;
		}
		else return null;
	}
	
	/** 
	 * Attempts to retrieve a String value from obj with the given key/name.
	 * Returns null if the key is not present or has a non-string value.
	 */
	private static String getString(JSONObject obj, String key) {
		Object keyobj = obj.get(key);
		if (keyobj instanceof java.lang.String) {
			return (String) keyobj;
		}
		else return null;
	}
	
	/** 
	 * Attempts to retrieve a JSONObject from obj with the given key/name.
	 * Returns null if the key is not present or it is not a JSONObject.
	 */
	private static JSONObject getJSONObject(JSONObject obj, String key) {
		Object keyobj = obj.get(key);
		if (keyobj instanceof JSONObject) {
			return (JSONObject) keyobj;
		}
		else return null;
	}
	
	private static boolean parseOptionsFromJSON(String json) {
		Object obj = null;
		JSONObject gameOptions, windowSettings, keyBindings;

		// parse the JSON string and get the top-level JSONObject
		try {
			obj = JSONValue.parseWithException(json);
		} catch (ParseException e) {
			
			LogHelper.logger.logError("Error while parsing game options file: " + e.toString());
			return false;
		}
		if (!(obj instanceof JSONObject)) {
			// give up!
			LogHelper.logger.logError("Options file does not begin with a JSON Object");
			return false;
		}
		gameOptions = (JSONObject)obj;

		// Check the options file version
		Integer version = getInteger(gameOptions, "optionsVersion");
		if (version == null) {
			LogHelper.logger.logWarning("Attempting to read options file without a value for 'optionsVersion'");
		}
		else if (version > OPTIONS_VERSION) {
			// a higher version number indicates an incompatible file that
			// this version of the game does not know how to read
			LogHelper.logger.logWarning("Could not read options file from a newer version of the game: " + gameOptions.get("gameVersion"));
			return false;
		}

		// Check for each option and update if present
		Integer i; Boolean b;

		windowSettings = getJSONObject(gameOptions, "windowSettings");
		if (windowSettings != null) {
			i = getInteger(windowSettings, "width");
			if (i != null) SideScroller.WIDTH = i;
			i = getInteger(windowSettings, "height");
			if (i != null) SideScroller.HEIGHT = i;
			i = getInteger(windowSettings, "scale");
			if (i != null) SideScroller.SCALE = i;
			i = getInteger(windowSettings, "top");
			if (i != null) SideScroller.TOP = i;
			i = getInteger(windowSettings, "left");
			if (i != null) SideScroller.LEFT = i;
			Game.resizeGameFrame(true);
		}

		keyBindings = getJSONObject(gameOptions, "keyBindings");
		if (windowSettings != null) {
			i = getInteger(keyBindings, "left");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_LEFT] = i;
			i = getInteger(keyBindings, "right");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_RIGHT] = i;
			i = getInteger(keyBindings, "jump");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_JUMP] = i;
			i = getInteger(keyBindings, "glide");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_GLIDE] = i;
			i = getInteger(keyBindings, "conquringSands");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_CONCSANDS] = i;
			i = getInteger(keyBindings, "arise");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_ARISE] = i;
			i = getInteger(keyBindings, "shiftingSands");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_SHIFTSANDS] = i;
			i = getInteger(keyBindings, "emperorsDivide");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_EMPERORSDIVIDE] = i;
		}

		i = getInteger(gameOptions, "ticksPerSec");
		if (i != null) SideScroller.FPS = i;
		b = getBoolean(gameOptions, "enableSound");
		if (b != null) SideScroller.isSoundEnabled = b;
		b = getBoolean(gameOptions, "enableConsole");
		if (b != null) OptionsState.setConsole(b);
		b = getBoolean(gameOptions, "enableDebug");
		if (b != null) OptionsState.setDebug(b);

		return true;
	}
	
	private static String getOptionsPath() {
		return OSUtils.getHomeDirectory(OPTIONS_FILENAME);
	}
	
	public static boolean save() {
		BufferedWriter fout = null;
		String path = getOptionsPath();
		
		LogHelper.logger.logInfo("Saving game options");
		try {
			// File optionsFile = new File(path);
			fout = new BufferedWriter(new FileWriter(path));
			fout.write(optionsToJSON());
		} catch (IOException e) {
			LogHelper.logger.logError("Problem writing " + path);
			e.printStackTrace();
			return false;
		} finally {
			Utilities.closeStream(fout);
		}

		return true;
	}

	/** 
	 * Thanks to StackOverflow user barjak for the example of reading an entire file to a String at
	 * <http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file>
	 */
	public static boolean load() {
		RandomAccessFile fin = null;
		String path = getOptionsPath();
		byte[] buffer;
		
		LogHelper.logger.logInfo("Loading game options");
		try {
			// File optionsFile = new File(path);
			fin = new RandomAccessFile(path, "r");		// "r" = open file for reading only
			buffer = new byte[(int) fin.length()];
			fin.readFully(buffer);
		} catch (FileNotFoundException e) {
			// ignore missing options file
			return false;
		} catch (IOException e) {
			LogHelper.logger.logError("Problem reading " + path);
			e.printStackTrace();
			return false;
		} finally {
			Utilities.closeStream(fin);
		}
		
		String json = new String(buffer);
		LogHelper.logger.logInfo(json);
		return parseOptionsFromJSON(json);
	}
}