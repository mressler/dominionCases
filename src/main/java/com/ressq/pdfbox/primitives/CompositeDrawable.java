package com.ressq.pdfbox.primitives;

import java.util.ArrayList;
import java.util.List;

import com.ressq.pdfbox.helpers.ContentStream;

public abstract class CompositeDrawable implements Drawable {

	private List<Drawable> components;
	
	public CompositeDrawable() {
		components = new ArrayList<>();
	}
	
	protected void add(Drawable component) {
		components.add(component);
	}
	
	@Override
	public void applyRotation(double theta) {
		components.stream().forEach(p -> p.applyRotation(theta));
	}

	@Override
	public void applyTranslation(float deltaX, float deltaY) {
		components.stream().forEach(p -> p.applyTranslation(deltaX, deltaY));
	}

	@Override
	public void draw(ContentStream cStream) {
		components.stream().forEach(p -> p.draw(cStream));
	}

}
