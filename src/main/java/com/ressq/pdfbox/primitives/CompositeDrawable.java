package com.ressq.pdfbox.primitives;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import com.ressq.helpers.MinMaxHolder;
import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.helpers.Tuple;

public abstract class CompositeDrawable implements Drawable {

	protected List<Drawable> components;
	
	public CompositeDrawable() {
		components = new ArrayList<>();
	}
	
	protected void add(Drawable component) {
		components.add(component);
	}
	
	@Override
	public void applyRotation(double theta) {
		components.stream().forEach(p -> p.applyRotation(theta));
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		components.stream().forEach(p -> p.applyTranslation(deltaX, deltaY));
	}

	@Override
	public void draw(ContentStream cStream, EnumSet<DrawOptions> drawOptions) {
		components.stream().forEach(p -> p.draw(cStream, drawOptions));
	}
	
	@Override
	public Tuple<Point, Point> getBoundingBox() {
		MinMaxHolder<Float> xBounds = components.stream()
			.map(Drawable::getBoundingBox)
			.flatMap(t -> Stream.of(t.x, t.y))
			.map(Point::getX)
			.collect(MinMaxHolder.collect());
		MinMaxHolder<Float> yBounds = components.stream()
				.map(Drawable::getBoundingBox)
				.flatMap(t -> Stream.of(t.x, t.y))
				.map(Point::getY)
				.collect(MinMaxHolder.collect());
		
		return new Tuple<>(
				new Point(xBounds.getMin(), yBounds.getMin()), 
				new Point(xBounds.getMax(), yBounds.getMax()));
	}
	
	@Override
	public float getWidth() {
		Tuple<Point, Point> bbox = getBoundingBox();
		return bbox.y.getX() - bbox.x.getX();
	}
	
	@Override
	public float getHeight() {
		Tuple<Point, Point> bbox = getBoundingBox();
		return bbox.y.getY() - bbox.x.getY();
	}

}
