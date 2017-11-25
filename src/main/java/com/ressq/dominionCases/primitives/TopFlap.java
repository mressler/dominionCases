package com.ressq.dominionCases.primitives;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.helpers.ContentStream;

public class TopFlap extends MultiPointObject {

	public static final float TEXT_PADDING = 2.5f;
	public static final float IMAGE_PADDING = 1;
	
	private ScalableText title;
	private Coin coin;
	
	public TopFlap(
		float width, float height, 
		int cardCost, String cardName, 
		PDImageXObject coinImage, PDFont titleFont) 
	{
		super(4);
		addPoint(0, 0);
		addPoint(height, height);
		addPoint(width - height, height);
		addPoint(width, 0);
		
		float coinWidth = height - IMAGE_PADDING * 2;
		float coinHeight = coinWidth;
		
		coin = new Coin(coinImage, coinWidth, coinHeight);
		coin.applyTranslation(height,  (height - coinHeight) / 2);
		
		title = new ScalableText(
				cardName, titleFont, 
				width - 2 * height - coinWidth - TEXT_PADDING,
				height - 2 * TEXT_PADDING);
		title.applyTranslation(
				height + coinWidth + TEXT_PADDING, 
				TEXT_PADDING);
	}
	
	@Override
	public void applyRotation(double theta) {
		super.applyRotation(theta);
		
		title.applyRotation(theta);
		coin.applyRotation(theta);
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		super.applyTranslation(deltaX, deltaY);
		
		title.applyTranslation(deltaX, deltaY);
		coin.applyTranslation(deltaX, deltaY);
	}

	@Override
	public void draw(ContentStream cStream) {
		super.draw(cStream);
		
		title.draw(cStream);
		coin.draw(cStream);
	}
}
