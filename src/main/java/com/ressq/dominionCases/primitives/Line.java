package com.ressq.dominionCases.primitives;

public class Line extends MultiPointObject {

	protected Line(float fromX, float fromY, float toX, float toY) {
		super(2);
		corners.add(new Point(fromX, fromY));
		corners.add(new Point(toX, toY));
	}

}
