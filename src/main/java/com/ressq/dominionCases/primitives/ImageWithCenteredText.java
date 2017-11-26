package com.ressq.dominionCases.primitives;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.helpers.ContentStream;

public class ImageWithCenteredText extends Image {

	private BasicText centeredText;
	
	public ImageWithCenteredText(String text, PDImageXObject coinImage, float width, float height) {
		super(coinImage, width, height);
		
		centeredText = new BasicText(text, PDType1Font.TIMES_BOLD, 14);
		float startingX = TextAlignment.CENTER.getStartingX(width, centeredText.getWidth());
		float startingY = TextAlignment.CENTER.getStartingX(height, centeredText.getHeight());
		centeredText.applyTranslation(startingX, startingY);
	}

	@Override
	public void applyRotation(double theta) {
		super.applyRotation(theta);
		
		centeredText.applyRotation(theta);
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		super.applyTranslation(deltaX, deltaY);
		
		centeredText.applyTranslation(deltaX, deltaY);
	}
	
	@Override
	public void draw(ContentStream cStream) {
		super.draw(cStream);
		
		centeredText.draw(cStream);
	}

}
