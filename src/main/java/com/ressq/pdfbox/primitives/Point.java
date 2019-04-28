package com.ressq.pdfbox.primitives;

public class Point implements Transformable {

	private float x;
	private float y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
	
	public void applyRotation(double theta) {
		Double xPrime = x * Math.cos(theta) - y * Math.sin(theta);
		Double yPrime = y * Math.cos(theta) + x * Math.sin(theta);
		
		x = xPrime.floatValue();
		y = yPrime.floatValue();
	}

	public void applyTranslation(float deltaX, float deltaY) {
		x = x + deltaX;
		y = y + deltaY;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
