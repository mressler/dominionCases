package com.ressq.dominionCases;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.ressq.dominionCases.shapes.CardCase;
import com.ressq.pdfbox.helpers.ContentStream;

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
		
		PDPageContentStream cStream = new PDPageContentStream(masterDoc, helloPage);
		
		PDImageXObject coinImage = PDImageXObject.createFromFile("coin.png", masterDoc);
		
		CardCase cCase = new CardCase(
				10,
				3, "Village",
				"When you play this, you draw two cards, then each other player gains a Ruins. These come from the Ruins pile in the Supply, and are put into discard piles. Go in turn order starting to your left; each player takes the top Ruins, revealing the next one each time. If the Ruins pile runs out, players stop gaining them at that point. After giving out Ruins, you may play another Cultist from your hand. It can be one you just drew from playing Cultist, or one you already had in your hand. Playing a Cultist this way does not use up any extra Actions you were allowed to play due to cards like Fortress - the original Cultist uses up one Action and that is it. When you trash a Cultist of yours, you draw three cards. This happens whether or not it is your turn, and whether or not the card that causes Cultist to be trashed was yours. If you trash a Cultist while revealing cards, such as to a Knight attack, you do not draw the revealed cards that are about to be discarded.",
				//"When you play this, you pick an Action card from the Supply that costs less than it, and treat this card as if it were the card you chose. Normally this will just mean that you follow the instructions on the card you picked. So, if you play Band of Misfits and Fortress is in the Supply, you could pick that and then you would draw a card and get +2 Actions, since that is what Fortress does when you play it. Band of Misfits also gets the chosen card's cost, name, and types. If you use Band of Misfits as a card that trashes itself, such as Death Cart, you will trash the Band of Misfits (at which point it will just be a Band of Misfits card in the trash). If you use Band of Misfits as a duration card (from Seaside), Band of Misfits will stay in play until next turn, just like the duration card would. If you use Band of Misfits as a Throne Room (from Dominion), King's Court (from Prosperity), or Procession, and use that effect to play a duration card, Band of Misfits will similarly stay in play. If you use Throne Room, King's Court, or Procession to play a Band of Misfits card multiple times, you only pick what to play it as the first time; the other times it is still copying the same card. For example, if you use Procession to play Band of Misfits twice and choose Fortress the first time, you will automatically replay it as Fortress, then trash the Band of Misfits, return it to your hand (it is a Fortress when it's trashed, and Fortress has a when-trashed ability that returns it to your hand), and gain an Action card costing exactly  (  more than Band of Misfits, which has left play and so is no longer copying Fortress). If you use Band of Misfits as a card that does something during Clean-up, such as Hermit, it will do that thing during Clean-up. When you play Horn of Plenty (from Cornucopia), it counts Band of Misfits as whatever Band of Misfits was played as; for example if you play a Band of Misfits as a Fortress, and then play another Band of Misfits as a Scavenger, and then play Horn of Plenty, you will gain a card costing up to  . Band of Misfits can only be played as a card that is visible in the Supply; it cannot be played as a card after its pile runs out, and cannot be played as a non-Supply card like Mercenary; it can be played as the top card of the Ruins pile, but no other Ruins, and can only be played as Sir Martin when that is the top card of the Knights pile.",
				coinImage, trajan);
		cCase.applyTranslation(250f, 400f);
		

		cStream.setLineWidth(1f);
		cStream.setStrokingColor(Color.BLACK);
		
		ContentStream drawStream = new ContentStream(cStream);
		cCase.draw(drawStream);
		
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
