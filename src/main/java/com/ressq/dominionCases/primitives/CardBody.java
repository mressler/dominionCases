package com.ressq.dominionCases.primitives;

public class CardBody extends MultiPointObject {

	public CardBody(float height, float shoulderNotch, float overallWidth) {
		super(6);
		addPoint(0, 0);
		addPoint(0, height);
		addPoint(overallWidth - shoulderNotch, height);
		addPoint(overallWidth, height - shoulderNotch);
		addPoint(overallWidth, shoulderNotch);
		addPoint(overallWidth - shoulderNotch, 0);
	}
}
