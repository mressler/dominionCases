package com.ressq.dominionCases.dto;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardGrouping implements DisplayableCardInfo {

	private String name;
	private String errata;
	@JsonProperty("cards")
	private Set<String> cardNames;
	private Set<CardInfo> allCardInfo;

	public void postConstruct(Function<String, CardInfo> mapToInfo) {
		allCardInfo = cardNames.stream()
				.map(mapToInfo)
				.collect(Collectors.toSet());
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getErrata() {
		return errata;
	}
	
	public void setErrata(String errata) {
		this.errata = errata;
	}
	
	@Override
	public Integer getStandardCount() {
		if (allCardInfo == null) {
			throw new IllegalStateException("Must call postConstruct before calling getStandardCount");
		}
		
		return allCardInfo.stream()
				.map(CardInfo::getStandardCount)
				.reduce(Integer::sum)
				.orElseThrow(() -> new IllegalArgumentException(""));
	}

	@Override
	public Integer getCost() {
		return null;
	}

	@Override
	public Integer getDebt() {
		return null;
	}

	@Override
	public Boolean getPotion() {
		return null;
	}

}
