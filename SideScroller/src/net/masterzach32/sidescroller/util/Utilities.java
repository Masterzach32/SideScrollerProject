package net.masterzach32.sidescroller.util;

import java.time.LocalTime;

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
}