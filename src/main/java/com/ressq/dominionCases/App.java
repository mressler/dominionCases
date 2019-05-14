package com.ressq.dominionCases;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
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
		
		db.getSetsById().entrySet().forEach(e -> {
			generatePDF(e.getValue().getName() + ".pdf", db.getCardsForDisplay(true, ci -> ci.getSetId().equals(e.getKey()) ));
		});
	}
	
	private static void generatePDF(String fileName, Stream<? extends DisplayableCardInfo> toPrint) {
		PDDocument masterDoc = new PDDocument();
		loadResources(masterDoc);
		
		LinkedList<DisplayableCardInfo> allCardInfos = 
				toPrint
				.sorted((ci1, ci2) -> ci1.getStandardCount() - ci2.getStandardCount())
				.collect(Collectors.toCollection(LinkedList<DisplayableCardInfo>::new));

		// Layout the cards
		PDPage helloPage = new PDPage();
		PDRectangle trimBox = helloPage.getTrimBox();
		List<MultiCardLayout> allLayouts = new LinkedList<MultiCardLayout>();
		do {
			DisplayableCardInfo thisCase = allCardInfos.pop();
			Optional<MultiCardLayout> layoutOpt = layoutForCards(trimBox, allCardInfos, thisCase);
			MultiCardLayout info = layoutOpt.orElseThrow(() -> new IllegalArgumentException("No way to layout. Attempted " + thisCase.getName()));
			allLayouts.add(info);
		} while (!allCardInfos.isEmpty());
		
		// Print the cards - For hand cutting
		allLayouts.stream().forEach(mcl -> {
			printLayout(
				masterDoc, 
				EnumSet.noneOf(DrawOptions.class),
				mcl);
		});
		// Text only for cutter
		allLayouts.stream().forEach(mcl -> {
			printLayout(
				masterDoc, 
				EnumSet.of(DrawOptions.TEXT_ONLY),
				mcl);
		});
		// Layouts for cutter
		allLayouts.stream()
			.distinct()
			.forEach(mcl -> {
			printLayout(
				masterDoc, 
				EnumSet.of(DrawOptions.LINES_NOT_PATHS, DrawOptions.OUTLINE),
				mcl);
		});

		// TODO: Second page remainder text that is very short?
		// TODO: Possession text cuts on the second page. What to do about text that cuts on the second and not the first?
		
		try {
			masterDoc.save(fileName);
			masterDoc.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
	
	private static void loadResources(PDDocument masterDoc) {
		try {
			trajan = loadFontResource(masterDoc, "Trajan Pro Regular.ttf");
			barbedor = loadFontResource(masterDoc, "Barbedor Regular.ttf");
			
			imageRepo = new DominionImageRepository(
				loadImageResource(masterDoc, "coin.png"),
				loadImageResource(masterDoc, "potion.png"),
				loadImageResource(masterDoc, "debt.png"),
				loadImageResource(masterDoc, "victory.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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

	private static void addRegistrationMarksToPage(PDPage somePage, ContentStream drawStream) {
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

	private static void printLayout(
			PDDocument masterDoc, 
			EnumSet<DrawOptions> drawOptions, 
			MultiCardLayout layout)
	{
		PDPage helloPage = new PDPage();
		PDRectangle trimBox = helloPage.getTrimBox();
		
		masterDoc.addPage(helloPage);
		try (ContentStream drawStream = new ContentStream(masterDoc, helloPage)) {
			addRegistrationMarksToPage(helloPage, drawStream);
			
			float centerX = trimBox.getWidth() / 2 + trimBox.getLowerLeftX();
			float centerY = trimBox.getHeight() / 2 + trimBox.getLowerLeftY();
			
			Tuple<Point, Point> objectSize = layout.getBoundingBox();
			float objectCenterX = (objectSize.y.getX() + objectSize.x.getX()) / 2;
			float objectCenterY = (objectSize.y.getY() + objectSize.x.getY()) / 2;
			
			centerX -= objectCenterX;
			centerY -= objectCenterY;
			
			layout.applyTranslation(centerX, centerY);
			layout.draw(drawStream, drawOptions);
		}
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
		return new CardCase(someInfo, imageRepo, trajan, barbedor);
	}
}
