package com.ressq.dominionCases.shapes;

import static com.ressq.dominionCases.shapes.Card.HEIGHT;
import static com.ressq.dominionCases.shapes.Card.WIDTH;
import static com.ressq.dominionCases.shapes.Card.getThicknessFor;
import static com.ressq.dominionCases.shapes.Card.inchesToPixels;

import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.primitives.Drawable;
import com.ressq.pdfbox.primitives.Image;
import com.ressq.pdfbox.primitives.Line;
import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.dominionCases.DominionImageRepository;
import com.ressq.dominionCases.dto.CardInfo;
import com.ressq.dominionCases.dto.DisplayableCardInfo;
import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.shapes.Rectangle;
import com.ressq.pdfbox.text.MultiLineText;
import com.ressq.pdfbox.text.ScalableText;
import com.ressq.pdfbox.text.TextAlignment;

import java.util.*;
import java.util.stream.Collectors;

public class  CardCase extends CompositeDrawable {

	public static final float PEEK_HEIGHT = HEIGHT;
	public static final float SHOULDER_SIZE = inchesToPixels(0.4);
	public static final float SHOULDER_HEIGHT = PEEK_HEIGHT - SHOULDER_SIZE;
	public static final float FOLD_UNDER_WIDTH = inchesToPixels(0.5);
	public static final float EXTERNAL_GLUE_WIDTH = inchesToPixels(0.3);
	public static final float FLAP_HEIGHT = inchesToPixels(0.3);
	public static final float TEXT_PADDING = 2.5f;
	
	private CardBody mainCardBody;
	private float foldWidth;
	private float thickness;
	private Integer cardCount;
	private ScalableText cardCountText;

	public CardCase(
			DisplayableCardInfo cardInfo, 
			DominionImageRepository imageRepo, PDFont titleFont, PDFont contentFont) 
	{
		super();
		
		cardCount = cardInfo.getStandardCount();
		thickness = getThicknessFor(cardCount);
		float glueWidth = Math.max(EXTERNAL_GLUE_WIDTH, FOLD_UNDER_WIDTH - thickness);
		
		/////////
		Rectangle bottomFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, thickness);
		bottomFoldUnder.applyTranslation(-1 * FOLD_UNDER_WIDTH, 0);
		add(bottomFoldUnder);
		
		/////////
		Rectangle bottom = new Rectangle(SHOULDER_HEIGHT, thickness);
		add(bottom);
		
		/////////
		mainCardBody = new CardBody(WIDTH, SHOULDER_SIZE, PEEK_HEIGHT);
		add(mainCardBody);

		///////// Front & Back Text
		MultiLineText mainText = new MultiLineText(
			cardInfo.getErrata(), contentFont, 14, 7, 
			mainCardBody, imageRepo);
		mainText.applyTranslation(0, bottom.getHeight());
		add(mainText);
		
		/////////
		String backText = mainText.getRemainingText();
		if (backText == null) {
			backText = cardInfo.getSecondaryCardInfo()
					.map(CardInfo::getErrata)
					.orElseGet(cardInfo::getErrata);
		}
		
		Rectangle workableArea2 = new Rectangle(SHOULDER_HEIGHT - glueWidth, mainCardBody.getHeight());
		MultiLineText secondaryText = new MultiLineText(
			backText, 
			contentFont, mainText.getUsedFontSize(), 7, 
			workableArea2, imageRepo);
		secondaryText.applyRotation(Math.PI);
		secondaryText.applyTranslation(secondaryText.getWidth() + glueWidth, 0);
		add(secondaryText);
		
		// Now apply the CardBody translation since we had used it for the MultiLineText
		mainCardBody.applyTranslation(0, bottom.getHeight());
		
		/////////
		Rectangle top = new Rectangle(SHOULDER_HEIGHT, thickness);
		top.applyTranslation(0, bottom.getHeight() + mainCardBody.getHeight());
		add(top);

		/////////
		String topTextName = cardInfo.getName();
		if (cardInfo.getSecondaryCardInfo().isPresent()) {
			topTextName += " / " + cardInfo.getSecondaryCardInfo().get().getName();
		}
		
		ScalableText topText = new ScalableText(
			topTextName, titleFont,
			top.getWidth() - TEXT_PADDING * 2, 
			getThicknessFor(10) - TEXT_PADDING * 2, top.getHeight() - TEXT_PADDING * 2,
			TextAlignment.CENTER, TextAlignment.CENTER);
		topText.applyTranslation(
			TEXT_PADDING, 
			bottom.getHeight() + mainCardBody.getHeight() + TEXT_PADDING);
		add(topText);

		List<Image> setImages = cardInfo.getCardSets().stream()
				.map(imageRepo::getExpansion)
				.map(pdx -> new Image(pdx, topText.getHeight()))
				.collect(Collectors.toList());

