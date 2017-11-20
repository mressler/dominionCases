package com.ressq.dominionCases.helpers;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.ressq.dominionCases.primitives.Point;

public class ContentStream {

	private PDPageContentStream contentStream;
	
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
	
	public void lineTo(Point somePoint) {
		try {
			contentStream.lineTo(somePoint.getX(), somePoint.getY());
		} catch (IOException e) {
			throw new RuntimeException("Unhandled error lineTo Point", e);
		}
	}

	public void closeAndStroke() throws IOException {
		contentStream.closeAndStroke();
	}
	
}
