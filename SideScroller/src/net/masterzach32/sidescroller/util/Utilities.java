package net.masterzach32.sidescroller.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

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
	
	public static String[] readTextFile(String path, String location) {
		download(path, location);
		Path p = Paths.get(location);
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
	
	public static void download(String url, String location) {
		String site = url; 
		String filename = location; 
		JFrame frame = new JFrame("Downloading..."); 
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
			LogHelper.logInfo("Sucesfully downloaded " + url);
		} catch(Exception e) { 
			//JOptionPane.showConfirmDialog((Component) null, "Could not download file: " + e.getMessage(), "Error Downloading File", JOptionPane.DEFAULT_OPTION); 
			LogHelper.logError("An error occured while downloading: " + url);
		}
		frame.setVisible(false);
		frame.dispose();
	}
}