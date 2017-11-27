package com.ressq.pdfbox.text;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.pdfbox.helpers.FontInfo;
import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.primitives.Point;

public class MultiLineText extends CompositeDrawable {
	
	public static float TEXT_PADDING = 10f;
	
	private float height;
	private float width;
	
	public MultiLineText(
		String text, PDFont font, int fontSize,
		float width, float height) 
	{
		super();
		this.width = width;
		this.height = height;
		
		Point bottomLeft = new Point(0, 0);
		float fontHeight = FontInfo.getHeightForFontSize(font, fontSize);
		bottomLeft.applyTranslation(TEXT_PADDING, height - fontHeight - TEXT_PADDING);
		
		int lastSplit = 0;
		int splitIndex = 0;
		do {
			splitIndex = getSegmentationIndex(text, font, fontSize, lastSplit, width - 2*TEXT_PADDING);
			
			// Split the line
			BasicText oneLine = new BasicText(text.substring(lastSplit, splitIndex), font, fontSize);
			oneLine.applyTranslation(bottomLeft.getX(), bottomLeft.getY());
			add(oneLine);
			
			lastSplit = splitIndex + 1; // +1 to skip the last space
			bottomLeft.applyTranslation(0,  -1.5f * fontHeight); // Move to the new line
		} while (splitIndex != text.length());
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

}
