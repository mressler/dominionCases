package com.ressq.dominionCases.primitives;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.dominionCases.helpers.ContentStream;

public class ScalableText implements Drawable {

	private Point bottomLeft;
	private float theta;

	private String text;
	private PDFont font;
	private int fontSize;
	
	public ScalableText(String text, PDFont font, float maxWidth, float maxHeight) {
		bottomLeft = new Point(0, 0);
		theta = 0;
		this.text = text;
		this.font = font;
		
		fontSize = getFontSizeToScaleToWidth(maxWidth);
		if (getHeightForFontSize(fontSize) > maxHeight) {
			fontSize = getFontSizeToScaleToHeight(maxHeight);
		}
		
		float height = getHeightForFontSize(fontSize);
		bottomLeft.applyTranslation(0, (maxHeight - height) / 2);
	}
	
	private float getHeightForFontSize(int fontSize) {
		return fontSize * (font.getFontDescriptor().getFontBoundingBox().getHeight() / 1700);
	}
	
	@SuppressWarnings("unused")
	private float getWidthForFontSize(int fontSize) {
		try {
			return fontSize * (font.getStringWidth(text) / 1000);
		} catch (IOException e) {
			throw new RuntimeException("Error while calculating width", e);
		}
	}
	
	private int getFontSizeToScaleToWidth(float width) {
		try {
			return (int) (width / (font.getStringWidth(text) / 1000));
		} catch (IOException e) {
			throw new RuntimeException("Error while calculating width", e);
		}
	}
	
	private int getFontSizeToScaleToHeight(float height) {
		return (int) (height / (font.getFontDescriptor().getFontBoundingBox().getHeight() / 1700));
	}
	
	@Override
	public void applyRotation(double theta) {
		this.theta += theta;
		bottomLeft.applyRotation(theta);
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		bottomLeft.applyTranslation(deltaX, deltaY);
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
