package com.ressq.dominionCases;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import com.ressq.dominionCases.dto.DisplayableCardInfo;
import com.ressq.dominionCases.shapes.Card;
import com.ressq.dominionCases.shapes.CardCase;
import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.helpers.PDFStreamLogger;
import com.ressq.pdfbox.helpers.Tuple;
import com.ressq.pdfbox.primitives.Point;
import com.ressq.pdfbox.shapes.Rectangle;

/**
 * 
 */
public class App {
	
	private static DominionImageRepository imageRepo;
	private static PDFont trajan;
	private static PDFont barbedor;
	private static final double REGISTRATION_INSET = 0.4;
	
	public static void main(String[] args) throws IOException 
	{
		CardDatabase db = readDatabase();
		PDDocument masterDoc = new PDDocument();
		loadResources(masterDoc);
		
		Set<Integer> setIds = new HashSet<>(Arrays.asList(11, 12, 13, 14, 15, 16));
		Set<String> cardNames = new HashSet<>(Arrays.asList("Tracker", "Pixie", "Shepherd"));
		
		LinkedList<DisplayableCardInfo> allCardInfos = db.getCardsForDisplay(true, ci -> setIds.contains(ci.getSetId()) )
				.filter(dci -> cardNames.contains(dci.getName()))
				.sorted((ci1, ci2) -> ci1.getStandardCount() - ci2.getStandardCount())
				.collect(Collectors.toCollection(LinkedList<DisplayableCardInfo>::new));
		
		do {
			consumeCards(masterDoc, allCardInfos);
		} while (!allCardInfos.isEmpty());

		// TODO: Second page remainder text that is very short?
		// TODO: Possession text cuts on the second page. What to do about text that cuts on the second and not the first?
		
		masterDoc.save("temp.pdf");
		masterDoc.close();
	}
	

	// THOUGHTS ON RANDOMINION IMPL
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
	// Pick which cards you WANT to have in, randomize the rest
	// Never want to have in
	
	// Played list & when
	// With who?
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                       Helper Methods
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
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
	
	private static CardDatabase readDatabase() throws IOException {
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
		
		db.getCompoundCards().stream()
			.forEach(System.out::println);
		
		return db;
	}
	
	private static void loadResources(PDDocument masterDoc) throws IOException {
		trajan = loadFontResource(masterDoc, "Trajan Pro Regular.ttf");
		barbedor = loadFontResource(masterDoc, "Barbedor Regular.ttf");
		
		imageRepo = new DominionImageRepository(
			loadImageResource(masterDoc, "coin.png"),
			loadImageResource(masterDoc, "potion.png"),
			loadImageResource(masterDoc, "debt.png"),
			loadImageResource(masterDoc, "victory.png"));
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
		double squareWidth = 0.2;
		double regLength = 0.5;
		double inset = REGISTRATION_INSET;
		
		Rectangle upperLeft = new Rectangle(Card.inchesToPixels(squareWidth), Card.inchesToPixels(squareWidth));
		upperLeft.applyTranslation(
				Card.inchesToPixels(inset), 
				somePage.getTrimBox().getHeight() - Card.inchesToPixels(inset) - upperLeft.getHeight());
		upperLeft.fill(drawStream);
		
		Rectangle upperRightH = new Rectangle(Card.inchesToPixels(regLength), 1);
		upperRightH.applyTranslation(
				somePage.getTrimBox().getWidth() - Card.inchesToPixels(inset) - upperRightH.getWidth(), 
				somePage.getTrimBox().getHeight() - Card.inchesToPixels(inset) - upperRightH.getHeight());
		upperRightH.fill(drawStream);
		
		Rectangle upperRightV = new Rectangle(1, Card.inchesToPixels(regLength));
		upperRightV.applyTranslation(
				somePage.getTrimBox().getWidth() - Card.inchesToPixels(inset) - upperRightV.getWidth(), 
				somePage.getTrimBox().getHeight() - Card.inchesToPixels(inset) - upperRightV.getHeight());
		upperRightV.fill(drawStream);
		
		Rectangle lowerLeftH = new Rectangle(Card.inchesToPixels(regLength), 1);
		lowerLeftH.applyTranslation(
				Card.inchesToPixels(inset), 
				Card.inchesToPixels(inset));
		lowerLeftH.fill(drawStream);
		
		Rectangle lowerLeftV = new Rectangle(1, Card.inchesToPixels(regLength));
		lowerLeftV.applyTranslation(
				Card.inchesToPixels(inset), 
				Card.inchesToPixels(inset));
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

	private static void consumeCards(PDDocument masterDoc, LinkedList<DisplayableCardInfo> allCases) throws IOException {
		PDPage helloPage = new PDPage();
		PDRectangle trimBox = helloPage.getTrimBox();
		
		DisplayableCardInfo thisCase = allCases.pop();
		Optional<MultiCardLayout> layoutOpt = layoutForCards(trimBox, allCases, thisCase);
		MultiCardLayout info = layoutOpt.orElseThrow(() -> new IllegalArgumentException("No way to layout. Attempted " + thisCase.getName()));
		
		masterDoc.addPage(helloPage);
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		ContentStream drawStream = new ContentStream(cStream);
		
		addRegistrationMarksToPage(helloPage, drawStream);
		
		float centerX = trimBox.getWidth() / 2 + trimBox.getLowerLeftX();
		float centerY = trimBox.getHeight() / 2 + trimBox.getLowerLeftY();
		
		Tuple<Point, Point> objectSize = info.getBoundingBox();
		float objectCenterX = (objectSize.y.getX() + objectSize.x.getX()) / 2;
		float objectCenterY = (objectSize.y.getY() + objectSize.x.getY()) / 2;
		
		centerX -= objectCenterX;
		centerY -= objectCenterY;
		
		info.applyTranslation(centerX, centerY);
		info.draw(drawStream);
		
		cStream.close();
	}
	
	private static Optional<MultiCardLayout> layoutForCards(
			PDRectangle trimBox, 
			LinkedList<DisplayableCardInfo> allCases, 
			DisplayableCardInfo... cardsInLayout) 
	{
		Optional<MultiCardLayout> foundLayout = Optional.empty();
		// Can I take another? If so, take one and lay out additional cards
		if ((cardsInLayout.length < 3) && !allCases.isEmpty()) {
			DisplayableCardInfo nextCard = allCases.pop();
			
			foundLayout = layoutForCards(trimBox, allCases, 
					Stream.concat(Stream.of(cardsInLayout), Stream.of(nextCard)).toArray(DisplayableCardInfo[]::new)); // Admittedly a bit verbose to append ONE element to an array
			
			// If this wasn't successful, then put that card back to be layed out by a later operation
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
			
			if ((maxCardLayout.getWidth() < trimBox.getWidth() - Card.inchesToPixels(REGISTRATION_INSET) * 2) && 
				(maxCardLayout.getHeight() < trimBox.getHeight() - Card.inchesToPixels(REGISTRATION_INSET) * 2)) 
			{
				return Optional.of(maxCardLayout);
			} else {
				return Optional.empty();
			}
		}
		
		// Ultimately report what we found
		return foundLayout;
	}
	
	private static CardCase caseForCardInfo(DisplayableCardInfo someInfo) {
		return new CardCase(someInfo, imageRepo, trajan, barbedor, EnumSet.noneOf(DrawOptions.class)); //EnumSet.of(DrawOptions.LINES_NOT_PATHS, DrawOptions.OUTLINE)); //EnumSet.of(DrawOptions.TEXT_ONLY)); //
	}
}
