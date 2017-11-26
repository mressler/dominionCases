package com.ressq.dominionCases.helpers;

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
}
