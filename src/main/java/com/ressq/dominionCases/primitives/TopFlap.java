package com.ressq.dominionCases.primitives;

import java.awt.geom.AffineTransform;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.helpers.ContentStream;

public class TopFlap extends MultiPointObject {
	
	public static final float COIN_HEIGHT = 20;
	public static final float COIN_WIDTH = 20;

	private PDImageXObject coinImage;
	private Point imageBottomLeft;
	private double theta;
	
	private ScalableText title;
	
	public TopFlap(float width, float height, PDImageXObject coinImage, PDFont titleFont) {
		super(4);
		addPoint(0, 0);
		addPoint(height, height);
		addPoint(width - height, height);
		addPoint(width, 0);
		
		theta = 0;
		
		this.coinImage = coinImage;
		imageBottomLeft = new Point(
			height, 
			(height - COIN_HEIGHT) / 2);
		
		title = new ScalableText(
				"Hello World!", titleFont, 
				width - 2 * height - COIN_WIDTH,
				COIN_HEIGHT);
		title.applyTranslation(height + COIN_WIDTH, (height - COIN_HEIGHT) / 2);
	}
	
	@Override
	public void applyRotation(double theta) {
		super.applyRotation(theta);
		
		imageBottomLeft.applyRotation(theta);
		title.applyRotation(theta);
		
		this.theta += theta;
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		super.applyTranslation(deltaX, deltaY);
		
		imageBottomLeft.applyTranslation(deltaX, deltaY);
		title.applyTranslation(deltaX, deltaY);
	}

	@Override
	public void draw(ContentStream cStream) {
		super.draw(cStream);
		
		AffineTransform imageTransform = 
				new AffineTransform(COIN_WIDTH, 0, 0, COIN_HEIGHT, imageBottomLeft.getX(), imageBottomLeft.getY());
		imageTransform.rotate(theta);
		
		cStream.drawImage(coinImage, imageTransform);
		
		title.draw(cStream);
	}
}
