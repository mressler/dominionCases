package com.ressq.pdfbox.helpers;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.Closeable;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import com.ressq.pdfbox.primitives.Point;

public class ContentStream implements Closeable {

	private PDPageContentStream contentStream;
	
	public ContentStream(PDDocument masterDoc, PDPage helloPage) {
		try {
			this.contentStream = new PDPageContentStream(masterDoc, helloPage);
		} catch (IOException e) {
			throw new RuntimeException("Unhandled Error on content stream creation", e);
		}
	}
	
	public ContentStream(PDPageContentStream contentStream) {
		this.contentStream = contentStream;
	}
	
	public void moveTo(Point somePoint) {
		try {
			contentStream.moveTo(somePoint.getX(), somePoint.getY());
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error moving to Point", e);
		}
	}
	
	public void stroke() {
		try {
			contentStream.stroke();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error in stroke.", e);
		}
	}
	
	public void lineTo(Point somePoint) {
		try {
			contentStream.lineTo(somePoint.getX(), somePoint.getY());
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error lineTo Point", e);
		}
	}
	
	public void drawImage(PDImageXObject image, double theta, float x, float y, float width, float height) {
		try {
			contentStream.saveGraphicsState();
			Matrix rotate = new Matrix();
			rotate.rotate(theta);
			contentStream.transform(rotate);
			
			Point bottomLeft = new Point(x, y);
			bottomLeft.applyRotation(-1 * theta);
			
			contentStream.drawImage(image, bottomLeft.getX(), bottomLeft.getY(), width, height);
			
			contentStream.restoreGraphicsState();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error drawing image", e);
		}
	}
	
	public void drawImage(PDImageXObject image, AffineTransform imageTransform) {
		try {
			contentStream.drawImage(image, new Matrix(imageTransform));
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error drawing image", e);
		}
	}

	public void closeAndStroke() {
		try {
			contentStream.closeAndStroke();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error closing line", e);
		}
	}

	public void closeAndFillAndStroke() {
		try {
			contentStream.closeAndFillAndStroke();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error closing line", e);
		}
	}

	public void beginText() {
		try {
			contentStream.beginText();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error beginning text", e);
		}
	}

	public void setFont(PDFont someFont, int fontSize) {
		try {
			contentStream.setFont(someFont, fontSize);
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error setting font", e);
		}
	}

	public void newLineAtOffset(float tx, float ty) {
		try {
			contentStream.newLineAtOffset(tx, ty);
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error starting a new line", e);
		}
	}

	public void showText(String text) {
		try {
			contentStream.showText(text);
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error showing text", e);
		}
	}

	public void endText() {
		try {
			contentStream.endText();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error ending text", e);
		}
	}

	public void setTextMatrix(double theta, float tx, float ty) {
		try {
			contentStream.setTextMatrix(Matrix.getRotateInstance(theta, tx, ty));
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error setting text matrix", e);
		}
	}
	
	public void saveGraphicsState() {
		try {
			contentStream.saveGraphicsState();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error saving graphics state", e);
		}
	}
	
	public void restoreGraphicsState() {
		try {
			contentStream.restoreGraphicsState();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error restoring graphics state", e);
		}
	}

	public void setFontColor(Color fontColor) {
		try {
			contentStream.setNonStrokingColor(fontColor);
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error setting font color", e);
		}
	}

	@Override
	public void close() {
		try {
			contentStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error on close", e);
		}
	}
	
}
