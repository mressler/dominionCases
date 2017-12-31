package com.ressq.dominionCases;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.shapes.ImageWithCenteredText;
import com.ressq.pdfbox.primitives.Drawable;
import com.ressq.pdfbox.text.TextSubstitution;
import com.ressq.pdfbox.text.MultiLineText.TextElementFactory;

public class DominionImageRepository implements TextElementFactory {

	private PDImageXObject coinImage;
	private PDImageXObject potionImage;
	private PDImageXObject debtImage;
	private PDImageXObject victoryImage;
	
	public DominionImageRepository(
		PDImageXObject coinImage, 
		PDImageXObject potionImage,
		PDImageXObject debtImage,
		PDImageXObject victoryImage)
	{
		this.coinImage = coinImage;
		this.potionImage = potionImage;
		this.debtImage = debtImage;
		this.victoryImage = victoryImage;
	}
	
	@Override
	public Drawable getTextElement(String text, PDFont font, int fontSize) {
		return new TextSubstitution(
			text, font, fontSize,
			"<(p|d|v)*(\\d*)>", (m, height) -> {
				String comparison = m.group(1);
				String centeredText = m.group(2);
				PDImageXObject image = coinImage;
				Color fontColor = Color.BLACK;
				if ("p".equalsIgnoreCase(comparison)) {
					image = potionImage;
				} else if ("d".equalsIgnoreCase(comparison)) {
					fontColor = Color.WHITE;
					image = debtImage;
				} else if ("v".equalsIgnoreCase(comparison)) {
					image = victoryImage;
				}
				
				float newWidth = height * image.getWidth() / image.getHeight();
				
				return new ImageWithCenteredText(centeredText, fontColor, image, newWidth, height);
			});
	}
	
	public PDImageXObject getCoinImage() {
		return coinImage;
	}
	
	public PDImageXObject getDebtImage() {
		return debtImage;
	}

	public PDImageXObject getPotionImage() {
		return potionImage;
	}

}
