package com.ressq.dominionCases;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

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
	
	private static void pageForThreeCards(PDDocument masterDoc, CardInfo cardOne, CardInfo cardTwo, CardInfo cardThree) throws IOException {
		PDPage helloPage = new PDPage();
		masterDoc.addPage(helloPage);
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		cStream.setLineWidth(1f);
		cStream.setStrokingColor(Color.BLACK);
		ContentStream drawStream = new ContentStream(cStream);
		
		cardTwo.setStandardCount(20);
		
		CardCase caseOne = caseForCardInfo(cardOne);
		CardCase caseTwo = caseForCardInfo(cardTwo);
		CardCase caseThree = caseForCardInfo(cardThree);
		float maxFoldWidth = Math.max(caseOne.getFoldWidth(), caseTwo.getFoldWidth());
		
		PDRectangle trimBox = helloPage.getTrimBox();
		
		float centerX = trimBox.getWidth() / 2 + trimBox.getLowerLeftX();
		float centerY = trimBox.getHeight() / 2 + trimBox.getLowerLeftY() + caseThree.getWidth() / 2;
		
		float cardOneBottomHeight = caseOne.getMainCardHeight() + CardCase.FLAP_HEIGHT;
		float cardTwoTopHeight = caseTwo.getMainCardHeight() + CardCase.FLAP_HEIGHT + 2 * caseTwo.getThickness();
		float verticalOffset = Math.max(
				cardOneBottomHeight + caseThree.getFoldWidth(), 
				cardTwoTopHeight);
		
		caseOne.applyTranslation(centerX, centerY);
		caseOne.draw(drawStream);
		
		caseTwo.applyRotation(Math.PI);
		caseTwo.applyTranslation(centerX - maxFoldWidth, centerY);
		caseTwo.draw(drawStream);
		
		caseThree.applyRotation(3 * Math.PI / 2);
		caseThree.applyTranslation(centerX, centerY - verticalOffset);
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
