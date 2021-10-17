package com.ressq.dominionCases.dto;

import java.util.Optional;
import java.util.Set;

public interface DisplayableCardInfo {

	Integer getStandardCount();
	String getErrata();
	String getName();
	Set<String> getCardSets();
	Integer getCost();
	Integer getDebt();
	Boolean getPotion();
	
	default Optional<CardInfo> getSecondaryCardInfo() {
		return Optional.empty();
	}
	
}
