package com.ressq.pdfbox.helpers;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDFont;

public class FontInfo {

	public static int BBOX_HEIGHT_DIVISOR = 1700;
	public static int STRING_WIDTH_DIVISOR = 1000;

	public static float getHeightForFontSize(PDFont font, int fontSize) {
		return fontSize * (font.getFontDescriptor().getFontBoundingBox().getHeight() / BBOX_HEIGHT_DIVISOR);
	}
	
	public static float getWidthForFontSize(PDFont font, String text, int fontSize) {
		try {
			return fontSize * (font.getStringWidth(text) / STRING_WIDTH_DIVISOR);
		} catch (IOException e) {
			throw new RuntimeException("Error while calculating width", e);
		}
	}
	
	public static int getFontSizeToScaleToWidth(PDFont font, String text, float width) {
		try {
			return (int) (width / (font.getStringWidth(text) / STRING_WIDTH_DIVISOR));
		} catch (IOException e) {
			throw new RuntimeException("Error while calculating width", e);
		}
	}
	
	public static int getFontSizeToScaleToHeight(PDFont font, float height) {
		return (int) (height / (font.getFontDescriptor().getFontBoundingBox().getHeight() / BBOX_HEIGHT_DIVISOR));
	}

	// Total Area = w * h
	// Line count = fontSize * fontWidth / width 
	// Total Height Used = Line Count * fontHeight * lineSpacing * fontSize
	// Percentage area used = totalHeight / height
	// "Fill" font size = totalHeight = height
	// height = (fontSize * fontWidth / width) * fontHeight * lineSpacing * fontSize
	// height = fontSize^2 * fontWidth * fontHeight * lineSpacing / width
	// (height * width) / (fontWidth * fontHeight * lineSpacing) = fontSize^2
	public static float getFillFontSize(
			PDFont font, String text, 
			float width, float height, float lineSpacing) 
	{
		try {
			float fontWidth = (font.getStringWidth(text) / STRING_WIDTH_DIVISOR);
			float fontHeight = (font.getFontDescriptor().getFontBoundingBox().getHeight() / BBOX_HEIGHT_DIVISOR);
			
			return (float) Math.sqrt(height * width / (fontWidth * fontHeight * lineSpacing));
		} catch (IOException e) {
			throw new RuntimeException("Error while calculating width", e);
		}
	}
}
