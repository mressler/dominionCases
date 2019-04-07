package com.ressq.dominionCases.dto;

import java.util.Optional;

public interface DisplayableCardInfo {

	Integer getStandardCount();
	String getErrata();
	String getName();
	Integer getCost();
	Integer getDebt();
	Boolean getPotion();
	
	default Optional<CardInfo> getSecondaryCardInfo() {
		return Optional.empty();
	}
	
}
