package net.masterzach32.sidescroller.util;

import java.io.*;

import net.masterzach32.sidescroller.gamestate.menus.KeyConfigState;
import net.masterzach32.sidescroller.gamestate.menus.OptionsState;
import net.masterzach32.sidescroller.main.SideScroller;

import org.json.simple.JSONObject;

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
	public static final String OPTIONS_FILENAME = "SideScroller_options.json";
	
	@SuppressWarnings("unchecked")
	private static String optionsToJSON() {
		JSONObject gameOptions = new JSONObject();
		gameOptions.put("gameVersion", SideScroller.VERSION);
		gameOptions.put("optionsVersion", new Integer(OPTIONS_VERSION));

		JSONObject windowSettings = new JSONObject();
		windowSettings.put("width", new Integer(SideScroller.WIDTH));
		windowSettings.put("height", new Integer(SideScroller.HEIGHT));
		windowSettings.put("scale", new Integer(SideScroller.SCALE));
		windowSettings.put("top", new Integer(0));
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
		
		return gameOptions.toString();
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
			// FIXME: ignore missing options file
			LogHelper.logError("Could not find options file: " + path);
			return false;
		} catch (IOException e) {
			LogHelper.logError("Problem reading " + path);
			e.printStackTrace();
			return false;
		}
		finally {
			Utilities.closeStream(fin);
		}

		LogHelper.logInfo(new String(buffer));
		return true;
	}

	// main() function for testing this class
	public static void main(String[] args) {
		System.out.print(optionsToJSON());
	}
}
