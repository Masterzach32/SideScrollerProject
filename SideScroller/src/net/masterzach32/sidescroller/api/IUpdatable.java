package net.masterzach32.sidescroller.api;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.util.LogHelper;
import net.masterzach32.sidescroller.util.OSUtils;
import net.masterzach32.sidescroller.util.Utilities;

/**
 * Game Updater, v0.1
 */
public interface IUpdatable {

	public String getLocalVersion();
	
	public String getServerVersionURL();
	
	public String getUpdateURL();
	
	public String getDownloadURL();
	
	/**
	 * Checks to see if their is a newer version of the game available, and downloads it to the users home directory.<br>
	 * This method does not return if the user runs the updated build
	 * @return true if update succeded, false otherwise
	 */
	public static boolean checkForUpdates() {
		if(!SideScroller.isUpdateEnabled) {
			LogHelper.logger.logInfo("Updates are disabled. This is probably because you are running a beta or nightly build.");
			return false;
		}
		LogHelper.logger.logInfo("Checking for updates");
		Path p = Paths.get(OSUtils.getHomeDirectory("latest.txt"));
		String[] s = Utilities.readTextFile(SideScroller.game.getServerVersionURL(), p, false);
		
		if(s == null || s[0] == null) {
			LogHelper.logger.logInfo("Error while checking for updates: Could not read server update file.");
		} else if(!s[0].equals(SideScroller.game.getLocalVersion())) {
			LogHelper.logger.logInfo("An update is available, you have build " + SideScroller.game.getLocalVersion() + ", Server build is " + s[0]);
			LogHelper.logger.logInfo("You can download the update here: " + SideScroller.game.getUpdateURL());
			LogHelper.logger.logInfo("NOTE: If you are testing a beta version of the game and it prompts you to update, ignore it.");
			
			int result = JOptionPane.showConfirmDialog(Game.getFrame(), (Object) "An update is available!\nLocal Build: " + SideScroller.game.getLocalVersion() + " Server Build: " + s[0] + "\nDo you want to update now?", "Update Available - Build " + s[0], JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			try {
			    Files.delete(p);
			} catch (NoSuchFileException e) {
				e.printStackTrace();
			} catch (DirectoryNotEmptyException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(result == JOptionPane.YES_OPTION) {
				Path path = Paths.get(OSUtils.getHomeDirectory("SideScroller_" + s[0] + ".jar"));
				Utilities.download(SideScroller.game.getDownloadURL() + s[0] + ".jar", path.toString(), "Downloading Update", false);
				int result2 = JOptionPane.showConfirmDialog(Game.getFrame(), (Object) "Update complete! Downloaded to:\n" + path.toString() + ".\nDo you want to close this instance and run the new build?", "Update Complete", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if(result2 == JOptionPane.YES_OPTION) {
					try {
						ProcessBuilder pb = new ProcessBuilder("java", "-jar", path.toString());
						pb.start();
						System.exit(0);
						return true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if(result2 == JOptionPane.NO_OPTION) return true;
			} else {
				return false;
			}
		} else {
			LogHelper.logger.logInfo("No update is available");
		}
		return false;
	}	
}