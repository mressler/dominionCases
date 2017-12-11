package com.ressq.dominionCases.shapes;

import static com.ressq.dominionCases.shapes.Card.HEIGHT;
import static com.ressq.dominionCases.shapes.Card.WIDTH;
import static com.ressq.dominionCases.shapes.Card.getThicknessFor;
import static com.ressq.dominionCases.shapes.Card.inchesToPixels;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.shapes.Rectangle;
import com.ressq.pdfbox.text.MultiLineText;
import com.ressq.pdfbox.text.ScalableText;
import com.ressq.pdfbox.text.TextAlignment;
import com.ressq.pdfbox.text.TextSubstitution;

public class CardCase extends CompositeDrawable {

	public static final float PEEK_HEIGHT = HEIGHT;
	public static final float SHOULDER_SIZE = inchesToPixels(0.4);
	public static final float SHOULDER_HEIGHT = PEEK_HEIGHT - SHOULDER_SIZE;
	public static final float FOLD_UNDER_WIDTH = inchesToPixels(0.5);
	public static final float EXTERNAL_GLUE_WIDTH = inchesToPixels(0.3);
	public static final float FLAP_HEIGHT = inchesToPixels(0.3);
	public static final float TEXT_PADDING = 2.5f;
	
	private CardBody mainCardBody;
	
	public CardCase(
			int cardCount,
			int cardCost, String cardName, String description,
			PDImageXObject coinImage, PDFont titleFont, PDFont contentFont) 
	{
		super();
		
		float thickness = getThicknessFor(cardCount);
		/////////
		Rectangle bottomFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, thickness);
		bottomFoldUnder.applyTranslation(-1 * FOLD_UNDER_WIDTH, 0);
		add(bottomFoldUnder);
		
		/////////
		Rectangle bottom = new Rectangle(SHOULDER_HEIGHT, thickness);
		add(bottom);
		
		/////////
		mainCardBody = new CardBody(WIDTH, SHOULDER_SIZE, PEEK_HEIGHT);
		add(mainCardBody);

		/////////
		MultiLineText mainText = new MultiLineText(
			description, contentFont, 14, 7, 
			mainCardBody, getReplaceTextElement(coinImage));
		mainText.applyTranslation(0, bottom.getHeight());
		add(mainText);
		
		// Now apply the CardBody translation since we had used it for the MultiLineText
		mainCardBody.applyTranslation(0, bottom.getHeight());
		
		/////////
		Rectangle top = new Rectangle(SHOULDER_HEIGHT, thickness);
		top.applyTranslation(0, bottom.getHeight() + mainCardBody.getHeight());
		add(top);

		/////////
		ScalableText topText = new ScalableText(
			cardName, titleFont,
			top.getWidth() - TEXT_PADDING * 2, 
			top.getHeight() - TEXT_PADDING * 2,
			TextAlignment.CENTER);
		topText.applyTranslation(
			TEXT_PADDING, 
			bottom.getHeight() + mainCardBody.getHeight() + TEXT_PADDING);
		add(topText);
		
		/////////
		TopFlap topFlap = new TopFlap(
			SHOULDER_HEIGHT, FLAP_HEIGHT,
			cardCost, cardName,
			coinImage, titleFont);
		topFlap.applyTranslation(0, bottom.getHeight() + mainCardBody.getHeight() + top.getHeight());
		add(topFlap);
		
		/////////
		Rectangle topFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, thickness);
		topFoldUnder.applyTranslation(-1 * FOLD_UNDER_WIDTH, bottom.getHeight() + mainCardBody.getHeight());
		add(topFoldUnder);
		
		/////////
		Rectangle back = new Rectangle(thickness, WIDTH);
		back.applyTranslation(-1 * thickness, bottom.getHeight());
		add(back);
		
		/////////
		float glueWidth = Math.max(EXTERNAL_GLUE_WIDTH, FOLD_UNDER_WIDTH - thickness);
		Rectangle externalGlueArea = new Rectangle(glueWidth, WIDTH);
		externalGlueArea.applyTranslation(-1 * back.getWidth() - externalGlueArea.getWidth(), bottom.getHeight());
		add(externalGlueArea);
		
		/////////
		CardBody upsideDownCardBody = new CardBody(WIDTH, SHOULDER_SIZE, PEEK_HEIGHT);
		add(upsideDownCardBody);
		
		/////////
		Rectangle workableArea2 = new Rectangle(SHOULDER_HEIGHT - glueWidth, mainCardBody.getHeight());
		MultiLineText secondaryText = new MultiLineText(
			mainText.getRemainingText() != null ? mainText.getRemainingText() : description, 
			contentFont, mainText.getUsedFontSize(), mainText.getUsedFontSize(), 
			workableArea2, getReplaceTextElement(coinImage));
		secondaryText.applyRotation(Math.PI);
		secondaryText.applyTranslation(secondaryText.getWidth() + glueWidth, 0);
		add(secondaryText);
		
		// Now apply the CardBody translation since we are finished with it
		upsideDownCardBody.applyTranslation(0, -1 * upsideDownCardBody.getHeight());
//		workableArea2.applyRotation(Math.PI);
//		workableArea2.applyTranslation(workableArea2.getWidth() + glueWidth, 0);
//		add(workableArea2);
		
		/////////
		TopFlap upsideDownTopFlap = new TopFlap(
				SHOULDER_HEIGHT, FLAP_HEIGHT,
				cardCost, cardName,
				coinImage, titleFont);
		upsideDownTopFlap.applyRotation(Math.PI);
		upsideDownTopFlap.applyTranslation(upsideDownTopFlap.getWidth(), -1 * upsideDownCardBody.getHeight());
		add(upsideDownTopFlap);
	}
	
	private MultiLineText.GetTextElement getReplaceTextElement(PDImageXObject coinImage) {
		return (text, font, fontSize) -> {
			return new TextSubstitution(
				text, font, fontSize,
				"<(\\d)>", (m, height) -> {
					return new ImageWithCenteredText(m.group(1), coinImage, height, height);
				});
		};
	}

	@Override
	public float getHeight() {
		return mainCardBody.getHeight();
	}

	@Override
	public float getWidth() {
		return mainCardBody.getWidth();
	}
}
