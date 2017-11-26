package com.ressq.pdfbox.text;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.pdfbox.helpers.FontInfo;
import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.primitives.Point;

public class MultiLineText extends CompositeDrawable {
	
	private float height;
	private float width;
	
	public MultiLineText(
		String text, PDFont font, int fontSize,
		Point bottomLeft, float width, float height) 
	{
		super();
		this.width = width;
		this.height = height;
		
		float fontHeight = FontInfo.getHeightForFontSize(font, fontSize);
		bottomLeft.applyTranslation(0, height);
		
		int lastSplit = -1;
		int splitIndex = 0;
		do {
			bottomLeft.applyTranslation(0,  -1 * fontHeight); // Move to the new line
			splitIndex = getSegmentationIndex(text, font, fontSize, lastSplit + 1, width); // Split the line, +1 to skip the last space
			
			BasicText oneLine = new BasicText(text.substring(lastSplit, splitIndex), font, fontSize);
			oneLine.applyTranslation(0, bottomLeft.getY());
			add(oneLine);
			
			lastSplit = splitIndex;
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
			spaceIndex = text.indexOf(" ", candidateIndex);
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
