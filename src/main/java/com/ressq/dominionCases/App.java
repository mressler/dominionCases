package com.ressq.dominionCases;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		
		db.postConstruct();
		
		PDDocument masterDoc = new PDDocument();
		
		// Load resources
		trajan = loadFontResource(masterDoc, "Trajan Pro Regular.ttf");
		barbedor = loadFontResource(masterDoc, "Barbedor Regular.ttf");
		
		imageRepo = new DominionImageRepository(
			loadImageResource(masterDoc, "coin.png"),
			loadImageResource(masterDoc, "potion.png"),
			loadImageResource(masterDoc, "debt.png"),
			loadImageResource(masterDoc, "victory.png"));
		
		LinkedList<CardInfo> allCardInfos = db.getCards().stream()
				.collect(Collectors.toCollection(LinkedList<CardInfo>::new));
		
		do {
			consumeCards(masterDoc, allCardInfos);
		} while (!allCardInfos.isEmpty());

		// TODO: groupWith property & front/back text
		// TODO: Second page remainder text that is very short?
		// TODO: Possession text cuts on the second page. What to do about text that cuts on the second and not the first?
		
		// First page
//		pageForCards(
//			masterDoc,
//			db.getCardByName("Village"), 
//			db.getCardByName("Moat"), 
//			db.getCardByName("Militia")
//		);
		
		//readRegistrationMarks();
		
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
	
	@SuppressWarnings("unused")
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

	private static void addRegistrationMarksToPage(PDPage somePage, ContentStream drawStream) throws IOException {
		Rectangle upperLeft = new Rectangle(Card.inchesToPixels(0.20), Card.inchesToPixels(0.20));
		upperLeft.applyTranslation(
				Card.inchesToPixels(0.5), 
				somePage.getTrimBox().getHeight() - Card.inchesToPixels(0.5) - upperLeft.getHeight());
		upperLeft.fill(drawStream);
		
		Rectangle upperRightH = new Rectangle(Card.inchesToPixels(0.50), 1);
		upperRightH.applyTranslation(
				somePage.getTrimBox().getWidth() - Card.inchesToPixels(0.5) - upperRightH.getWidth(), 
				somePage.getTrimBox().getHeight() - Card.inchesToPixels(0.5) - upperRightH.getHeight());
		upperRightH.fill(drawStream);
		
		Rectangle upperRightV = new Rectangle(1, Card.inchesToPixels(0.50));
		upperRightV.applyTranslation(
				somePage.getTrimBox().getWidth() - Card.inchesToPixels(0.5) - upperRightV.getWidth(), 
				somePage.getTrimBox().getHeight() - Card.inchesToPixels(0.5) - upperRightV.getHeight());
		upperRightV.fill(drawStream);
		
		Rectangle lowerLeftH = new Rectangle(Card.inchesToPixels(0.50), 1);
		lowerLeftH.applyTranslation(
				Card.inchesToPixels(0.5), 
				Card.inchesToPixels(0.5));
		lowerLeftH.fill(drawStream);
		
		Rectangle lowerLeftV = new Rectangle(1, Card.inchesToPixels(0.50));
		lowerLeftV.applyTranslation(
				Card.inchesToPixels(0.5), 
				Card.inchesToPixels(0.5));
		lowerLeftV.fill(drawStream);
	}
	
	@SuppressWarnings("unused")
	private static void pageForCards(PDDocument masterDoc, CardInfo... allCards) throws IOException {
		consumeCards(masterDoc, 
			Arrays.asList(allCards).stream()
				.collect(Collector.of(
					LinkedList::new, 
					List::add,
					(left, right) -> { left.addAll(right); return left; }
				)
			)
		);
	}

	private static void consumeCards(PDDocument masterDoc, LinkedList<CardInfo> allCases) throws IOException {
		PDPage helloPage = new PDPage();
		PDRectangle trimBox = helloPage.getTrimBox();
		
		Optional<MultiCardLayout> layoutOpt = layoutForCards(trimBox, allCases, allCases.pop());
		MultiCardLayout info = layoutOpt.orElseThrow(() -> new IllegalArgumentException("No way to layout"));
		
		masterDoc.addPage(helloPage);
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		ContentStream drawStream = new ContentStream(cStream);
		
		addRegistrationMarksToPage(helloPage, drawStream);
		
		float centerX = trimBox.getWidth() / 2 + trimBox.getLowerLeftX();
		float centerY = trimBox.getHeight() / 2 + trimBox.getLowerLeftY();
		
		info.applyTranslation(centerX, centerY);
		info.draw(drawStream);
		
		cStream.close();
	}
	
	private static Optional<MultiCardLayout> layoutForCards(
			PDRectangle trimBox, 
			LinkedList<CardInfo> allCases, 
			CardInfo... cardsInLayout) 
	{
		Optional<MultiCardLayout> foundLayout = Optional.empty();
		// Can I take another? If so, take one and lay out additional cards
		if ((cardsInLayout.length < 3) && !allCases.isEmpty()) {
			CardInfo nextCard = allCases.pop();
			
			foundLayout = layoutForCards(trimBox, allCases, 
					Stream.concat(Stream.of(cardsInLayout), Stream.of(nextCard)).toArray(CardInfo[]::new)); // Admittedly a bit verbose to append ONE element to an array
			
			if (!foundLayout.isPresent()) {
				allCases.push(nextCard);
			}
		}
		
		// Lay out the current number of cards
		if (!foundLayout.isPresent()) {
			// Attempt to layout with the current number of cards.
			MultiCardLayout maxCardLayout = new MultiCardLayout(
				Arrays.asList(cardsInLayout).stream()
					.map(App::caseForCardInfo)
					.collect(Collectors.toList()));
			
			if ((maxCardLayout.getWidth() < trimBox.getWidth()) && 
				(maxCardLayout.getHeight() < trimBox.getHeight())) 
			{
				return Optional.of(maxCardLayout);
			} else {
				return Optional.empty();
			}
		}
		
		// Ultimately report what we found
		return foundLayout;
	}
	
	private static CardCase caseForCardInfo(CardInfo someInfo) {
		return new CardCase(someInfo, imageRepo, trajan, barbedor);
	}
}
