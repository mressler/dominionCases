package com.ressq.dominionCases;

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
			"<((\\d*)|p|d|v)>", (m, height) -> {
				String centeredText = m.group(1);
				PDImageXObject image = coinImage;
				if ("p".equalsIgnoreCase(centeredText)) {
					centeredText = "";
					image = potionImage;
				} else if ("d".equalsIgnoreCase(centeredText)) {
					centeredText = "";
					image = debtImage;
				} else if ("v".equalsIgnoreCase(centeredText)) {
					centeredText = "";
					image = victoryImage;
				}
				
				float newWidth = height * image.getWidth() / image.getHeight();
				
				return new ImageWithCenteredText(centeredText, image, newWidth, height);
			});
	}
	
	public PDImageXObject getCoinImage() {
		return coinImage;
	}

}
