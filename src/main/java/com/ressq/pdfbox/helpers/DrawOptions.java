package com.ressq.pdfbox.helpers;

import java.util.EnumSet;

public enum DrawOptions {

	OUTLINE,
	TEXT_ONLY,
	LINES_NOT_PATHS;

	public static EnumSet<DrawOptions> defaults() {
		return EnumSet.noneOf(DrawOptions.class);
	}
	
}
