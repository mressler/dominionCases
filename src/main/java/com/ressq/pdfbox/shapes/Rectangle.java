package com.ressq.pdfbox.shapes;

import java.util.EnumSet;

import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.primitives.MultiPointObject;

public class Rectangle extends MultiPointObject {
	
	public Rectangle(float width, float height) {
		this(width, height, DrawOptions.defaults());
	}

	public Rectangle(float width, float height, EnumSet<DrawOptions> options) {
		super(4, options);
		addPoint(0, 0);
		addPoint(0, height);
		addPoint(width, height);
		addPoint(width, 0);
	}
	
}
