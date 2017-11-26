package com.ressq.dominionCases.shapes;

import static com.ressq.dominionCases.shapes.CardCase.*;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.primitives.Image;
import com.ressq.pdfbox.primitives.MultiPointObject;
import com.ressq.pdfbox.text.ScalableText;
import com.ressq.pdfbox.text.TextAlignment;

public class TopFlap extends CompositeDrawable {

	public static final float IMAGE_PADDING = 1;
	
	private MultiPointObject outline;
	
	public TopFlap(
		float width, float height, 
		Integer cardCost, String cardName, 
		PDImageXObject coinImage, PDFont titleFont) 
	{
		super();
		
		outline = new MultiPointObject(4);
		outline.addPoint(0, 0);
		outline.addPoint(height, height);
		outline.addPoint(width - height, height);
		outline.addPoint(width, 0);
		add(outline);
		
		float coinWidth = height - IMAGE_PADDING * 2;
		float coinHeight = coinWidth;
		
		Image coin = new ImageWithCenteredText(cardCost.toString(), coinImage, coinWidth, coinHeight);
		coin.applyTranslation(height,  (height - coinHeight) / 2);
		add(coin);
		
		ScalableText title = new ScalableText(
				cardName, titleFont, 
				width - 2 * height - coinWidth - TEXT_PADDING, // Only 1x TEXT_PADDING because the last part is on a slant. Fine to creep in a bit
				height - 2 * TEXT_PADDING,
				TextAlignment.LEFT);
		title.applyTranslation(
				height + coinWidth + TEXT_PADDING, 
				TEXT_PADDING);
		add(title);
	}

	@Override
	public float getHeight() {
		return outline.getHeight();
	}

	@Override
	public float getWidth() {
		return outline.getWidth();
	}
}
