package com.ressq.pdfbox.text;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.pdfbox.helpers.FontInfo;
import com.ressq.pdfbox.helpers.Tuple;
import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.primitives.MultiPointObject;
import com.ressq.pdfbox.primitives.Point;

public class MultiLineText extends CompositeDrawable {
	
	public static float TEXT_PADDING = 10f;
	public static float LINE_SPACING = 1.5f; // Multiplier for height
	
	private float height;
	private float width;
	
	private String remainingText;
	private int usedFontSize;
	
	public MultiLineText(
		String text, PDFont font, int preferredFontSize, int minFontSize,
		MultiPointObject boundingArea) 
	{
		super();
		this.width = boundingArea.getWidth();
		this.height = boundingArea.getHeight();

		usedFontSize = preferredFontSize; // Do I not overflow at all?
		float fillFontSize = FontInfo.getFillFontSize(
				font, text, 
				width - 2*TEXT_PADDING, height - 2*TEXT_PADDING, 
				LINE_SPACING);
		if (fillFontSize < minFontSize) {
			usedFontSize = (int) Math.floor(minFontSize); // Do I overflow a lot and need to get smushed
		} else if (fillFontSize < preferredFontSize) {
			usedFontSize = (int) Math.floor(fillFontSize); // Do I overflow a little and need to shrink?
		}
		
		Point bottomLeft = new Point(0, 0);
		float fontHeight = FontInfo.getHeightForFontSize(font, usedFontSize);
		bottomLeft.applyTranslation(TEXT_PADDING, height - fontHeight - TEXT_PADDING);
		
		int lastSplit = 0;
		int splitIndex = 0;
		do {
			Tuple<Point> bottomWidth = boundingArea.getHorizontalIntersections(bottomLeft.getX(), bottomLeft.getY());
			Tuple<Point> topWidth = boundingArea.getHorizontalIntersections(bottomLeft.getX(), bottomLeft.getY() + fontHeight);
			
			float minWidth = Math.min(bottomWidth.y.getX() - bottomLeft.getX(), topWidth.y.getX() - bottomLeft.getX());
			
			splitIndex = getSegmentationIndex(text, font, usedFontSize, lastSplit, minWidth - 2*TEXT_PADDING);
			
			// Split the line
			BasicText oneLine = new BasicText(text.substring(lastSplit, splitIndex), font, usedFontSize);
			oneLine.applyTranslation(bottomLeft.getX(), bottomLeft.getY());
			add(oneLine);
			
			lastSplit = splitIndex + 1; // +1 to skip the last space
			bottomLeft.applyTranslation(0, -1 * LINE_SPACING * fontHeight); // Move to the new line
			
			// While we have more text to split and more room to put the text
		} while ((splitIndex < text.length()) && (bottomLeft.getY() > TEXT_PADDING));
		
		if (splitIndex < text.length()) {
			remainingText = text.substring(lastSplit);
		}
	}
	
	private static int getSegmentationIndex(
		String text, PDFont font, int fontSize,
		int beginningIndex, float width) 
	{
		int candidateIndex;
		int spaceIndex = beginningIndex;
		float textLength = 0;
		
		do {
			candidateIndex = spaceIndex;
			spaceIndex = text.indexOf(" ", candidateIndex + 1);
			if (spaceIndex == -1) {
				return text.length();
			}
			
			textLength = FontInfo.getWidthForFontSize(
					font, text.substring(beginningIndex, spaceIndex), fontSize);
		} while (textLength < width);
		
		return candidateIndex;
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getWidth() {
		return width;
	}
	
	public String getRemainingText() {
		return remainingText;
	}
	
	public int getUsedFontSize() {
		return usedFontSize;
	}

}
