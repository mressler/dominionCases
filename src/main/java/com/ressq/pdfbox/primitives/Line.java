package com.ressq.pdfbox.primitives;

public class Line extends MultiPointObject {

	public Line(float fromX, float fromY, float toX, float toY) {
		super(2);
		addPoint(fromX, fromY);
		addPoint(toX, toY);
	}
	
	public Line(Point from, Point to) {
		super(2);
		add(from);
		add(to);
	}

	public float getSlope() {
		return (corners.get(0).getY() - corners.get(1).getY()) /
			(corners.get(0).getX() - corners.get(1).getX());
	}
	
	public float getIntercept() {
		return corners.get(0).getY() - getSlope() * corners.get(0).getX();
	}
	
	public float getYForX(float x) {
		return getSlope() * x + getIntercept();
	}
	
	public float getXForY(float y) {
		return (y - getIntercept()) / getSlope();
	}
	
	public Point getIntersection(Line otherLine) {
		float x = (otherLine.getIntercept() - getIntercept()) /
			(getSlope() - otherLine.getSlope());
		float y = getYForX(x);
		return new Point(x, y);
	}
	
	public static Line getHorizontal(float y) {
		return new Line(0, y, 1, y);
	}
}
