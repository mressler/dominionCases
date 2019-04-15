package com.ressq.dominionCases.shapes;

import java.util.EnumSet;

import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.primitives.MultiPointObject;

public class CardBody extends MultiPointObject {

	public CardBody(float height, float shoulderNotch, float overallWidth) {
		this(height, shoulderNotch, overallWidth, DrawOptions.defaults());
	}

	public CardBody(float height, float shoulderNotch, float overallWidth, EnumSet<DrawOptions> options) {
		super(6, options);
		addPoint(0, 0);
		addPoint(0, height);
		addPoint(overallWidth - shoulderNotch, height);
		addPoint(overallWidth, height - shoulderNotch);
		addPoint(overallWidth, shoulderNotch);
		addPoint(overallWidth - shoulderNotch, 0);
	}
}
