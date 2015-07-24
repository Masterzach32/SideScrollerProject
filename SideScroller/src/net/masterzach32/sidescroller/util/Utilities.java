package net.masterzach32.sidescroller.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;

public class Utilities {
	
	private static boolean error = false;

	/**
	 * Gets the time and returns it in hh:mm:ss
	 * @return time
	 */
	public static String getTime() {
		LocalTime lt = LocalTime.now();
		String hour = null;
		String minute = null;
		String second = null;
		int i;
		// get hour
		i = lt.getHour();
		if(i <= 9) {
			hour = "0" + i;
		} else if(i >= 10) {
			hour = "" + i;
		}
		// get minute
		i = lt.getMinute();
		if(i <= 9) {
			minute = "0" + i;
		} else if(i >= 10) {
			minute = "" + i;
		}
		// get second
		i = lt.getSecond();
		if(i <= 9) {
			second = "0" + i;
		} else if(i >= 10) {
			second = "" + i;
		}
		String s = new String(hour + ":" + minute + ":" + second);
		return s;
	}
	
	/**
	 * Downloads a file and then attempts to to read it and store it in a String[] array.
	 * @param path
	 * @param location
	 * @return String[]
	 */
	public static String[] readTextFile(String path, String location) {
		download(path, location, "Downloading Server Files");
		Path p = Paths.get(location);
		if(p == null) return null;
		List<String> lines;
		String[] contents;
		try {
			lines = Files.readAllLines(p, Charset.forName("UTF-8"));
			contents = lines.toArray(new String[lines.size()]);
			return contents;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Downloads a file to the specified location.
	 * @param url
	 * @param location
	 */
	public static void download(String url, String location, String windowName) {
		error = false;
		String site = url; 
		String filename = location; 
		JFrame frame = new JFrame(windowName); 
		JProgressBar current = new JProgressBar(0, 100); 
		JLabel t = new JLabel();
		t.setText("Starting...");
		current.setSize(150, 50); 
		current.setValue(0); 
		current.setStringPainted(true); 
		frame.setLayout(new FlowLayout()); 
		frame.setSize(350, 100); 
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
		frame.add(current); 
		frame.add(t);
		Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim2.width/2-frame.getSize().width/2, dim2.height/2-frame.getSize().height/2);
		frame.setVisible(true); 
		try { 
			URL path = new URL(site); 
			HttpURLConnection connection = (HttpURLConnection) path.openConnection(); 
			int filesize = connection.getContentLength(); 
			float totalDataRead = 0; 
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream()); 
			FileOutputStream fos = new FileOutputStream(filename); 
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024); 
			byte[] data = new byte[1024]; 
			int i = 0; 
			while((i = in.read(data, 0, 1024)) >= 0) { 
				totalDataRead = totalDataRead + i; 
				bout.write(data, 0, i); 
				float Percent = (totalDataRead * 100) / filesize; 
				current.setValue((int)Percent);
				t.setText((int)(totalDataRead / 1000000) + " MB of " + (int)(filesize / 1000000) + " MB");
			}	
			bout.close(); 
			in.close();
		} catch(Exception e) {
			t.setText("Download Failed!");
			LogHelper.logError("An error occured while downloading: " + url);
			e.printStackTrace();
			JOptionPane.showConfirmDialog((Component) null, (Object) "Could not download file: " + e.getMessage(), "Error Downloading File", JOptionPane.DEFAULT_OPTION); 
			error = true;
		}
		frame.setVisible(false);
		frame.dispose();
	}
	
	/**
	 * Draws a string centered on the x axis
	 * @param g
	 * @param s
	 * @param y
	 */
	public static void drawCenteredString(Graphics2D g, String s, int y) {
        String text = s;

        FontMetrics fm = g.getFontMetrics();
        int totalWidth = (fm.stringWidth(text) * 2) + 4;
        
        int x = (((Game.getFrame().getWidth() / 2) - (totalWidth / 2)) / 2);
        g.drawString(text, x, y);
	}
	
	/**
	 * Returns the cooldown of an ability in seconds.
	 * @param cd
	 * @return
	 */
	public static int getCooldown(int cd) {
		if(cd > 0) return (cd / 60) + 1;
		else return cd / 60;
	}
	
	/**
	 * Checks to see if their is a newer version of the game available, and provides the link to download it in the console.
	 */
	public static void checkForUpdates() {
		if(!SideScroller.isUpdateEnabled) {
			LogHelper.logInfo("Updates arnt enabled. This is probably because you are running a beta or nightly build.");
			return;
		}
		LogHelper.logInfo("Checking for updates.");
		String[] s = readTextFile(SideScroller.getGame().getServerVersionURL(), "latest.txt");
		
		if(s == null || s[0] == null) {
			LogHelper.logInfo("Error while checking for updates: Could not read server update file.");
		} else if(s[0] != SideScroller.getGame().getLocalVersion()) {
			LogHelper.logInfo("An update is available, you have version " + SideScroller.getGame().getLocalVersion() + ", Server version is " + s[0]);
			LogHelper.logInfo("You can download the update here: " + SideScroller.getGame().getUpdateURL());
			LogHelper.logInfo("NOTE: If you are testing a beta version of the game and it prompts you to update, ignore it.");
			
			int result = JOptionPane.showConfirmDialog((Component) null, (Object) "An newer version of the game, (Build " + s[0] +") is avaliable, do you want to download it now? ", "Update Available - Build " + s[0], JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			
			if(result == JOptionPane.YES_OPTION) {
				String path = saveAs(".jar");
				download(SideScroller.getGame().getDownloadURL(), path, "Downloading Update");
				if(!error) {
					int result2 = JOptionPane.showConfirmDialog((Component) null, (Object) "Download complete. Do you close this instance and run the new build?", "Update Complete", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if(result2 == JOptionPane.YES_OPTION) {
						try {
							ProcessBuilder pb = new ProcessBuilder("java", "-jar", path);
							pb.start();
							System.exit(0);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else if(result2 == JOptionPane.NO_OPTION) return;
				}
			} else {
				return;
			}
		} else {
			LogHelper.logInfo("No update is available.");
		}
	}
	
	/**
	 * Saves the current console textArea to the designated file.
	 * @param extension
	 */
	public static String saveAs(String extension) {
		FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Executable Jar File", extension);
	    final JFileChooser saveAsFileChooser = new JFileChooser();
	    saveAsFileChooser.setApproveButtonText("Save");
	    saveAsFileChooser.setFileFilter(extensionFilter);
	    int actionDialog = saveAsFileChooser.showSaveDialog(Game.getFrame());
	    if (actionDialog != JFileChooser.APPROVE_OPTION) {
	       return null;
	    }
	    
	    File file = saveAsFileChooser.getSelectedFile();
	    if (!file.getName().endsWith(extension)) {
	       file = new File(file.getAbsolutePath() + extension);
	    }
	    
	    return file.toString();
	}
}