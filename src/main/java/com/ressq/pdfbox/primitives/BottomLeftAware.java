package com.ressq.pdfbox.primitives;

import com.ressq.pdfbox.helpers.Tuple;
import com.ressq.pdfbox.shapes.Rectangle;

public abstract class BottomLeftAware implements Drawable {

	protected Point bottomLeft;
	protected double theta;
	
	public BottomLeftAware() {
		bottomLeft = new Point(0, 0);
		theta = 0;
	}
	
	@Override
	public void applyRotation(double theta) {
		bottomLeft.applyRotation(theta);
		this.theta += theta;
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		bottomLeft.applyTranslation(deltaX, deltaY);
	}

	public void moveTo(Drawable otherPoint) {
		Tuple<Point, Point> boundingBox = otherPoint.getBoundingBox();
		float minX = Math.min(boundingBox.x.getX(), boundingBox.y.getX());
		float minY = Math.min(boundingBox.x.getY(), boundingBox.y.getY());
		this.bottomLeft = new Point(minX, minY);
	}
	
	public Tuple<Point, Point> getBoundingBox() {
		Rectangle boundingBox = new Rectangle(this.getWidth(), this.getHeight());
		boundingBox.applyRotation(theta);
		boundingBox.applyTranslation(bottomLeft.getX(), bottomLeft.getY());
		
		return boundingBox.getBoundingBox();
	}

}
