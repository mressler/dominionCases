package com.ressq.dominionCases;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.stream.Collectors;

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
		
		System.out.println("Read " + db.getCards().size() + " cards.");
		
		PDDocument masterDoc = new PDDocument();
		
		// Load resources
		trajan = loadFontResource(masterDoc, "Trajan Pro Regular.ttf");
		barbedor = loadFontResource(masterDoc, "Barbedor Regular.ttf");
		
		imageRepo = new DominionImageRepository(
			loadImageResource(masterDoc, "coin.png"),
			loadImageResource(masterDoc, "potion.png"),
			loadImageResource(masterDoc, "debt.png"),
			loadImageResource(masterDoc, "victory.png"));
		
		LinkedList<CardCase> allCases = db.getCards().stream()
				.map(App::caseForCardInfo)
				.collect(Collectors.toCollection(LinkedList<CardCase>::new));
		
		do {
			consumeCards(masterDoc, allCases);
		} while (!allCases.isEmpty());

		// TODO: Debt costs
		// TODO: groupWith property & front/back text
		// TODO: Second page remainder text that is very short?
		// TODO: Possession text cuts on the second page. What to do about text that cuts on the second and not the first?
		
		// First page
//		pageForThreeCards(masterDoc, db.getCardByName("Triumph"), db.getCards().get(1), db.getCardByName("Fortune"));
		
		
		// Random Recommended set? Only unplayed?
		// Select number of sets to choose from?
		// Landmarks?
		// Events?
		// # of both? Random? 0-2?
		// Use Shelters?
		// Use Platinum/Colony?
		// Always have a multi-action?
		// Banned cards?
		// Allow potion card? If so, how many must there be?
		// Prefer unplayed cards
		// Simulated shuffle and grab 10?
		// Veto Mode
		
		// Played list & when
		// With who?
		
		masterDoc.save("temp.pdf");
		masterDoc.close();
	}
	
	private static void consumeCards(PDDocument masterDoc, LinkedList<CardCase> allCases) throws IOException {
		PDPage helloPage = new PDPage();
		masterDoc.addPage(helloPage);
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		ContentStream drawStream = new ContentStream(cStream);
		
		PDRectangle trimBox = helloPage.getTrimBox();
		float centerX = trimBox.getWidth() / 2 + trimBox.getLowerLeftX();
		float centerY = trimBox.getHeight() / 2 + trimBox.getLowerLeftY();
		
		CardCase cards[] = new CardCase[3];
		cards[0] = allCases.pop();
		PDRectangle candidateSize = MultiCardInfo.getSizeFor(cards[0], false);
		MultiCardInfo info;
		if ((candidateSize.getHeight() < trimBox.getHeight()) && 
			(candidateSize.getWidth() < trimBox.getWidth()) &&
			!allCases.isEmpty()) 
		{
			cards[1] = allCases.pop();
			candidateSize = MultiCardInfo.getSizeFor(cards[0], cards[1], false);
			if ((candidateSize.getHeight() < trimBox.getHeight()) && 
				(candidateSize.getWidth() < trimBox.getWidth()) && 
				!allCases.isEmpty()) 
			{
				cards[2] = allCases.pop();
				candidateSize = MultiCardInfo.getSizeFor(cards[0], cards[1], cards[2], false);
				if ((candidateSize.getHeight() < trimBox.getHeight()) && 
					(candidateSize.getWidth() < trimBox.getWidth()) && 
					!allCases.isEmpty()) 
				{
					info = new MultiCardInfo(cards[0], cards[1], cards[2]);
				} else {
					allCases.push(cards[2]);
					info = new MultiCardInfo(cards[0], cards[1]);
				}
			} else {
				allCases.push(cards[1]);
				info = new MultiCardInfo(cards[0]);
			}
		} else {
			if (allCases.isEmpty()) {
				info = new MultiCardInfo(cards[0]);
			} else {
				throw new IllegalArgumentException("Single card stack does not fit!");
			}
		}
		
		info.applyTranslation(centerX, centerY);
		info.draw(drawStream);
		
		cStream.close();
	}

	private static void pageForThreeCards(PDDocument masterDoc, CardInfo cardOne, CardInfo cardTwo, CardInfo cardThree) throws IOException {
		PDPage helloPage = new PDPage();
		masterDoc.addPage(helloPage);
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		cStream.setLineWidth(1f);
		cStream.setStrokingColor(Color.BLACK);
		ContentStream drawStream = new ContentStream(cStream);
		
		PDRectangle trimBox = helloPage.getTrimBox();
		float centerX = trimBox.getWidth() / 2 + trimBox.getLowerLeftX();
		float centerY = trimBox.getHeight() / 2 + trimBox.getLowerLeftY();
		
		CardCase caseOne = caseForCardInfo(cardOne);
		CardCase caseTwo = caseForCardInfo(cardTwo);
		CardCase caseThree = caseForCardInfo(cardThree);
		
		MultiCardInfo info = new MultiCardInfo(caseOne, caseTwo, caseThree);
		info.applyTranslation(centerX, centerY);

		info.draw(drawStream);
		
		cStream.close();
	}
	
	private static CardCase caseForCardInfo(CardInfo someInfo) {
		return new CardCase(someInfo, imageRepo, trajan, barbedor);
	}
}
