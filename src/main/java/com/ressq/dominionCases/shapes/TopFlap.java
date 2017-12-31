package com.ressq.dominionCases.shapes;

import static com.ressq.dominionCases.shapes.CardCase.*;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.DominionImageRepository;
import com.ressq.dominionCases.dto.CardInfo;
import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.primitives.Image;
import com.ressq.pdfbox.primitives.MultiPointObject;
import com.ressq.pdfbox.text.ScalableText;
import com.ressq.pdfbox.text.TextAlignment;

public class TopFlap extends CompositeDrawable {

	public static final float IMAGE_PADDING = 1;
	
	private MultiPointObject outline;
	
	public TopFlap(
		float width, float height, 
		CardInfo cardInfo, 
		DominionImageRepository imageRepo, 
		PDFont titleFont) 
	{
		super();
		
		Integer cardCost = cardInfo.getCost();
		Integer cardDebt = cardInfo.getDebt();
		
		outline = new MultiPointObject(4);
		outline.addPoint(0, 0);
		outline.addPoint(height, height);
		outline.addPoint(width - height, height);
		outline.addPoint(width, 0);
		add(outline);
		
		float coinWidth = height - IMAGE_PADDING * 2;
		float coinHeight = coinWidth;
		float debtWidth = 0;
		float potionWidth = 0;
		
		if (cardCost != null) {
			Image coin = new ImageWithCenteredText(cardCost.toString(), Color.BLACK, imageRepo.getCoinImage(), coinWidth, coinHeight);
			coin.applyTranslation(height,  (height - coinHeight) / 2);
			add(coin);
		} else {
			coinWidth = 0;
		}
		
		if ((cardInfo.getPotion() != null) && cardInfo.getPotion()) {
			PDImageXObject potion = imageRepo.getPotionImage();
			potionWidth = coinHeight * potion.getWidth() / potion.getHeight();
			Image potionImg = new Image(potion, potionWidth, coinHeight);
			potionImg.applyTranslation(height + coinWidth, (height - coinHeight) / 2);
			add(potionImg);
		}
		
		if (cardDebt != null) {
			debtWidth = coinHeight;
			Image debt = new ImageWithCenteredText(cardDebt.toString(), Color.WHITE, imageRepo.getDebtImage(), debtWidth, coinHeight);
			debt.applyTranslation(height + coinWidth + potionWidth, (height - coinHeight) / 2);
			add(debt);
		}
		
		ScalableText title = new ScalableText(
				cardInfo.getName(), titleFont, 
				width - 2 * height - coinWidth - potionWidth - debtWidth - TEXT_PADDING, // Only 1x TEXT_PADDING because the last part is on a slant. Fine to creep in a bit
				height - 2 * TEXT_PADDING, height - 2 * TEXT_PADDING,
				TextAlignment.LEFT, TextAlignment.CENTER);
		title.applyTranslation(
				height + coinWidth + potionWidth + debtWidth + TEXT_PADDING, 
				TEXT_PADDING);
		add(title);
	}

	@Override
	public float getHeight() {
		return outline.getHeight();
	}

	@Override
	public float getWidth() {
		return outline.getWidth();
	}
}
