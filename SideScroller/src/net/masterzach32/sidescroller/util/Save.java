package net.masterzach32.sidescroller.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Save {

	public static void writeToSave(String path, String[] save) {
	    try {
	        File saveFile = new File(path);
	        FileOutputStream fos = new FileOutputStream(saveFile);
	        OutputStreamWriter osw = new OutputStreamWriter(fos);    
	        Writer w = new BufferedWriter(osw);
	        for(int i = 0; i < save.length; i++) {
	        	w.write(save[i]);
	        }
	        w.close();
	    } catch (IOException e) {
	        LogHelper.logError("Problem writing " + path);
	        e.printStackTrace();
	    }
	}
	
	public static String[] readFromSave(String path) {
		Path save = Paths.get(path);
		List<String> lines;
		String[] contents = null;
		try {
			lines = Files.readAllLines(save, Charset.forName("UTF-8"));
			contents = lines.toArray(new String[lines.size()]);
			return contents;
		} catch (IOException e) {
			LogHelper.logError("IO Exception while reading save file: " + save);
			e.printStackTrace();
			return null;
		}
	}
}