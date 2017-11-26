package com.ressq.dominionCases.primitives;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.dominionCases.helpers.ContentStream;
import com.ressq.dominionCases.helpers.FontInfo;

public class BasicText extends BottomLeftAware {

	protected String text;
	protected PDFont font;
	protected int fontSize;
	
	public BasicText(String text, PDFont font, int fontSize) {
		this.text = text;
		this.fontSize = fontSize;
		this.font = font;
	}
	
	public float getWidth() {
		return FontInfo.getWidthForFontSize(font, text, fontSize);
	}
	
	public float getHeight() {
		return FontInfo.getHeightForFontSize(font, fontSize);
	}

	@Override
	public void draw(ContentStream cStream) {
		cStream.beginText();
		cStream.setFont(font, fontSize);
		cStream.setTextMatrix(theta, bottomLeft.getX(), bottomLeft.getY());
		cStream.showText(text);
		cStream.endText();
	}

}
