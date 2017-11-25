package com.ressq.dominionCases.primitives;

public class Rectangle extends MultiPointObject {
	
	public Rectangle(float maxX, float maxY) {
		super(4);
		addPoint(0, 0);
		addPoint(0, maxY);
		addPoint(maxX, maxY);
		addPoint(maxX, 0);
	}
	
}
