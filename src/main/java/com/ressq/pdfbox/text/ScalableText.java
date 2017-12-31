package com.ressq.pdfbox.text;

import static com.ressq.pdfbox.helpers.FontInfo.*;

import org.apache.pdfbox.pdmodel.font.PDFont;

public class ScalableText extends BasicText {
	
	public ScalableText(
		String text, PDFont font, 
		float maxWidth, float maxHeight, float centerInHeight,
		TextAlignment alignment, TextAlignment verticalAlignment) 
	{
		super(text, font, 12);
		
		fontSize = getFontSizeToScaleToWidth(font, text, maxWidth);
		if (getHeightForFontSize(font, fontSize) > maxHeight) {
			fontSize = getFontSizeToScaleToHeight(font, maxHeight);
		}
		
		float height = getHeightForFontSize(font, fontSize);
		
		float xAlignment = alignment.getStartingX(maxWidth, getWidthForFontSize(font, text, fontSize));
		float yAlignment = verticalAlignment.getStartingX(centerInHeight, height);
		
		bottomLeft.applyTranslation(xAlignment, yAlignment);
	}

}
