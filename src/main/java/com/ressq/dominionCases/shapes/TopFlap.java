package com.ressq.dominionCases.shapes;

import static com.ressq.dominionCases.shapes.CardCase.TEXT_PADDING;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.DominionImageRepository;
import com.ressq.dominionCases.dto.DisplayableCardInfo;
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
		DisplayableCardInfo cardInfo, 
		DominionImageRepository imageRepo, 
		PDFont titleFont) 
	{
		super();
		
		outline = new MultiPointObject(4);
		outline.addPoint(0, 0);
		outline.addPoint(height, height);
		outline.addPoint(width - height, height);
		outline.addPoint(width, 0);
		add(outline);
		
		Integer cardCost = cardInfo.getCost();
		Integer cardDebt = cardInfo.getDebt();
		
		float usedImageWidth = 0;
		float imageHeight = height - IMAGE_PADDING * 2;
		
		if (cardCost != null) {
			Image coin = new ImageWithCenteredText(cardCost.toString(), Color.BLACK, imageRepo.getCoinImage(), imageHeight);
			coin.applyTranslation(height,  (height - imageHeight) / 2);
			add(coin);
			
			usedImageWidth += coin.getWidth();
		}
		
		if ((cardInfo.getPotion() != null) && cardInfo.getPotion()) {
			PDImageXObject potion = imageRepo.getPotionImage();
			Image potionImg = new Image(potion, imageHeight);
			potionImg.applyTranslation(height + usedImageWidth, (height - imageHeight) / 2);
			add(potionImg);
			
			usedImageWidth += potionImg.getWidth();
		}
		
		if (cardDebt != null) {
			Image debt = new ImageWithCenteredText(cardDebt.toString(), Color.WHITE, imageRepo.getDebtImage(), imageHeight);
			debt.applyTranslation(height + usedImageWidth, (height - imageHeight) / 2);
			add(debt);
			
			usedImageWidth += debt.getWidth();
		}
		
		ScalableText title = new ScalableText(
				cardInfo.getName(), titleFont, 
				width - 2 * height - usedImageWidth - TEXT_PADDING, // Only 1x TEXT_PADDING because the last part is on a slant. Fine to creep in a bit
				height - 2 * TEXT_PADDING, height - 2 * TEXT_PADDING,
				TextAlignment.LEFT, TextAlignment.CENTER);
		title.applyTranslation(
				height + usedImageWidth + TEXT_PADDING, 
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
