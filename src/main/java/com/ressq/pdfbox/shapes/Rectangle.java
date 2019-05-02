package com.ressq.pdfbox.shapes;

import com.ressq.pdfbox.primitives.MultiPointObject;

public class Rectangle extends MultiPointObject {

	public Rectangle(float width, float height) {
		super(4);
		addPoint(0, 0);
		addPoint(0, height);
		addPoint(width, height);
		addPoint(width, 0);
	}
	
}
