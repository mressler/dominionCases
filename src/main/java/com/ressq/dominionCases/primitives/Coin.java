package com.ressq.dominionCases.primitives;

import java.awt.geom.AffineTransform;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.helpers.ContentStream;

public class Coin implements Drawable {

	private Point imageBottomLeft;
	private double theta;
	
	private float width;
	private float height;
	
	private PDImageXObject coinImage;
	
	public Coin(PDImageXObject coinImage, float width, float height) {
		this.coinImage = coinImage;
		imageBottomLeft = new Point(0, 0);
		theta = 0;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void applyRotation(double theta) {
		imageBottomLeft.applyRotation(theta);
		this.theta += theta;
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		imageBottomLeft.applyTranslation(deltaX, deltaY);
	}

	@Override
	public void draw(ContentStream cStream) {
		AffineTransform imageTransform = 
				new AffineTransform(width, 0, 0, height, imageBottomLeft.getX(), imageBottomLeft.getY());
		imageTransform.rotate(theta);
		
		cStream.drawImage(coinImage, imageTransform);
	}

}
