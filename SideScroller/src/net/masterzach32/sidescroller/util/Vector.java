package net.masterzach32.sidescroller.util;

import java.awt.Point;

import net.masterzach32.sidescroller.entity.MapObject;

/**
 * Vector helper class for moving MapObjects
 * 
 * @author Zach Kozar
 */
public class Vector {
	
	private Vector vector;
	private MapObject object;
	private double direction, magnitude;
	private Point origen = new Point(0, 0), start;

	/**
	 * Creates a new vector object
	 */
	public Vector() {}
	
	/**
	 * Creates a new vector with the specified MapObject
	 * @param object
	 */
	public Vector(MapObject object) {
		vector = this;
		this.object = object;
	}
	
	/**
	 * Creates a new vector with the specified start and end points, and a magnitude (p/t)
	 * @param object
	 * @param start
	 * @param finish
	 * @param magnitude
	 */
	public Vector(MapObject object, Point start, Point end, double magnitude) {
		vector = this;
		this.object = object;
		this.start = start;
		this.magnitude = magnitude;
		
		double dx = end.getX() - start.getX();
		double dy = end.getY() - start.getY();
		
		direction = VectorHelper.atan(dx / dy);
		
		if(direction < 180) {
		} else if(direction > 180) {
			this.direction = direction - 360;
		} else {
			this.direction = 179;
		}
	}
	
	/**
	 * Creates a new vector with the specified direction (degrees) and magnitude (p/t)
	 * @param object
	 * @param start
	 * @param direction
	 * @param magnitude
	 */
	public Vector(MapObject object, Point start, double direction, double magnitude) {
		vector = this;
		this.object = object;
		this.start = start;
		this.magnitude = magnitude;
		
		if(direction < 180) {
			this.direction = direction;
		} else if(direction > 180) {
			this.direction = direction - 360;
		} else {
			this.direction = 179;
		}
	}
	
	/**
	 * Returns the next change in x and change in y as a point.
	 * @return
	 */
	public Point getNextOffset() {
		int dx = 0;
		int dy = 0;
		dx = (int) (VectorHelper.sin(direction) * magnitude);
		dy = (int) (VectorHelper.cos(direction) * magnitude);
		return new Point((int) dx, (int) dy);
	}
	
	public Vector getVector() {
		return vector;
	}
	
	public void setVector(Vector vector) {
		this.vector = vector;
	}

	public MapObject getMapObject() {
		return object;
	}

	public void setMapObject(MapObject object) {
		this.object = object;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}

	public Point getOrigen() {
		return origen;
	}

	public void setOrigen(Point origen) {
		this.origen = origen;
	}

	public Point getStartPoint() {
		return start;
	}

	public void setStartPoint(Point start) {
		this.start = start;
	}
}