package com.ressq.pdfbox.helpers;

public class Tuple<T, U> {

	public T x;
	public U y;
	
	public Tuple() {}
	public Tuple(T x, U y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Tuple of (" + x + ", " + y + ")";
	}
	
}
