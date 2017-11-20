package com.ressq.dominionCases.primitives;

public class CardBody extends MultiPointObject {

	public CardBody(float height, float shoulderNotch, float overallWidth) {
		super(6);
		corners.add(new Point(0, 0));
		corners.add(new Point(0, height));
		corners.add(new Point(overallWidth - shoulderNotch, height));
		corners.add(new Point(overallWidth, height - shoulderNotch));
		corners.add(new Point(overallWidth, shoulderNotch));
		corners.add(new Point(overallWidth - shoulderNotch, 0));
	}
}
