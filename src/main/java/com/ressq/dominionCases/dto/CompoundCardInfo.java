package com.ressq.dominionCases.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CompoundCardInfo implements DisplayableCardInfo {

	private CardInfo primary;
	private List<CardInfo> secondary;
	
	public CompoundCardInfo(CardInfo primary) {
		this.primary = primary;
		this.secondary = new ArrayList<CardInfo>();
	}

	public String toString() {
		return primary + secondary.stream()
			.map(Object::toString)
			.collect(Collectors.joining(" | ", " | ", "")) +
			" (" + getStandardCount() + ")";
	}
	
	public Set<String> getCardNames() {
		Set<String> cardNames = secondary.stream()
			.map(CardInfo::getName)
			.collect(Collectors.toSet());
		
		cardNames.add(primary.getName());
		
		return cardNames;
	}
	
	public void addSecondary(CardInfo someSecondary) {
		secondary.add(someSecondary);
	}
	
	@Override
	public Integer getStandardCount() {
		return primary.getStandardCount() + secondary.stream().mapToInt(CardInfo::getStandardCount).sum();
	}
	
	private <T> Optional<T> getSecondary(Function<CardInfo, T> accessor) {
		if (secondary.size() == 1) {
			return secondary.stream().findFirst().map(accessor);
		}
		
		return Optional.empty();
	}

	@Override
	public String getErrata() {
		return primary.getErrata();
	}
	
	@Override
	public Optional<String> getSecondaryErrata() {
		return getSecondary(CardInfo::getErrata);
	}

	@Override
	public String getName() {
		return primary.getName();
	}
	
	@Override
	public Optional<String> getSecondaryName() {
		return getSecondary(CardInfo::getName);
	}

	@Override
	public Integer getCost() {
		return primary.getCost();
	}
	
	@Override
	public Optional<Integer> getSecondaryCost() {
		return getSecondary(CardInfo::getCost);
	}

	@Override
	public Integer getDebt() {
		return primary.getDebt();
	}
	
	@Override
	public Optional<Integer> getSecondaryDebt() {
		return getSecondary(CardInfo::getDebt);
	}

	@Override
	public Boolean getPotion() {
		return primary.getPotion();
	}

	public boolean matches(Predicate<CardInfo> predicate) {
		return predicate.test(primary);
	}

}
