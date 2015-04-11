package net.masterzach32.sidescroller.util;

import java.time.LocalTime;

public class Utilities {

	/**
	 * Gets the time and returns it in hh:mm:ss
	 * @return time
	 */
	public static String getTime() {
		LocalTime lt = LocalTime.now();
		String s = new String(lt.getHour() + ":" + lt.getMinute() + ":" + lt.getSecond());
		return s;
	}
}
