package com.ressq.dominionCases;

public class Card {

	public static final float HEIGHT = inchesToPixels(3.15);
	public static final float WIDTH = inchesToPixels(2.45);
	public static final float THICKNESS = 1.368f;
	
	// This used to be in PDPage, but they removed it some time before 2.0.4
	public static final float DEFAULT_USER_SPACE_UNIT_DPI = 72;
	
	public static float getThicknessFor(int cardCount) {
		return THICKNESS * cardCount;
	}
	
	public static final float inchesToPixels(double inches) {
		return (float) (inches * DEFAULT_USER_SPACE_UNIT_DPI);
	}
}
