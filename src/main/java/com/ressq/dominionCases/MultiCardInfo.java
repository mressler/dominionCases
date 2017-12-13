package com.ressq.dominionCases;

import java.util.function.BiConsumer;

import com.ressq.dominionCases.shapes.CardCase;

public class MultiCardInfo {

	public float totalWidth;
	public float totalHeight;
	
	public BiConsumer<Float, Float> applyTranslations;
	
	private MultiCardInfo() {}
	
	public static MultiCardInfo forOneCard(CardCase cardOne) {
		MultiCardInfo forOne = new MultiCardInfo();
		forOne.totalHeight = cardOne.getHeight();
		forOne.totalWidth = cardOne.getWidth();
		
		forOne.applyTranslations = (centerX, centerY) -> {
			cardOne.applyTranslation(
				centerX - forOne.totalWidth / 2 + cardOne.getWidthLeftOfOrigin(), 
				centerY - forOne.totalHeight / 2 + cardOne.getHeightBelowOrigin());
		};
		
		return forOne;
	}
	
	public static MultiCardInfo forTwoCards(CardCase cardOne, CardCase cardTwo) {
		float maxFoldWidth = Math.max(cardOne.getWidthLeftOfOrigin(), cardTwo.getWidthLeftOfOrigin());
		
		MultiCardInfo forTwo = new MultiCardInfo();
		forTwo.totalHeight = cardOne.getHeightAboveOrigin() + cardTwo.getHeightAboveOrigin();
		forTwo.totalWidth = cardOne.getWidthRightOfOrigin() + cardTwo.getWidthRightOfOrigin() + maxFoldWidth;
		
		forTwo.applyTranslations = (centerX, centerY) -> {
			cardOne.applyTranslation(
				centerX - forTwo.totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth,
				centerY + forTwo.totalHeight / 2 - cardOne.getHeightAboveOrigin());
			cardTwo.applyRotation(Math.PI);
			cardTwo.applyTranslation(
				centerX - forTwo.totalWidth / 2 + cardTwo.getWidthRightOfOrigin(), 
				centerY + forTwo.totalHeight / 2 - cardOne.getHeightAboveOrigin());
		};
		
		return forTwo;
	}
	
	public static MultiCardInfo forThreeCards(CardCase cardOne, CardCase cardTwo, CardCase cardThree) {
		MultiCardInfo forThree = new MultiCardInfo();
		
		float maxFoldWidth = Math.max(cardOne.getWidthLeftOfOrigin(), cardTwo.getWidthLeftOfOrigin());
		float widthToRight = Math.max(cardOne.getWidthRightOfOrigin(), cardThree.getHeightAboveOrigin());
		
		forThree.totalWidth = cardTwo.getWidthRightOfOrigin() + maxFoldWidth + widthToRight;
		
		float caseThreeTop = Math.max(
				cardTwo.getHeightAboveOrigin(),
				cardOne.getHeightBelowOrigin() + cardThree.getWidthLeftOfOrigin());
		forThree.totalHeight = cardOne.getHeightAboveOrigin() + caseThreeTop + cardThree.getWidthRightOfOrigin();
		
		forThree.applyTranslations = (centerX, centerY) -> {
			cardOne.applyTranslation(
					centerX - forThree.totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth, 
					centerY + forThree.totalHeight / 2 - cardOne.getHeightAboveOrigin());
			cardTwo.applyRotation(Math.PI);
			cardTwo.applyTranslation(
					centerX - forThree.totalWidth / 2 + cardTwo.getWidthRightOfOrigin(),
					centerY + forThree.totalHeight / 2 - cardOne.getHeightAboveOrigin());
			cardThree.applyRotation(3 * Math.PI / 2);
			cardThree.applyTranslation(
					centerX - forThree.totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth,
					centerY + forThree.totalHeight / 2 - cardOne.getHeightAboveOrigin() - caseThreeTop);
		};
		
		return forThree;
	}
	
}
