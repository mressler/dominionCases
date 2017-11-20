package com.ressq.dominionCases;

public class Card {

	public static final float HEIGHT = 250f;
	public static final float WIDTH = 200f;
	public static final float THICKNESS = 1.2f;
	
	public static float getThicknessFor(int cardCount) {
		return THICKNESS * cardCount;
	}
}
