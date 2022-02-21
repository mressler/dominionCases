package com.ressq.dominionCases.shapes;

import java.awt.Color;
import java.util.EnumSet;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.helpers.FontInfo;
import com.ressq.pdfbox.primitives.Image;
import com.ressq.pdfbox.text.BasicText;
import com.ressq.pdfbox.text.TextAlignment;

public class ImageWithCenteredText extends Image {

	private BasicText centeredText;
	private Color fontColor;
	
	public ImageWithCenteredText(
		String text, 
		Color fontColor,
		PDImageXObject coinImage,
		float height) 
	{
		super(coinImage, height);
		
		int fontSize = FontInfo.getFontSizeToScaleToHeight(PDType1Font.TIMES_BOLD, height / 2);
		
		centeredText = new BasicText(text, PDType1Font.TIMES_BOLD, fontSize);
		float startingX = TextAlignment.CENTER.getStartingX(getWidth(), centeredText.getWidth());
		float startingY = TextAlignment.CENTER.getStartingX(getHeight(), centeredText.getHeight());
		centeredText.applyTranslation(startingX, startingY);
		
		this.fontColor = fontColor;
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
	public void draw(ContentStream cStream, EnumSet<DrawOptions> drawOptions) {
		super.draw(cStream, drawOptions);
		
		cStream.saveGraphicsState();
		cStream.setFontColor(fontColor);
		centeredText.draw(cStream, drawOptions);
		cStream.restoreGraphicsState();
	}

}
