package com.ressq.pdfbox.helpers;

import java.awt.geom.Point2D;
import java.io.IOException;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.util.Matrix;

public class PDFStreamLogger extends PDFGraphicsStreamEngine {

	public PDFStreamLogger() {
		// The PDFGraphicsStreamEngine requires you pass in the page upon construction, which doesn't make a 
		// lot of sense since the stream will likely span multiple pages. Furthermore, the page is a private
		// variable in the PDFGraphicsStreamEngine only accessible by a getPage method and only ever accessed
		// in the PageDrawer subclass. So that's the only thing that cares about it - it should be isolated
		// to the subclass. Now I have to care about it and write this long comment to explain why I'm passing
		// null to the constructor, why it's OK, and why the framework creator messed up a bit here.
		super(null); 
	}

	@Override
	public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {
		System.out.println("Append Rectangle: ");
	}

	@Override
	public void drawImage(PDImage image) throws IOException {
		System.out.println("Draw Image: ");
		
		Integer width = image.getWidth();
		Integer height = image.getHeight();
		
		Matrix ctm = getGraphicsState().getCurrentTransformationMatrix();
		Float imageXScale = ctm.getScalingFactorX();
		Float imageYScale = ctm.getScalingFactorY();
		
		System.out.println("Position: " + ctm.getTranslateX() + ", " + ctm.getTranslateY() + " in user space units");
		System.out.println("Raw image size: " + width + ", " + height + " in pixels");
		System.out.println("Displayed size: " + imageXScale + ", " + imageYScale + " in user space units");
		System.out.println("Displayed size: " + (imageXScale / 72) + ", " + (imageYScale / 72) + " in inches at 72 dpi"); 
	}

	@Override
	public void clip(int windingRule) throws IOException {
		System.out.println("Clip: ");
	}

	@Override
	public void moveTo(float x, float y) throws IOException {
		System.out.println("Move To: " + x + ", " + y);
	}

	@Override
	public void lineTo(float x, float y) throws IOException {
		System.out.println("Line To: " + x + ", " + y);
	}

	@Override
	public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException {
		System.out.println("Curve To: ");
	}

	@Override
	public Point2D getCurrentPoint() throws IOException {
		// I think it's mostly OK for me to retun null here. There are some stream operator processors that
		// would make use of this information, but they all WARN and proceed if this returns null.
		return null;
	}

	@Override
	public void closePath() throws IOException {
		System.out.println("Close Path:");
	}

	@Override
	public void endPath() throws IOException {
		System.out.println("End Path: ");
	}

	@Override
	public void strokePath() throws IOException {
		System.out.println("Stroke Path: ");
	}

	@Override
	public void fillPath(int windingRule) throws IOException {
		System.out.println("Fill Path: ");
	}

	@Override
	public void fillAndStrokePath(int windingRule) throws IOException {
		System.out.println("Fill and Stroke Path: ");
	}

	@Override
	public void shadingFill(COSName shadingName) throws IOException {
		System.out.println("Shading Fill: ");
	}

}
