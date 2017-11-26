package com.ressq.pdfbox.primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.ressq.helpers.MinMaxHolder;
import com.ressq.pdfbox.helpers.ContentStream;

public class MultiPointObject implements Drawable {

	private List<Point> corners;
	
	public MultiPointObject(int initialCapacity) {
		corners = new ArrayList<Point>(initialCapacity);
	}
	
	public void add(Point somePoint) {
		corners.add(somePoint);
	}
	
	public void addPoint(float x, float y) {
		corners.add(new Point(x, y));
	}
	
	@Override
	public void applyRotation(double theta) {
		corners.stream().forEach(p -> p.applyRotation(theta));
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		corners.stream().forEach(p -> p.applyTranslation(deltaX, deltaY));
	}

	@Override
	public void draw(ContentStream cStream) {
		Stream<Point> allPoints = corners.stream();
		
		Point first = corners.get(0);
		cStream.moveTo(first);
		
		allPoints = allPoints.skip(1);
		allPoints.forEach(cStream::lineTo);
		
		cStream.closeAndStroke();
	}
	
	public float getHeight() {
		return corners.stream().map(Point::getY).collect(
				MinMaxHolder.collectThen(mmh -> mmh.getMax() - mmh.getMin()));
	}
	
	public float getWidth() {
		return corners.stream().map(Point::getX).collect(
				MinMaxHolder.collectThen(mmh -> mmh.getMax() - mmh.getMin()));
	}
}