		float setSpaceUsed = 0;
		for (int i = 0; i < setImages.size(); i++) {
			Image setImage = setImages.get(i);
			setImage.moveTo(top);
			setImage.applyTranslation(
					TextAlignment.RIGHT.getStartingX(top.getWidth(), setImage.getWidth()) - TEXT_PADDING,
					TextAlignment.CENTER.getStartingX(top.getHeight(), setImage.getHeight())
			);
			setImage.applyTranslation(-1 * setSpaceUsed, 0);
			setSpaceUsed += setImage.getWidth() + TEXT_PADDING;
			add(setImage);
		}
		
		/////////
		TopFlap topFlap = new TopFlap(
			SHOULDER_HEIGHT, FLAP_HEIGHT,
			cardInfo, imageRepo, titleFont);
		topFlap.applyTranslation(0, bottom.getHeight() + mainCardBody.getHeight() + top.getHeight());
		add(topFlap);

		/////////
		Rectangle topFoldUnder = new Rectangle(FOLD_UNDER_WIDTH, thickness);
		topFoldUnder.applyTranslation(-1 * FOLD_UNDER_WIDTH, bottom.getHeight() + mainCardBody.getHeight());
		add(topFoldUnder);
		
		/////////
		Rectangle back = new Rectangle(thickness, WIDTH);
		back.applyTranslation(-1 * thickness, bottom.getHeight());
		add(back);
		
		cardCountText = new ScalableText(
				cardInfo.getStandardCount().toString(), titleFont,
				WIDTH,
				thickness - TEXT_PADDING * 2 - (this.cardCount / 10.0f), // Bigger text needs more padding
				thickness, TextAlignment.CENTER, TextAlignment.CENTER);
		cardCountText.disregardDrawOptions();
		cardCountText.applyRotation(Math.PI / 2);
		cardCountText.applyTranslation(0, bottom.getHeight());
		add(cardCountText);

		/////////
		Rectangle externalGlueArea = new Rectangle(glueWidth, WIDTH);
		externalGlueArea.applyTranslation(-1 * back.getWidth() - externalGlueArea.getWidth(), bottom.getHeight());
		add(externalGlueArea);
		
		foldWidth = glueWidth + thickness;

		/////////
		CardBody upsideDownCardBody = new CardBody(WIDTH, SHOULDER_SIZE, PEEK_HEIGHT);
		add(upsideDownCardBody);

		// Now apply the CardBody translation since we are finished with it
		upsideDownCardBody.applyTranslation(0, -1 * upsideDownCardBody.getHeight());

		/////////
		DisplayableCardInfo backTopFlapCard;
		if (cardInfo.getSecondaryCardInfo().isPresent()) {
			backTopFlapCard = cardInfo.getSecondaryCardInfo().get();
		} else {
			backTopFlapCard = cardInfo;
		}
		
		TopFlap upsideDownTopFlap = new TopFlap(
				SHOULDER_HEIGHT, FLAP_HEIGHT,
				backTopFlapCard, imageRepo, titleFont);
		upsideDownTopFlap.applyRotation(Math.PI);
		upsideDownTopFlap.applyTranslation(upsideDownTopFlap.getWidth(), -1 * upsideDownCardBody.getHeight());
		add(upsideDownTopFlap);
	}

	@Override
	public void draw(ContentStream cStream, EnumSet<DrawOptions> drawOptions) {
		if (!drawOptions.contains(DrawOptions.LINES_NOT_PATHS)) {
			super.draw(cStream, drawOptions);
		}

		cardCountText.draw(cStream, drawOptions);

		// Find the minimum number of lines from all child objects.
		List<Line> allLines = components.stream()
				.flatMap(Drawable::getLines)
				.sorted(Comparator.comparingDouble(Line::getLength).reversed())
				.collect(Collectors.toList());
		Set<Line> drawnLines = new HashSet<>();
		for (Line someLine : allLines) {
			if (drawnLines.stream().noneMatch(l -> l.contains(someLine))) {
				someLine.draw(cStream, drawOptions);
				drawnLines.add(someLine);
			}
		}
	}

	@Override
	public float getHeight() {
		return getHeightAboveOrigin() + getHeightBelowOrigin();
	}

	@Override
	public float getWidth() {
		return getWidthLeftOfOrigin() + getWidthRightOfOrigin();
	}
	
	public float getHeightAboveOrigin() {
		return mainCardBody.getHeight() + 2 * thickness + FLAP_HEIGHT;
	}
	
	public float getHeightBelowOrigin() {
		return mainCardBody.getHeight() + FLAP_HEIGHT;
	}
	
	public float getWidthLeftOfOrigin() {
		return foldWidth;
	}
	
	public float getWidthRightOfOrigin() {
		return mainCardBody.getWidth();
	}
	
	public Integer getCardCount() {
		return cardCount;
	}
	
}
