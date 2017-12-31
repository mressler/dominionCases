package com.ressq.pdfbox.text;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.pdfbox.helpers.FontInfo;
import com.ressq.pdfbox.helpers.Tuple;
import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.primitives.Drawable;
import com.ressq.pdfbox.primitives.MultiPointObject;
import com.ressq.pdfbox.primitives.Point;

public class MultiLineText extends CompositeDrawable {
	
	public static float TEXT_PADDING = 10f;
	public static float LINE_SPACING = 1.5f; // Multiplier for height
	
	private float height;
	private float width;
	
	private String remainingText;
	private int usedFontSize;
	
	public static interface TextElementFactory {
		public Drawable getTextElement(String text, PDFont font, int fontSize);
	}
	
	/**
	 * For use with MultiLineText constructor. 
	 * @return Return a function that will construct BasicText elements for its text lines.
	 */
	public static TextElementFactory getBasicTextElement() {
		return (text, font, fontSize) -> {
			return new BasicText(text, font, fontSize);
		};
	}
	
	public MultiLineText(
		String text, PDFont font, int preferredFontSize, int minFontSize,
		MultiPointObject boundingArea, TextElementFactory elementConstructor) 
	{
		super();
		this.width = boundingArea.getWidth();
		this.height = boundingArea.getHeight();

		usedFontSize = preferredFontSize; // Do I not overflow at all?
		
		if (text == null) {
			return;
		}
		
		if (text.indexOf("\n") == -1) { // Only do best guess if there are no \n's
			float fillFontSize = FontInfo.getFillFontSize(
					font, text, 
					width - 2*TEXT_PADDING, height - 2*TEXT_PADDING, 
					LINE_SPACING);
			if (fillFontSize < minFontSize) {
				usedFontSize = minFontSize; // Do I overflow a lot and need to get smushed
			} else if (fillFontSize < preferredFontSize) {
				usedFontSize = (int) Math.floor(fillFontSize); // Do I overflow a little and need to shrink?
			}
		}
		
		Point bottomLeft = new Point(0, 0);
		float fontHeight = FontInfo.getHeightForFontSize(font, usedFontSize);
		bottomLeft.applyTranslation(TEXT_PADDING, height - TEXT_PADDING);
		float startingHeight = bottomLeft.getY();
		float endingHeight = TEXT_PADDING;
		int numLines;
		
		usedFontSize++; // We have a best guess, let's use that below. (We'll be decrementing right away, so increment here) 
		List<Tuple<Tuple<Integer, Integer>, Point>> segmentationInformation;
		do {
			usedFontSize--;
			fontHeight = FontInfo.getHeightForFontSize(font, usedFontSize);
			numLines = (int) Math.floor((startingHeight - endingHeight) / (fontHeight * LINE_SPACING));
			segmentationInformation = segmentTextToFontSize(text, font, usedFontSize, bottomLeft, boundingArea);
		} while ((usedFontSize > minFontSize) && (segmentationInformation.size() > numLines));
		
		int maxToConsume = Math.min(numLines, segmentationInformation.size());
		segmentationInformation.stream()
			.limit(maxToConsume)
			.forEach(si -> {
				Drawable oneLine = elementConstructor.getTextElement(text.substring(si.x.x, si.x.y), font, usedFontSize);
				oneLine.applyTranslation(si.y.getX(), si.y.getY());
				add(oneLine);
			});
		
		if (numLines < segmentationInformation.size()) {
			remainingText = text.substring(segmentationInformation.get(numLines).x.x);
		}
	}
	
	private float getWidthAtPoint(Point somePoint, MultiPointObject boundingArea, float fontHeight) {
		Tuple<Point, Point> bottomWidth = boundingArea.getHorizontalIntersections(somePoint.getX(), somePoint.getY());
		Tuple<Point, Point> topWidth = boundingArea.getHorizontalIntersections(somePoint.getX(), somePoint.getY() + fontHeight);
		
		return Math.min(bottomWidth.y.getX() - somePoint.getX(), topWidth.y.getX() - somePoint.getX());
	}
	
	private List<Tuple<Tuple<Integer, Integer>, Point>> segmentTextToFontSize(
		String text, 
		PDFont font,
		int usedFontSize,
		Point beginningPoint, 
		MultiPointObject boundingArea) 
	{
		float fontHeight = FontInfo.getHeightForFontSize(font, usedFontSize);
		List<Tuple<Tuple<Integer, Integer>, Point>> segmentationInformation = new ArrayList<>();
		int lastSplit = 0;
		int splitIndex = 0;
		Point currentPoint = new Point(beginningPoint.getX(), beginningPoint.getY() - LINE_SPACING * fontHeight);
		do {
			float minWidth = getWidthAtPoint(currentPoint, boundingArea, fontHeight);
			
			splitIndex = getSegmentationIndex(text, font, usedFontSize, lastSplit, minWidth - 2*TEXT_PADDING);
			
			segmentationInformation.add(new Tuple<>(new Tuple<Integer, Integer>(lastSplit, splitIndex), currentPoint));
			
			lastSplit = splitIndex + 1; // +1 to skip the last space
			currentPoint = new Point(currentPoint.getX(), currentPoint.getY() - LINE_SPACING * fontHeight); // Move to the new line
			
			// While we have more text to split
		} while (splitIndex < text.length());
		
		return segmentationInformation;
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
			int newLineIndex = text.indexOf("\n", candidateIndex + 1);
			if (((newLineIndex != -1) && (newLineIndex < spaceIndex)) || (spaceIndex == -1 && newLineIndex != -1)) {
				spaceIndex = newLineIndex;
			}
			if (spaceIndex == -1) {
				spaceIndex = text.length();
			}
			if ((candidateIndex < text.length()) && (text.charAt(candidateIndex) == '\n')) {
				return candidateIndex;
			}
			
			textLength = FontInfo.getWidthForFontSize(
					font, text.substring(beginningIndex, spaceIndex), fontSize);
		} while ((textLength < width) && (candidateIndex < text.length()));
		
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
