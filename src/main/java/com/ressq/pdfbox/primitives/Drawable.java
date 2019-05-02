package com.ressq.pdfbox.primitives;

import java.util.EnumSet;

import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.helpers.Tuple;

public interface Drawable extends Transformable {

	public void draw(ContentStream cStream, EnumSet<DrawOptions> drawOptions);
	
	public float getHeight();
	
	public float getWidth();
	
	public Tuple<Point, Point> getBoundingBox();
	
}
