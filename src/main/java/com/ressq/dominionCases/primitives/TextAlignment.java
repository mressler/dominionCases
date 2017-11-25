package com.ressq.dominionCases.primitives;

public enum TextAlignment {

	LEFT,
	CENTER,
	RIGHT;
	
	public float getStartingX(float maxWidth, float objectWidth) {
		switch (this) {
		case LEFT:
			return 0;
		case CENTER:
			return (maxWidth - objectWidth) / 2;
		case RIGHT:
			return maxWidth - objectWidth;
		}
		return 0;
	}
	
}
