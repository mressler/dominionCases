package com.ressq.dominionCases;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) throws IOException 
	{
		PDDocument masterDoc = new PDDocument();
		
		PDPage helloPage = new PDPage();
		masterDoc.addPage(helloPage);
		
		PDFont trajan = PDType0Font.load(masterDoc, new File("Trajan Pro Regular.ttf"));
		PDFont times = PDType1Font.TIMES_ROMAN;
		
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		
		PDImageXObject coinImage = PDImageXObject.createFromFile("coin.png", masterDoc);
		
		CardCase cCase = new CardCase(250f, 400f, 10, coinImage, trajan);
		cCase.draw(cStream);
		
//		cStream.beginText();
//		cStream.setFont(font, 22);
//		cStream.newLineAtOffset(200, 400);
//		cStream.showText("Hello World!");
//		cStream.endText();
//		
//		cStream.setLineWidth(1f);
//		cStream.setStrokingColor(Color.BLACK);
//		for (int i = 10; i < 1000; i += 10) {
//			cStream.moveTo(0, i);
//			cStream.lineTo(i, i);
//			
//			cStream.beginText();
//			cStream.setFont(font, 10);
//			cStream.newLineAtOffset(i, i);
//			cStream.showText(i + "px, " + i + "px");
//			cStream.endText();
//		}
//		cStream.closeAndStroke();
		
		cStream.close();
		
		masterDoc.save("temp.pdf");
		masterDoc.close();
	}
}
