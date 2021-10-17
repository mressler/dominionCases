package com.ressq.dominionCases.dto;

import java.util.Optional;

public class CardSet {

	private String name;
	private String imageName;
	private String releaseDate;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                          Getters and Setters
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getImageName() {
		return Optional.ofNullable(imageName).orElse(name);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
}
