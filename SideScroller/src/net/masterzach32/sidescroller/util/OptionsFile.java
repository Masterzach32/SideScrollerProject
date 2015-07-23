package net.masterzach32.sidescroller.util;

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

	// main() function for testing this class
	public static void main(String[] args) {
		System.out.print(optionsToJSON());
	}
}
