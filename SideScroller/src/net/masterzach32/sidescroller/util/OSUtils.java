package net.masterzach32.sidescroller.util;

import java.io.File;
/**
 * Helper class to check the operating system this Java VM runs in
 *
 * Please keep the notes below as a pseudo-license
 *
 * http://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
 * compare to http://svn.terracotta.org/svn/tc/dso/tags/2.6.4/code/base/common/src/com/tc/util/runtime/Os.java
 * http://www.docjar.com/html/api/org/apache/commons/lang/SystemUtils.java.html
 */
import java.util.Locale;
public final class OSUtils {
	
	/**
	 * Types of Operating Systems
	 */
	public enum OSType {
		Windows, MacOS, Linux, Other
	};

	// cached result of OS detection
	protected static OSType detectedOS;

	/**
	 * Detect the operating system from the os.name System property and cache
	 * the result
	 * 
	 * @returns - the operating system detected
	 */
	public static OSType getOperatingSystemType() {
		if(detectedOS == null) {
			String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
			if((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
				detectedOS = OSType.MacOS;
			} else if(OS.indexOf("win") >= 0) {
				detectedOS = OSType.Windows;
			} else if(OS.indexOf("nux") >= 0) {
				detectedOS = OSType.Linux;
			} else {
				detectedOS = OSType.Other;
			}
		}
		return detectedOS;
	}
	
	public static String getHomeDirectory(String fileName) {
		File file = null;
		
		OSUtils.OSType ostype = OSUtils.getOperatingSystemType();
		switch (ostype) {
		    case Windows:
		    	file = new File(System.getProperty("user.home") + "\\" + fileName);
		    	break;
		    case MacOS: 
		    	file = new File(System.getProperty("user.home") + "/" + fileName);
		    	break;
		    case Linux: 
		    	file = new File(System.getProperty("user.home") + "/" + fileName);
		    	break;
		    case Other: 
		    	file = new File(fileName);
		    	break;
		}
		
		return file.toString();
	}
}