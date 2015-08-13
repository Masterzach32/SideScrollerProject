package net.masterzach32.sidescroller.api;

import java.awt.Component;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import javax.swing.JOptionPane;

import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;
import net.masterzach32.sidescroller.util.LogHelper;
import net.masterzach32.sidescroller.util.Utilities;

/**
 * Game Updater, v0.1
 *
 */
public interface IUpdatable {

	public String getLocalVersion();
	
	public String getServerVersionURL();
	
	public String getUpdateURL();
	
	public String getDownloadURL();
	
	/**
	 * Checks to see if their is a newer version of the game available, and provides the link to download it in the console.<br>
	 * This method does not return if the user runs the updated build
	 * @return true if update succeded, false otherwise
	 */
	public static boolean checkForUpdates() {
		if(!SideScroller.isUpdateEnabled) {
			LogHelper.logInfo("Updates are disabled. This is probably because you are running a beta or nightly build.");
			return false;
		}
		LogHelper.logInfo("Checking for updates");
		Path p = FileSystems.getDefault().getPath("latest.txt");
		String[] s = Utilities.readTextFile(SideScroller.getGame().getServerVersionURL(), p.toString(), false);
		
		if(s == null || s[0] == null) {
			LogHelper.logInfo("Error while checking for updates: Could not read server update file.");
		} else if(!s[0].equals(SideScroller.getGame().getLocalVersion())) {
			LogHelper.logInfo("An update is available, you have build " + SideScroller.getGame().getLocalVersion() + ", Server build is " + s[0]);
			LogHelper.logInfo("You can download the update here: " + SideScroller.getGame().getUpdateURL());
			LogHelper.logInfo("NOTE: If you are testing a beta version of the game and it prompts you to update, ignore it.");
			
			int result = JOptionPane.showConfirmDialog(Game.getFrame(), (Object) "An newer version of the game, (Build " + s[0] +") is avaliable, do you want to download it now? ", "Update Available - Build " + s[0], JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			try {
			    Files.delete(p);
			} catch (NoSuchFileException e) {
				LogHelper.logError("Could not find updates file.");
				e.printStackTrace();
			} catch (DirectoryNotEmptyException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			if(result == JOptionPane.YES_OPTION) {
				String path = Utilities.saveAs(".jar");
				Utilities.download(SideScroller.getGame().getDownloadURL() + s[0] + ".jar", path, "Downloading Update", false);
				if(!Utilities.error) {
					int result2 = JOptionPane.showConfirmDialog(Game.getFrame(), (Object) "Download complete. Do you want to close this instance and run the new build?", "Update Complete", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if(result2 == JOptionPane.YES_OPTION) {
						try {
							ProcessBuilder pb = new ProcessBuilder("java", "-jar", path);
							pb.start();
							System.exit(0);
							return true;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else if(result2 == JOptionPane.NO_OPTION) return true;
				}
			} else {
				return false;
			}
		} else {
			LogHelper.logInfo("No update is available");
		}
		return false;
	}	
}