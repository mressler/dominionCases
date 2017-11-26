package com.ressq.pdfbox.primitives;

public interface Transformable {

	public void applyRotation(double theta);
	
	public void applyTranslation(float deltaX, float deltaY);
	
}
