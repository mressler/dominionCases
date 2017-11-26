package com.ressq.dominionCases.primitives;

import static com.ressq.dominionCases.helpers.FontInfo.*;

import org.apache.pdfbox.pdmodel.font.PDFont;

public class ScalableText extends BasicText {
	
	public ScalableText(
		String text, PDFont font, 
		float maxWidth, float maxHeight,
		TextAlignment alignment) 
	{
		super(text, font, 12);
		
		fontSize = getFontSizeToScaleToWidth(font, text, maxWidth);
		if (getHeightForFontSize(font, fontSize) > maxHeight) {
			fontSize = getFontSizeToScaleToHeight(font, maxHeight);
		}
		
		float height = getHeightForFontSize(font, fontSize);
		
		float xAlignment = alignment.getStartingX(maxWidth, getWidthForFontSize(font, text, fontSize));
		
		bottomLeft.applyTranslation(xAlignment, (maxHeight - height) / 2);
	}

}
