package com.ressq.pdfbox.primitives;

import java.util.EnumSet;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.DrawOptions;

public class Image extends BottomLeftAware {
	
	private float width;
	private float height;
	
	private PDImageXObject image;
	
	public Image(PDImageXObject image, float height) {
		this.image = image;
		this.height = height;
		this.width = height * image.getWidth() / image.getHeight();
	}

	@Override
	public void draw(ContentStream cStream, EnumSet<DrawOptions> drawOptions) {
		if (drawOptions.contains(DrawOptions.OUTLINE)) {
			return;
		}
		
		cStream.drawImage(image, theta, bottomLeft.getX(), bottomLeft.getY(), width, height);
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
