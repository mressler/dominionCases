package com.ressq.dominionCases.primitives;

import com.ressq.dominionCases.helpers.ContentStream;

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

	@Override
	public abstract void draw(ContentStream cStream);

}
