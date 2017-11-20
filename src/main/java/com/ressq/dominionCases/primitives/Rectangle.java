package com.ressq.dominionCases.primitives;

public class Rectangle extends MultiPointObject {
	
	public Rectangle(float maxX, float maxY) {
		super(4);
		corners.add(new Point(0, 0));
		corners.add(new Point(0, maxY));
		corners.add(new Point(maxX, maxY));
		corners.add(new Point(maxX, 0));
	}
	
	public Rectangle(float minX, float minY, float maxX, float maxY) {
		super(4);
		corners.add(new Point(minX, minY));
		corners.add(new Point(minX, maxY));
		corners.add(new Point(maxX, maxY));
		corners.add(new Point(maxX, minY));
	}
	
}
