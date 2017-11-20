package com.ressq.dominionCases.primitives;

import java.io.IOException;

import com.ressq.dominionCases.helpers.ContentStream;

public interface Drawable {

	public void draw(ContentStream cStream) throws IOException;
	
}
