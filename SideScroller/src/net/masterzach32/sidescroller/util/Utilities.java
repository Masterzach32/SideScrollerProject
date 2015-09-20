package net.masterzach32.sidescroller.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import net.masterzach32.sidescroller.gamestate.menus.LoadingState;
import net.masterzach32.sidescroller.main.Game;
import net.masterzach32.sidescroller.main.SideScroller;

public class Utilities {

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
	 * @param show
	 * @return String[]
	 */
	public static String[] readTextFile(String path, Path location, boolean show) {
		download(path, location.toString(), "Downloading Server Files", show);
		List<String> lines;
		String[] contents;
		try {
			lines = Files.readAllLines(location, Charset.forName("UTF-8"));
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
	public static void download(String url, String location, String windowName, boolean useWindow) {
		String site = url;
		String filename = location;
		JFrame frame = new JFrame(windowName);
		JProgressBar current = new JProgressBar(0, 100);
		JLabel t = new JLabel();
		t.setText("Starting...");
		LoadingState.setInfo("Starting Update", 0);
		current.setSize(150, 50);
		current.setValue(0);
		current.setStringPainted(true);
		frame.setLayout(new FlowLayout());
		frame.setSize(350, 100);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.add(current);
		frame.add(t);
		frame.setResizable(false);
		Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim2.width/2-frame.getSize().width/2, dim2.height/2-frame.getSize().height/2);
		frame.setVisible(useWindow);
		try {
			URL path = new URL(site);
			HttpURLConnection connection = (HttpURLConnection) path.openConnection();
			int filesize = connection.getContentLength();
			double totalDataRead = 0;
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			FileOutputStream fos = new FileOutputStream(filename);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[1024];
			int i = 0;
			double percent = 0;
			long l = System.nanoTime();
			while((i = in.read(data, 0, 1024)) >= 0) {
				totalDataRead = totalDataRead + i;
				bout.write(data, 0, i);
				percent = (totalDataRead * 100) / filesize;
				current.setValue((int) percent);
				LoadingState.setInfo("Updating... " + (int) (totalDataRead / 1000000) + " MB of " + (int) (filesize / 1000000) + " MB - " + (int) ((totalDataRead / 1000) / ((System.nanoTime() - l) / 1000000000)) + " KBPS", percent);
				t.setText((int) (totalDataRead / 1000000) + " MB of " + (int) (filesize / 1000000) + " MB");
			}
			LoadingState.setInfo("Finishing Up...", percent);
			bout.close();
			in.close();
			percent = 0;
			LoadingState.setInfo("Finished Download", percent);
		} catch(Exception e) {
			t.setText("Download Failed!");
			LoadingState.setInfo("Download Failed!", 0);
			LogHelper.logger.logError("An error occured while downloading: " + url);
			e.printStackTrace();
			createErrorDialog("Download Error", "An error occured while downloading this file:\n" + url, e);
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
        
        int x = (((Game.getFrame().getWidth() / SideScroller.SCALE) - (totalWidth / 2)) / 2);
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
	 * Opens a {@link JFileChooser} for the user to select a location, and returns the path as a String
	 * @param extension
	 */
	public static String saveAs(String extension) {
	    final JFileChooser saveAsFileChooser = new JFileChooser();
	    
	    saveAsFileChooser.setApproveButtonText("Save");
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
	
	/**
	 * Close a file or other stream with error checking/trapping.
	 * 
	 * Thanks to StackOverflow user barjak for the example at
	 * <http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file>
	 */
	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Gets the mouse position on the screen.
	 * @return
	 */
	public static Point getMousePosition() {
		Point p1 = MouseInfo.getPointerInfo().getLocation();
		Point p2 = Game.getFrame().getLocationOnScreen();
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}
	
	/**
	 * Checks to see if one entity is within another entities range.
	 * 
	 * FIXME: Finish this method later
	 * 
	 * @param range
	 * @return
	 */
	public static boolean checkRange(int range) {
		boolean inRange = false;
		
		return inRange;
	}
	
	/**
	 * Creates an error dialog and prints the stacktrace to the console.
	 * 
	 * @param title
	 * @param message
	 * @param e
	 */
	public static void createErrorDialog(String title, String message, Exception e) {
		e.printStackTrace();
		
	    StringBuilder sb = new StringBuilder(e.toString());
	    for(StackTraceElement ste : e.getStackTrace()) {
	        sb.append("\n\tat ");
	        sb.append(ste);
	    }
	    
	    String trace = sb.toString();
	    JOptionPane.showMessageDialog(null, message + "\n\n" + trace + "\n\n" + "If this error continues please report it at https://github.com/Masterzach32/SideScrollerProject/issues", title, JOptionPane.ERROR_MESSAGE);
	}
}