package com.ressq.dominionCases;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.ressq.dominionCases.shapes.CardCase;
import com.ressq.pdfbox.primitives.CompositeDrawable;

public class MultiCardLayout extends CompositeDrawable {

	public float totalWidth;
	public float totalHeight;
	
	public MultiCardLayout(CardCase cardOne) {
		PDRectangle sizeOf = getSizeFor(cardOne, true);
		totalHeight = sizeOf.getHeight();
		totalWidth = sizeOf.getWidth();
		
		add(cardOne);
	}
	
	public static PDRectangle getSizeFor(CardCase cardOne, boolean applyRotations) {
		float totalHeight = cardOne.getHeight();
		float totalWidth = cardOne.getWidth();
		
		if (applyRotations) {
			cardOne.applyTranslation(
				- totalWidth / 2 + cardOne.getWidthLeftOfOrigin(), 
				- totalHeight / 2 + cardOne.getHeightBelowOrigin());
		}
		
		return new PDRectangle(totalWidth, totalHeight);
	}
	
	public MultiCardLayout(CardCase cardOne, CardCase cardTwo) {
		PDRectangle sizeOf = getSizeFor(cardOne, cardTwo, true);
		totalHeight = sizeOf.getHeight();
		totalWidth = sizeOf.getWidth();
		
		add(cardOne);
		add(cardTwo);
	}
	
	public static PDRectangle getSizeFor(CardCase cardOne, CardCase cardTwo, boolean applyRotations) {
		float maxFoldWidth = Math.max(cardOne.getWidthLeftOfOrigin(), cardTwo.getWidthLeftOfOrigin());
		
		float totalHeight = cardOne.getHeightAboveOrigin() + cardTwo.getHeightAboveOrigin();
		float totalWidth = cardOne.getWidthRightOfOrigin() + cardTwo.getWidthRightOfOrigin() + maxFoldWidth;
		
		if (applyRotations) {
			cardOne.applyTranslation(
				- totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth,
				totalHeight / 2 - cardOne.getHeightAboveOrigin());
			cardTwo.applyRotation(Math.PI);
			cardTwo.applyTranslation(
				- totalWidth / 2 + cardTwo.getWidthRightOfOrigin(), 
				totalHeight / 2 - cardOne.getHeightAboveOrigin());
		}
		
		return new PDRectangle(totalWidth, totalHeight);
	}
	
	public MultiCardLayout(CardCase cardOne, CardCase cardTwo, CardCase cardThree) {
		PDRectangle sizeOf = getSizeFor(cardOne, cardTwo, cardThree, true);
		totalHeight = sizeOf.getHeight();
		totalWidth = sizeOf.getWidth();
		
		add(cardOne);
		add(cardTwo);
		add(cardThree);
	}
	
	public static PDRectangle getSizeFor(CardCase cardOne, CardCase cardTwo, CardCase cardThree, boolean applyRotations) {
		float maxFoldWidth = Math.max(cardOne.getWidthLeftOfOrigin(), cardTwo.getWidthLeftOfOrigin());
		float widthToRight = Math.max(cardOne.getWidthRightOfOrigin(), cardThree.getHeightAboveOrigin());
		
		float totalWidth = cardTwo.getWidthRightOfOrigin() + maxFoldWidth + widthToRight;
		
		float caseThreeTop = Math.max(
				cardTwo.getHeightAboveOrigin(),
				cardOne.getHeightBelowOrigin() + cardThree.getWidthLeftOfOrigin());
		float totalHeight = cardOne.getHeightAboveOrigin() + caseThreeTop + cardThree.getWidthRightOfOrigin();
		
		if (applyRotations) {
			cardOne.applyTranslation(
					- totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth, 
					totalHeight / 2 - cardOne.getHeightAboveOrigin());
			cardTwo.applyRotation(Math.PI);
			cardTwo.applyTranslation(
					- totalWidth / 2 + cardTwo.getWidthRightOfOrigin(),
					totalHeight / 2 - cardOne.getHeightAboveOrigin());
			cardThree.applyRotation(3 * Math.PI / 2);
			cardThree.applyTranslation(
					- totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth,
					totalHeight / 2 - cardOne.getHeightAboveOrigin() - caseThreeTop);
		}
		
		return new PDRectangle(totalWidth, totalHeight);
	}

	@Override
	public float getHeight() {
		return totalWidth;
	}

	@Override
	public float getWidth() {
		return totalHeight;
	}
	
}
