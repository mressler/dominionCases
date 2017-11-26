package com.ressq.pdfbox.primitives;

import com.ressq.pdfbox.helpers.ContentStream;

public interface Drawable extends Transformable {

	public void draw(ContentStream cStream);
	
}
