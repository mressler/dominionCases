package com.ressq.pdfbox.text;

import java.util.EnumSet;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.pdfbox.helpers.ContentStream;
import com.ressq.pdfbox.helpers.DrawOptions;
import com.ressq.pdfbox.helpers.FontInfo;
import com.ressq.pdfbox.primitives.BottomLeftAware;

public class BasicText extends BottomLeftAware {

	protected String text;
	protected PDFont font;
	protected int fontSize;
	protected boolean accountForDrawOptions = true;
	
	public BasicText(String text, PDFont font, int fontSize) {
		this.text = text;
		this.fontSize = fontSize;
		this.font = font;
	}
	
	public float getWidth() {
		return FontInfo.getWidthForFontSize(font, text, fontSize);
	}
	
	public float getHeight() {
		return FontInfo.getHeightForFontSize(font, fontSize);
	}

	public void disregardDrawOptions() {
		accountForDrawOptions = false;
	}
	
	@Override
	public void draw(ContentStream cStream, EnumSet<DrawOptions> drawOptions) {
		if (accountForDrawOptions && drawOptions.contains(DrawOptions.OUTLINE)) {
			return;
		}
		
		cStream.beginText();
		cStream.setFont(font, fontSize);
		cStream.setTextMatrix(theta, bottomLeft.getX(), bottomLeft.getY());
		cStream.showText(text);
		cStream.endText();
	}

}
