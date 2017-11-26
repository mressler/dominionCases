package com.ressq.dominionCases.shapes;

import static com.ressq.dominionCases.shapes.Card.*;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.primitives.Drawable;
import com.ressq.pdfbox.primitives.Transformable;
import com.ressq.pdfbox.shapes.Rectangle;
import com.ressq.pdfbox.text.ScalableText;
import com.ressq.pdfbox.text.TextAlignment;

public class CardCase implements Transformable {

	public static final float PEEK_HEIGHT = HEIGHT;
	public static final float SHOULDER_SIZE = inchesToPixels(0.4);
	public static final float SHOULDER_HEIGHT = PEEK_HEIGHT - SHOULDER_SIZE;
	public static final float FOLD_UNDER_WIDTH = inchesToPixels(0.5);
	public static final float EXTERNAL_GLUE_WIDTH = inchesToPixels(0.3);
	public static final float FLAP_HEIGHT = inchesToPixels(0.3);
	public static final float TEXT_PADDING = 2.5f;
	
	private int cardCost;
	private String cardName;
	private String description;
	
	List<Drawable> translateAndDraw;
	
	public CardCase(
			int cardCount,
			int cardCost, String cardName, String description,
			PDImageXObject coinImage, PDFont titleFont) 
	{
		float thickness = getThicknessFor(cardCount);
		translateAndDraw = new ArrayList<>();
		/////////
		Rectangle bottomFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, thickness);
		bottomFoldUnder.applyTranslation(-1 * FOLD_UNDER_WIDTH, 0);
		translateAndDraw.add(bottomFoldUnder);
		
		/////////
		Rectangle bottom = new Rectangle(SHOULDER_HEIGHT, thickness);
		translateAndDraw.add(bottom);
		
		/////////
		CardBody mainCardBody = new CardBody(WIDTH, SHOULDER_SIZE, PEEK_HEIGHT);
		mainCardBody.applyTranslation(0, bottom.getHeight());
		translateAndDraw.add(mainCardBody);
		
		/////////
		Rectangle top = new Rectangle(SHOULDER_HEIGHT, thickness);
		top.applyTranslation(0, bottom.getHeight() + mainCardBody.getHeight());
		translateAndDraw.add(top);

		/////////
		ScalableText topText = new ScalableText(
			cardName, titleFont,
			top.getWidth() - TEXT_PADDING * 2, 
			top.getHeight() - TEXT_PADDING * 2,
			TextAlignment.CENTER);
		topText.applyTranslation(
			TEXT_PADDING, 
			bottom.getHeight() + mainCardBody.getHeight() + TEXT_PADDING);
		translateAndDraw.add(topText);
		
		/////////
		TopFlap topFlap = new TopFlap(
			SHOULDER_HEIGHT, FLAP_HEIGHT,
			cardCost, cardName,
			coinImage, titleFont);
		topFlap.applyTranslation(0, bottom.getHeight() + mainCardBody.getHeight() + top.getHeight());
		translateAndDraw.add(topFlap);
		
		/////////
		Rectangle topFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, thickness);
		topFoldUnder.applyTranslation(-1 * FOLD_UNDER_WIDTH, bottom.getHeight() + mainCardBody.getHeight());
		translateAndDraw.add(topFoldUnder);
		
		/////////
		Rectangle back = new Rectangle(thickness, WIDTH);
		back.applyTranslation(-1 * thickness, bottom.getHeight());
		translateAndDraw.add(back);
		
		/////////
		float glueWidth = Math.max(EXTERNAL_GLUE_WIDTH, FOLD_UNDER_WIDTH - thickness);
		Rectangle externalGlueArea = new Rectangle(glueWidth, WIDTH);
		externalGlueArea.applyTranslation(-1 * back.getWidth() - externalGlueArea.getWidth(), bottom.getHeight());
		translateAndDraw.add(externalGlueArea);
		
		/////////
		CardBody upsideDownCardBody = new CardBody(WIDTH, SHOULDER_SIZE, PEEK_HEIGHT);
		upsideDownCardBody.applyTranslation(0, -1 * upsideDownCardBody.getHeight());
		translateAndDraw.add(upsideDownCardBody);
		
		/////////
		TopFlap upsideDownTopFlap = new TopFlap(
				SHOULDER_HEIGHT, FLAP_HEIGHT,
				cardCost, cardName,
				coinImage, titleFont);
		upsideDownTopFlap.applyRotation(Math.PI);
		upsideDownTopFlap.applyTranslation(upsideDownTopFlap.getWidth(), -1 * upsideDownCardBody.getHeight());
		translateAndDraw.add(upsideDownTopFlap);
	}
	
	public void draw(PDPageContentStream cStream) throws IOException {
		cStream.setLineWidth(1f);
		cStream.setStrokingColor(Color.BLACK);
		
		ContentStream drawStream = new ContentStream(cStream);
		
		translateAndDraw.forEach(mpo -> {
			mpo.draw(drawStream);
		});
	}

	@Override
	public void applyRotation(double theta) {
		translateAndDraw.forEach(d -> {
			d.applyRotation(theta);
		});
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		translateAndDraw.forEach(d -> {
			d.applyTranslation(deltaX, deltaY);
		});
	}
}
