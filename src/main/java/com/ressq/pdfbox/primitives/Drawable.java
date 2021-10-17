package com.ressq.pdfbox.primitives;

import java.util.EnumSet;
import java.util.stream.Stream;

import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.helpers.Tuple;

public interface Drawable extends Transformable {

	void draw(ContentStream cStream, EnumSet<DrawOptions> drawOptions);
	
	float getHeight();
	
	float getWidth();
	
	Tuple<Point, Point> getBoundingBox();

	default float getMaxX() {
		return getBoundingBox().y.getX();
	}

	default float getMinX() {
		return getBoundingBox().x.getX();
	}

	default float getMaxY() {
		return getBoundingBox().y.getY();
	}

	default float getMinY() {
		return getBoundingBox().x.getY();
	}

	default Stream<Line> getLines() {
		return Stream.empty();
	}
	
}
