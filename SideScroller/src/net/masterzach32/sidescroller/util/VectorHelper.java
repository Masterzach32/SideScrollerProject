package net.masterzach32.sidescroller.util;

import java.awt.Point;

public class VectorHelper {
	
	public Point getNextPosition(Vector vector) {
		Point p = new Point(vector.getMapObject().getx(), vector.getMapObject().gety());
		return p;
	}
	
	/**
	 * Returns the trig function in degrees
	 * @param degrees
	 * @return
	 */
	public static double sin(double degrees) {
		return Math.toDegrees(Math.sin(Math.toRadians(degrees)));
	}
	
	/**
	 * Returns the trig function in degrees
	 * @param degrees
	 * @return
	 */
	public static double cos(double degrees) {
		return Math.toDegrees(Math.cos(Math.toRadians(degrees)));
	}
	
	/**
	 * Returns the trig function in degrees
	 * @param degrees
	 * @return
	 */
	public static double tan(double degrees) {
		return Math.toDegrees(Math.tan(Math.toRadians(degrees)));
	}
	
	/**
	 * Returns the trig function in degrees
	 * @param degrees
	 * @return
	 */
	public static double asin(double ratio) {
		return Math.toDegrees(Math.asin(ratio));
	}
	
	/**
	 * Returns the trig function in degrees
	 * @param degrees
	 * @return
	 */
	public static double acos(double ratio) {
		return Math.toDegrees(Math.acos(ratio));
	}
	
	/**
	 * Returns the trig function in degrees
	 * @param degrees
	 * @return
	 */
	public static double atan(double ratio) {
		return Math.toDegrees(Math.atan(ratio));
	}
}