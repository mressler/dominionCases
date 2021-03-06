package com.ressq.pdfbox.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.ressq.pdfbox.helpers.FontInfo;
import com.ressq.pdfbox.primitives.CompositeDrawable;
import com.ressq.pdfbox.primitives.Drawable;

public class TextSubstitution extends CompositeDrawable {

	private float height;
	private float width;
	
	@FunctionalInterface
	public static interface ReplacementDrawable {
		public Drawable getReplacement(Matcher matcher, float height);
	}
	
	public TextSubstitution(
		String text, PDFont font, int fontSize,
		String replaceRegexp, ReplacementDrawable replacer) 
	{
		height = FontInfo.getHeightForFontSize(font, fontSize);
		
		Pattern p = Pattern.compile(replaceRegexp);
		Matcher m = p.matcher(text);
		
		int lastMatchIndex = 0;
		float lastBottomLeftX = 0;
		while (m.find()) {
			String displayedText = text.substring(lastMatchIndex, m.start());
			BasicText displayed = textAtOffset(displayedText, font, fontSize, lastBottomLeftX);
			lastBottomLeftX += displayed.getWidth();
			add(displayed);
			
			Drawable replaced = replacer.getReplacement(m, height);
			replaced.applyTranslation(lastBottomLeftX, 0);
			lastBottomLeftX += replaced.getWidth();
			add(replaced);
			
			lastMatchIndex = m.end();
		}
		
		BasicText remaining = textAtOffset(text.substring(lastMatchIndex), font, fontSize, lastBottomLeftX);
		add(remaining);
		
		width = remaining.getWidth() + lastBottomLeftX;
	}
	
	private BasicText textAtOffset(String text, PDFont font, int fontSize, float atOffset) {
		BasicText newText = new BasicText(text, font, fontSize);
		newText.applyTranslation(atOffset, 0);
		return newText;
	}
	
	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getWidth() {
		return width;
	}

}
