package com.ressq.pdfbox.primitives;

public class Line extends MultiPointObject {

	protected Line(float fromX, float fromY, float toX, float toY) {
		super(2);
		addPoint(fromX, fromY);
		addPoint(toX, toY);
	}

}
