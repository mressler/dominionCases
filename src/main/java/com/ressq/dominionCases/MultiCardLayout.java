package com.ressq.dominionCases;

import java.util.List;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.ressq.dominionCases.shapes.Card;
import com.ressq.dominionCases.shapes.CardCase;
import com.ressq.pdfbox.primitives.CompositeDrawable;

public class MultiCardLayout extends CompositeDrawable {

	public float totalWidth;
	public float totalHeight;
	
	public MultiCardLayout(List<CardCase> cards) {
		switch (cards.size()) {
		case 1:
			getSizeFor(cards.get(0), true);
			add(cards.get(0));
			break;
		case 2:
			getSizeFor(cards.get(0), cards.get(1), true);
			add(cards.get(0));
			add(cards.get(1));
			break;
		case 3:
			getSizeFor(cards.get(0), cards.get(1), cards.get(2), true);
			add(cards.get(0));
			add(cards.get(1));
			add(cards.get(2));
			break;
		default:
			throw new IllegalArgumentException("Invalid number of cards passed to MultiCardLayout: " + cards.size() + " expecting 1, 2, or 3.");
			
		}
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
	
	public static PDRectangle getSizeFor(CardCase cardOne, CardCase cardTwo, boolean applyRotations) {
		float maxFoldWidth = Math.max(cardOne.getWidthLeftOfOrigin(), cardTwo.getWidthLeftOfOrigin());
		
		float totalHeight = cardOne.getHeightAboveOrigin() + cardTwo.getHeightAboveOrigin();
		float totalWidth = cardOne.getWidthRightOfOrigin() + cardTwo.getWidthRightOfOrigin() + maxFoldWidth;
		
		if (applyRotations) {
			cardOne.applyTranslation(
				- totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth + Card.getThicknessFor(10) / 2,
				totalHeight / 2 - cardOne.getHeightAboveOrigin() + Card.getThicknessFor(10) / 2);
			cardTwo.applyRotation(Math.PI);
			cardTwo.applyTranslation(
				- totalWidth / 2 + cardTwo.getWidthRightOfOrigin() - Card.getThicknessFor(10) / 2, 
				totalHeight / 2 - cardOne.getHeightAboveOrigin() - Card.getThicknessFor(10) / 2);
		}
		
		return new PDRectangle(totalWidth, totalHeight);
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
					- totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth + Card.getThicknessFor(10) / 2, 
					totalHeight / 2 - cardOne.getHeightAboveOrigin() + Card.getThicknessFor(10));
			cardTwo.applyRotation(Math.PI);
			cardTwo.applyTranslation(
					- totalWidth / 2 + cardTwo.getWidthRightOfOrigin() - Card.getThicknessFor(10) / 2,
					totalHeight / 2 - cardOne.getHeightAboveOrigin());
			cardThree.applyRotation(3 * Math.PI / 2);
			cardThree.applyTranslation(
					- totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth + Card.getThicknessFor(10) / 2,
					totalHeight / 2 - cardOne.getHeightAboveOrigin() - caseThreeTop - Card.getThicknessFor(10) / 2);
		}
		
		return new PDRectangle(totalWidth, totalHeight);
	}
	
}
