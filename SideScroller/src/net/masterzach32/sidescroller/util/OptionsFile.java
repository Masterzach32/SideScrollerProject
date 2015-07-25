package net.masterzach32.sidescroller.util;

import java.io.*;

import net.masterzach32.sidescroller.gamestate.menus.KeyConfigState;
import net.masterzach32.sidescroller.gamestate.menus.OptionsState;
import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/** OptionsFile contains static methods that use the JSON.simple library
 *  to manage writing and reading the game options to a file.
 */
public class OptionsFile {

	/** OPTIONS_VERSION identifies the version of the options file format.
	 *  Increase this value whenever incompatible changes are made to the 
	 *  options file format.  (Just adding a new JSON field will not
	 *  break compatibility).
	 */
	public static final int OPTIONS_VERSION = 1;
	public static final String OPTIONS_FILENAME = "SideScroller_Options.json";
	
	@SuppressWarnings("unchecked")
	private static String optionsToJSON() {
		JSONObject gameOptions = new JSONObject();
		gameOptions.put("gameVersion", SideScroller.VERSION);
		gameOptions.put("optionsVersion", new Integer(OPTIONS_VERSION));

		JSONObject windowSettings = new JSONObject();
		windowSettings.put("width", new Integer(SideScroller.WIDTH));
		windowSettings.put("height", new Integer(SideScroller.HEIGHT));
		windowSettings.put("scale", new Integer(SideScroller.SCALE));
		windowSettings.put("top", new Integer(0)); // FIXME: save window position as "top" and "left"
		windowSettings.put("left", new Integer(0));
		gameOptions.put("windowSettings", windowSettings);

		JSONObject keyBindings = new JSONObject();
		keyBindings.put("left", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_LEFT]));
		keyBindings.put("right", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_RIGHT]));
		keyBindings.put("jump", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_JUMP]));
		keyBindings.put("glide", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_GLIDE]));
		keyBindings.put("scratch", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_SCRATCH]));
		keyBindings.put("orb", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_ORB]));
		keyBindings.put("rewind", new Integer(KeyConfigState.keyBinding[KeyConfigState.KEY_REWIND]));
		gameOptions.put("keyBindings", keyBindings);

		gameOptions.put("ticksPerSec", new Integer(SideScroller.FPS));
		gameOptions.put("enableSound", new Boolean(SideScroller.isSoundEnabled));
		gameOptions.put("enableConsole", new Boolean(OptionsState.isConsoleEnabled()));
		gameOptions.put("enableDebug", new Boolean(OptionsState.isDebugEnabled()));
		
		// player exp and progression should be saved here later
		
		return gameOptions.toString();
	}
	
	/** Attempts to retrieve an integer value from obj with the given key/name.
	 *  Returns null if the key is not present or has a non-numeric value.
	 *  (Floating-point values will be rounded or truncated).
	 */
	private static Integer getInteger(JSONObject obj, String key) {
		Object keyobj = obj.get(key);
		if (keyobj instanceof java.lang.Number) {
			return new Integer(((Number)keyobj).intValue());
		}
		else return null;
	}
	
	/** Attempts to retrieve a boolean value from obj with the given key/name.
	 *  Returns null if the key is not present or has a non-boolean value.
	 */
	private static Boolean getBoolean(JSONObject obj, String key) {
		Object keyobj = obj.get(key);
		if (keyobj instanceof java.lang.Boolean) {
			return (Boolean)keyobj;
		}
		else return null;
	}
	
	/** Attempts to retrieve a String value from obj with the given key/name.
	 *  Returns null if the key is not present or has a non-string value.
	 */
	@SuppressWarnings("unused")
	private static String getString(JSONObject obj, String key) {
		Object keyobj = obj.get(key);
		if (keyobj instanceof java.lang.String) {
			return (String)keyobj;
		}
		else return null;
	}
	
	/** Attempts to retrieve a JSONObject from obj with the given key/name.
	 *  Returns null if the key is not present or it is not a JSONObject.
	 */
	private static JSONObject getJSONObject(JSONObject obj, String key) {
		Object keyobj = obj.get(key);
		if (keyobj instanceof JSONObject) {
			return (JSONObject)keyobj;
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
			LogHelper.logError("Error while parsing game options file: " + e.toString());
			return false;
		}
		if (! (obj instanceof JSONObject)) {
			// give up!
			LogHelper.logError("Options file does not begin with a JSON Object");
			return false;
		}
		gameOptions = (JSONObject)obj;

		// Check the options file version
		Integer version = getInteger(gameOptions, "optionsVersion");
		if (version == null) {
			LogHelper.logWarning("Attempting to read options file without a value for 'optionsVersion'");
		}
		else if (version > OPTIONS_VERSION) {
			// a higher version number indicates an incompatible file that
			// this version of the game does not know how to read
			LogHelper.logWarning("Could not read options file from a newer version of the game: " + gameOptions.get("gameVersion"));
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
			// FIXME: read & restore window position
			// i = getInteger(windowSettings, "top");
			// if (i != null) ??? = i;
			// i = getInteger(windowSettings, "left");
			// if (i != null) ??? = i;
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
			i = getInteger(keyBindings, "scratch");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_SCRATCH] = i;
			i = getInteger(keyBindings, "orb");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_ORB] = i;
			i = getInteger(keyBindings, "rewind");
			if (i != null) KeyConfigState.keyBinding[KeyConfigState.KEY_REWIND] = i;
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
		// FIXME: We should use an appropriate path, such as the user's preferences folder
		return OPTIONS_FILENAME;
	}
	
	public static boolean save() {
		BufferedWriter fout = null;
		String path = getOptionsPath();
		
		LogHelper.logInfo("Saving game options");
		try {
			// File optionsFile = new File(path);
			fout = new BufferedWriter(new FileWriter(path));
			fout.write(optionsToJSON());
		} catch (IOException e) {
			LogHelper.logError("Problem writing " + path);
			e.printStackTrace();
			return false;
		}
		finally {
			Utilities.closeStream(fout);
		}

		return true;
	}

	/* Thanks to StackOverflow user barjak for the example of reading an entire file to a String at
	 * <http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file>
	 */
	public static boolean load() {
		RandomAccessFile fin = null;
		String path = getOptionsPath();
		byte[] buffer;
		
		LogHelper.logInfo("Loading game options");
		try {
			// File optionsFile = new File(path);
			fin = new RandomAccessFile(path, "r");		// "r" = open file for reading only
			buffer = new byte[(int) fin.length()];
			fin.readFully(buffer);
		} catch (FileNotFoundException e) {
			// ignore missing options file
			return false;
		} catch (IOException e) {
			LogHelper.logError("Problem reading " + path);
			e.printStackTrace();
			return false;
		}
		finally {
			Utilities.closeStream(fin);
		}
		
		String json = new String(buffer);
		// LogHelper.logInfo(json);
		return parseOptionsFromJSON(json);
	}
}
