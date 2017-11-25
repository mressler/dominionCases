package com.ressq.dominionCases;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.helpers.ContentStream;
import com.ressq.dominionCases.primitives.CardBody;
import com.ressq.dominionCases.primitives.MultiPointObject;
import com.ressq.dominionCases.primitives.Rectangle;
import com.ressq.dominionCases.primitives.TopFlap;

public class CardCase {

	public static final float PEEK_HEIGHT = Card.HEIGHT - 20f;
	public static final float SHOULDER_SIZE = 40f;
	public static final float SHOULDER_HEIGHT = PEEK_HEIGHT - SHOULDER_SIZE;
	public static final float FOLD_UNDER_WIDTH = 40f;
	public static final float EXTERNAL_GLUE_WIDTH = 20f;
	public static final float FLAP_HEIGHT = 25f;
	
	private float centerX;
	private float centerY;
	private float rotationDegrees;
	
	private int cardCost;
	private String cardName;
	private int cardCount;
	private String description;
	
	private PDImageXObject coinImage;
	private PDFont titleFont;
	
	public CardCase(
			float centerX, float centerY, 
			int cardCount, 
			PDImageXObject coinImage, PDFont titleFont) 
	{
		this.centerX = centerX;
		this.centerY = centerY;
		this.cardCount = cardCount;
		this.coinImage = coinImage;
		
		this.titleFont = titleFont;
	}
	
	public void draw(PDPageContentStream cStream) throws IOException {
		cStream.setLineWidth(1f);
		cStream.setStrokingColor(Color.BLACK);
		
		float thickness = Card.getThicknessFor(cardCount);
		ContentStream drawStream = new ContentStream(cStream);
		List<MultiPointObject> translateAndDraw = new ArrayList<>();
		
		/////////
		Rectangle bottomFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, thickness);
		bottomFoldUnder.applyTranslation(-1 * FOLD_UNDER_WIDTH, 0);
		translateAndDraw.add(bottomFoldUnder);
		
		/////////
		Rectangle bottom = new Rectangle(SHOULDER_HEIGHT, thickness);
		translateAndDraw.add(bottom);
		
		/////////
		CardBody mainCardBody = new CardBody(Card.WIDTH, SHOULDER_SIZE, PEEK_HEIGHT);
		mainCardBody.applyTranslation(0, bottom.getHeight());
		translateAndDraw.add(mainCardBody);
		
		/////////
		Rectangle top = new Rectangle(SHOULDER_HEIGHT, thickness);
		top.applyTranslation(0, bottom.getHeight() + mainCardBody.getHeight());
		translateAndDraw.add(top);
		
		/////////
		TopFlap topFlap = new TopFlap(SHOULDER_HEIGHT, FLAP_HEIGHT, coinImage, titleFont);
		topFlap.applyTranslation(0, bottom.getHeight() + mainCardBody.getHeight() + top.getHeight());
		translateAndDraw.add(topFlap);
		
		/////////
		Rectangle topFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, thickness);
		topFoldUnder.applyTranslation(-1 * FOLD_UNDER_WIDTH, bottom.getHeight() + mainCardBody.getHeight());
		translateAndDraw.add(topFoldUnder);
		
		/////////
		Rectangle back = new Rectangle(thickness, Card.WIDTH);
		back.applyTranslation(-1 * thickness, bottom.getHeight());
		translateAndDraw.add(back);
		
		/////////
		Rectangle backFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, Card.WIDTH);
		backFoldUnder.applyTranslation(-1 * back.getWidth() - backFoldUnder.getWidth(), bottom.getHeight());
		translateAndDraw.add(backFoldUnder);
		
		/////////
		CardBody upsideDownCardBody = new CardBody(Card.WIDTH, SHOULDER_SIZE, PEEK_HEIGHT);
		upsideDownCardBody.applyTranslation(0, -1 * upsideDownCardBody.getHeight());
		translateAndDraw.add(upsideDownCardBody);
		
		/////////
		TopFlap upsideDownTopFlap = new TopFlap(SHOULDER_HEIGHT, FLAP_HEIGHT, coinImage, titleFont);
		upsideDownTopFlap.applyRotation(Math.PI);
		upsideDownTopFlap.applyTranslation(upsideDownTopFlap.getWidth(), -1 * upsideDownCardBody.getHeight());
		translateAndDraw.add(upsideDownTopFlap);
		
		translateAndDraw.forEach(mpo -> {
			mpo.applyTranslation(centerX, centerY);
			mpo.draw(drawStream);
		});
		
	}
}
