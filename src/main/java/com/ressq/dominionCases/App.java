package com.ressq.dominionCases;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.ressq.dominionCases.dto.CardDatabase;
import com.ressq.dominionCases.dto.CardInfo;
import com.ressq.dominionCases.shapes.Card;
import com.ressq.dominionCases.shapes.CardCase;
import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.PDFStreamLogger;
import com.ressq.pdfbox.shapes.Rectangle;

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
		Map<Integer, Long> counts = db.getCards().stream().collect(
				Collectors.groupingBy(
						CardInfo::getStandardCount, 
						Collectors.counting()));
		counts.entrySet().stream().forEach(entry -> {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		});
		
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
		
//		do {
//			consumeCards(masterDoc, allCases);
//		} while (!allCases.isEmpty());

		// TODO: groupWith property & front/back text
		// TODO: Second page remainder text that is very short?
		// TODO: Possession text cuts on the second page. What to do about text that cuts on the second and not the first?
		
		// First page
		//pageForThreeCards(masterDoc, db.getCardByName("Village"), db.getCardByName("Moat"), db.getCardByName("Militia"));
		readRegistrationMarks();
		registrationMarks(masterDoc);
		
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
	
	private static void readRegistrationMarks() throws IOException {
		try (PDDocument regsDoc = PDDocument.load(new File("regMarks.pdf"))) {
			int pageNum = 0;
			PDFStreamLogger pdfLogger = new PDFStreamLogger();
			for(PDPage page : regsDoc.getPages()) {
				System.out.println("Processing page " + pageNum);
				pdfLogger.processPage(page);
			}
		}
	}

	private static void registrationMarks(PDDocument masterDoc) throws IOException {
		PDPage helloPage = new PDPage();
		masterDoc.addPage(helloPage);
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		ContentStream drawStream = new ContentStream(cStream);
		
		Rectangle upperLeft = new Rectangle(Card.inchesToPixels(0.20), Card.inchesToPixels(0.20));
		upperLeft.applyTranslation(Card.inchesToPixels(0.5), helloPage.getTrimBox().getHeight() - Card.inchesToPixels(0.5) - upperLeft.getHeight());
		upperLeft.fill(drawStream);
		
		cStream.close();
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
		PDRectangle candidateSize = MultiCardLayout.getSizeFor(cards[0], false);
		MultiCardLayout info;
		if ((candidateSize.getHeight() < trimBox.getHeight()) && 
			(candidateSize.getWidth() < trimBox.getWidth()) &&
			!allCases.isEmpty()) 
		{
			cards[1] = allCases.pop();
			candidateSize = MultiCardLayout.getSizeFor(cards[0], cards[1], false);
			if ((candidateSize.getHeight() < trimBox.getHeight()) && 
				(candidateSize.getWidth() < trimBox.getWidth()) && 
				!allCases.isEmpty()) 
			{
				cards[2] = allCases.pop();
				candidateSize = MultiCardLayout.getSizeFor(cards[0], cards[1], cards[2], false);
				if ((candidateSize.getHeight() < trimBox.getHeight()) && 
					(candidateSize.getWidth() < trimBox.getWidth()) && 
					!allCases.isEmpty()) 
				{
					info = new MultiCardLayout(cards[0], cards[1], cards[2]);
				} else {
					allCases.push(cards[2]);
					info = new MultiCardLayout(cards[0], cards[1]);
				}
			} else {
				allCases.push(cards[1]);
				info = new MultiCardLayout(cards[0]);
			}
		} else {
			if (allCases.isEmpty()) {
				info = new MultiCardLayout(cards[0]);
			} else {
				throw new IllegalArgumentException("Single card stack does not fit!");
			}
		}
		
		info.applyTranslation(centerX, centerY);
		info.draw(drawStream);
		
		cStream.close();
	}

	private static void pageForThreeCards(PDDocument masterDoc, CardInfo cardOne, CardInfo cardTwo, CardInfo cardThree) throws IOException {
		System.out.println("Total card count: " + (cardOne.getStandardCount() + cardTwo.getStandardCount() + cardThree.getStandardCount()));
		
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
		
		MultiCardLayout info = new MultiCardLayout(caseOne);//, caseTwo, caseThree);
		info.applyTranslation(centerX, centerY);

		info.draw(drawStream);
		
		cStream.close();
	}
	
	private static CardCase caseForCardInfo(CardInfo someInfo) {
		return new CardCase(someInfo, imageRepo, trajan, barbedor);
	}
}
