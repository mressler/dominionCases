package com.ressq.dominionCases.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDatabase {

	@JsonProperty("sets")
	private Map<Integer, CardSet> setsById;
	private List<CardInfo> cards;
	private List<CardGrouping> groupAs;
	private Map<String, CompoundCardInfo> compoundCards = new HashMap<>();
	
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

	public Collection<CompoundCardInfo> getCompoundCards() {
		return compoundCards.values();
	}
	
	public void postConstruct() {
		cards.stream().forEach(c -> c.postConstruct(setsById));

		groupAs.stream().forEach(ga -> ga.postConstruct(getMapToInfo()));
		
		cards.stream()
			.filter(ci -> ci.getGroupWith().isPresent())
			.forEach(this::mergeCardIntoCompoundInfo);
	}
	
	/**
	 * Merge all of the grouping cards and compound cards into the regular card info DB. Generate the resulting
	 * stream from all of the remaining cards.
	 * 
	 * @param includeGroupAs
	 * @param predicate
	 * @return
	 */
	public Stream<? extends DisplayableCardInfo> getCardsForDisplay(Boolean includeGroupAs, Predicate<CardInfo> predicate) {
		Set<String> cardsInGroupedAs = groupAs.stream()
			.map(CardGrouping::getCardNames)
			.flatMap(Set::stream)
			.collect(Collectors.toSet());
		
		Set<String> cardsInCompound = compoundCards.values().stream()
			.map(CompoundCardInfo::getCardNames)
			.flatMap(Set::stream)
			.collect(Collectors.toSet());
		
		Set<String> allCardsUsed = new HashSet<>();
		allCardsUsed.addAll(cardsInGroupedAs);
		allCardsUsed.addAll(cardsInCompound);
		
		Stream<? extends DisplayableCardInfo> compoundCardStream = compoundCards.values().stream()
			.filter(cci -> cci.matches(predicate));
		
		Stream<? extends DisplayableCardInfo> remainingCards = cards.stream()
			.filter(predicate)
			.filter(ci -> !allCardsUsed.contains(ci.getName()));
		
		Stream<? extends DisplayableCardInfo> returnableStream = Stream.concat(remainingCards, compoundCardStream);
		if (includeGroupAs) {
			returnableStream = Stream.concat(returnableStream, groupAs.stream().filter(cg -> cg.matches(predicate)));
		}
		
		return returnableStream;
	}
	
	private void mergeCardIntoCompoundInfo(CardInfo info) {
		String groupOwnerName = info.getGroupWith().orElseThrow(IllegalArgumentException::new);
		CompoundCardInfo compoundCard = Optional.ofNullable(compoundCards.get(groupOwnerName))
			.orElseGet(() -> {
				return new CompoundCardInfo(getCardByName(groupOwnerName));
			});
		compoundCard.addSecondary(info);
		
		compoundCards.put(groupOwnerName, compoundCard);
	}

	private Map<String, CardInfo> mapToInfo;
	private synchronized Map<String, CardInfo> getMapToInfo() {
		if (mapToInfo == null) {
			mapToInfo = cards.stream().collect(Collectors.toMap(CardInfo::getName, Function.identity()));
		}
		return mapToInfo;
	}
	
	public CardInfo getCardByName(String name) {
		return Optional.ofNullable(getMapToInfo().get(name))
				.orElseThrow(() -> new IllegalArgumentException("No card named " + name));
	}
}
