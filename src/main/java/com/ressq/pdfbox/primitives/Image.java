package com.ressq.pdfbox.primitives;

import java.awt.geom.AffineTransform;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.pdfbox.helpers.ContentStream;

public class Image extends BottomLeftAware {
	
	private float width;
	private float height;
	
	private PDImageXObject coinImage;
	
	public Image(PDImageXObject coinImage, float width, float height) {
		this.coinImage = coinImage;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(ContentStream cStream) {
		AffineTransform imageTransform = 
				new AffineTransform(width, 0, 0, height, bottomLeft.getX(), bottomLeft.getY());
		imageTransform.rotate(theta);
		
		cStream.drawImage(coinImage, imageTransform);
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getWidth() {
		return width;
	}

}
