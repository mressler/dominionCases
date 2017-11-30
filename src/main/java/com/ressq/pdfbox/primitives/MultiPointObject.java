package com.ressq.pdfbox.primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.ressq.helpers.BisectClosest;
import com.ressq.helpers.MinMaxHolder;
import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.Tuple;

public class MultiPointObject implements Drawable {

	protected List<Point> corners;
	
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
	
	public Stream<Line> getLines() {
		Point from = corners.get(0);
		Builder<Line> streamBuilder = Stream.builder();
		for (int i = 1; i < corners.size(); i++) {
			Point to = corners.get(i);
			streamBuilder.add(new Line(from, to));
			from = to;
		}
		
		return streamBuilder.build();
	}
	
	public Tuple<Point> getHorizontalIntersections(float fromX, float fromY) {
		Line horizontal = Line.getHorizontal(fromY);
		
		Tuple<Point> results = getLines()
			.filter(l -> l.getSlope() != 0) // Horizontal won't intersect with horizontal
			.map(l -> l.getIntersection(horizontal)) // Turn lines into intersection points
			.map(Point::getX) // Get the X coordinate of intersection
			.collect(BisectClosest.bisect(fromX, bc -> { // Bisect about the fromPoint.x
				Tuple<Point> leftRight = new Tuple<>(); // and then turn back to points since we know the y
				leftRight.x = new Point(bc.getClosestLessThan(), fromY);
				leftRight.y = new Point(bc.getClosestGreaterThan(), fromY);
				
				return leftRight;
			}));
		
		return results;
	}

}
