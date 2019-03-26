package com.ressq.pdfbox.primitives;

import com.ressq.pdfbox.helpers.ContentStream;
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
	
	public Tuple<Point, Point> getBoundingBox() {
		Rectangle boundingBox = new Rectangle(this.getWidth(), this.getHeight());
		boundingBox.applyRotation(theta);
		boundingBox.applyTranslation(bottomLeft.getX(), bottomLeft.getY());
		
		return boundingBox.getBoundingBox();
	}

	@Override
	public abstract void draw(ContentStream cStream);

}
