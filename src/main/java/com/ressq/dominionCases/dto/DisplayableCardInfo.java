package com.ressq.dominionCases.dto;

import java.util.Optional;

public interface DisplayableCardInfo {

	Integer getStandardCount();
	String getErrata();
	String getName();
	Integer getCost();
	Integer getDebt();
	Boolean getPotion();
	
	default Optional<String> getSecondaryErrata() {
		return Optional.empty();
	}
	
	default Optional<String> getSecondaryName() {
		return Optional.empty();
	}
	
	default Optional<Integer> getSecondaryCost() {
		return Optional.empty();
	}
	
	default Optional<Integer> getSecondaryDebt() {
		return Optional.empty();
	}
	
}
