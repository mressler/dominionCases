package com.ressq.dominionCases.dto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDatabase {

	@JsonProperty("sets")
	private Map<Integer, CardSet> setsById;
	private List<CardInfo> cards;
	private List<CardGrouping> groupAs;
	
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
	
//	public void setGroupAs(Map<String,List<String>> groupAs) {
//		this.groupAs = groupAs.entrySet().stream()
//			.flatMap(groupAndList -> groupAndList.getValue().stream()
//					.map(cardName -> new Tuple<>(cardName, groupAndList.getKey())))
//			.map(t -> new Tuple<>(t.x, new CardInfo(t.y, 1)))
//			.collect(Collectors.toMap(t -> t.x, t -> t.y));
//	}
	
	public List<CardGrouping> getGroupAs() {
		return groupAs;
	}

	public void setGroupAs(List<CardGrouping> groupAs) {
		this.groupAs = groupAs;
	}

	private Map<String, CardInfo> mapToInfo;
	public CardInfo getCardByName(String name) {
		if (mapToInfo == null) {
			mapToInfo = cards.stream().collect(Collectors.toMap(CardInfo::getName, Function.identity()));
		}
		return Optional.ofNullable(mapToInfo.get(name))
				.orElseThrow(() -> new IllegalArgumentException("No card named " + name));
	}
}
