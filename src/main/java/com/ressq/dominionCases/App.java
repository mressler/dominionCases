package com.ressq.dominionCases;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.ressq.dominionCases.dto.CardDatabase;
import com.ressq.dominionCases.dto.CardInfo;
import com.ressq.dominionCases.shapes.CardCase;
import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.text.TextAlignment;

/**
 * Hello world!
 *
 */
public class App {
	
	private static DominionImageRepository imageRepo;
	private static PDFont trajan;
	private static PDFont barbedor;
	
	private static URL getResource(String res) {
		return App.class.getClassLoader().getResource(res);
	}
	
	private static PDFont loadFontResource(PDDocument masterDoc, String res) throws IOException {
		URL trajanUrl = getResource(res);
		return PDType0Font.load(masterDoc, trajanUrl.openStream());
	}
	
	private static PDImageXObject loadImageResource(PDDocument masterDoc, String res) throws IOException {
		URL coinUrl = getResource(res);
		return PDImageXObject.createFromFile(coinUrl.getFile(), masterDoc);
	}
	
	public static void main(String[] args) throws IOException 
	{
		URL cardDBUrl = getResource("dominionCards.json");
		
		ObjectMapper om = new ObjectMapper();
		om.configure(Feature.ALLOW_COMMENTS, true);
		ObjectReader dbReader = om.readerFor(CardDatabase.class);
		CardDatabase db = dbReader.readValue(cardDBUrl.openStream());
		
		PDDocument masterDoc = new PDDocument();
		
		// Load resources
		trajan = loadFontResource(masterDoc, "Trajan Pro Regular.ttf");
		barbedor = loadFontResource(masterDoc, "Barbedor Regular.ttf");
		
		imageRepo = new DominionImageRepository(
			loadImageResource(masterDoc, "coin.png"),
			loadImageResource(masterDoc, "potion.png"),
			loadImageResource(masterDoc, "debt.png"),
			loadImageResource(masterDoc, "victory.png"));
		
		// First page
		pageForThreeCards(masterDoc, db.getCards().get(0), db.getCards().get(1), db.getCards().get(2));
		
		masterDoc.save("temp.pdf");
		masterDoc.close();
	}
	
	public static class MultiCardInfo {
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
				cardTwo.applyTranslation(
						centerX - forThree.totalWidth / 2 + cardTwo.getWidthRightOfOrigin(),
						centerY + forThree.totalHeight / 2 - cardOne.getHeightAboveOrigin());
				cardThree.applyTranslation(
						centerX - forThree.totalWidth / 2 + cardTwo.getWidthRightOfOrigin() + maxFoldWidth,
						centerY + forThree.totalHeight / 2 - cardOne.getHeightAboveOrigin() - caseThreeTop);
			};
			
			return forThree;
		}
	}
	
	private static void pageForThreeCards(PDDocument masterDoc, CardInfo cardOne, CardInfo cardTwo, CardInfo cardThree) throws IOException {
		PDPage helloPage = new PDPage();
		masterDoc.addPage(helloPage);
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		cStream.setLineWidth(1f);
		cStream.setStrokingColor(Color.BLACK);
		ContentStream drawStream = new ContentStream(cStream);
		
		cardOne.setStandardCount(30);
		cardThree.setStandardCount(30);
		
		PDRectangle trimBox = helloPage.getTrimBox();
		float centerX = trimBox.getWidth() / 2 + trimBox.getLowerLeftX();
		float centerY = trimBox.getHeight() / 2 + trimBox.getLowerLeftY();
		
		CardCase caseOne = caseForCardInfo(cardOne);
		CardCase caseTwo = caseForCardInfo(cardTwo);
		CardCase caseThree = caseForCardInfo(cardThree);
		
		float maxFoldWidth = Math.max(caseOne.getWidthLeftOfOrigin(), caseTwo.getWidthLeftOfOrigin());
		float widthToRight = Math.max(caseOne.getWidthRightOfOrigin(), caseThree.getHeightAboveOrigin());
		
		float totalWidth = caseTwo.getWidthRightOfOrigin() + maxFoldWidth + widthToRight;
		
		float caseThreeTop = Math.max(
				caseTwo.getHeightAboveOrigin(),
				caseOne.getHeightBelowOrigin() + caseThree.getWidthLeftOfOrigin());
		float totalHeight = caseOne.getHeightAboveOrigin() + caseThreeTop + caseThree.getWidthRightOfOrigin();
		
		caseOne.applyTranslation(
				centerX - totalWidth / 2 + caseTwo.getWidthRightOfOrigin() + maxFoldWidth, 
				centerY + totalHeight / 2 - caseOne.getHeightAboveOrigin());
		caseOne.draw(drawStream);
		
		caseTwo.applyRotation(Math.PI);
		caseTwo.applyTranslation(
				centerX - totalWidth / 2 + caseTwo.getWidthRightOfOrigin(),
				centerY + totalHeight / 2 - caseOne.getHeightAboveOrigin());
		caseTwo.draw(drawStream);
		
		caseThree.applyRotation(3 * Math.PI / 2);
		caseThree.applyTranslation(
				centerX - totalWidth / 2 + caseTwo.getWidthRightOfOrigin() + maxFoldWidth,
				centerY + totalHeight / 2 - caseOne.getHeightAboveOrigin() - caseThreeTop);
		caseThree.draw(drawStream);
		
		cStream.close();
	}
	
	private static CardCase caseForCardInfo(CardInfo someInfo) {
		return new CardCase(
			someInfo.getStandardCount(),
			someInfo.getCost(), someInfo.getName(),
			someInfo.getErrata(),
			imageRepo, trajan, barbedor);
	}
}
