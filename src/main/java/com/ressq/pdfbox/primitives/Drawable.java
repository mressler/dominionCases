package com.ressq.pdfbox.primitives;

import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.Tuple;

public interface Drawable extends Transformable {

	public void draw(ContentStream cStream);
	
	public float getHeight();
	
	public float getWidth();
	
	public Tuple<Point, Point> getBoundingBox();
	
}
