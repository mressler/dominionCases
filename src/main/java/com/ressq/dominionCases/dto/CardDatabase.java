package com.ressq.dominionCases.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDatabase {

	@JsonProperty("sets")
	private Map<Integer, CardSet> setsById;
	private List<CardInfo> cards;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                          Getters and Setters
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Map<Integer, CardSet> getSetsById() {
		return setsById;
	}

	public void setSetsById(Map<Integer, CardSet> setsById) {
		this.setsById = setsById;
	}

	public List<CardInfo> getCards() {
		return cards;
	}

	public void setCards(List<CardInfo> cards) {
		this.cards = cards;
	}
}
