package com.ressq.dominionCases.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfo {

	private String name;
	private Integer setId;
	private Boolean attack;
	private Boolean action;
	private Boolean victory;
	private Boolean treasure;
	private Boolean duration;
	private Boolean reaction;
	private Boolean overpay;
	private Boolean looter;
	private Boolean excludeFromSupply;
	private Boolean prize;
	
	private Integer cost;
	private Boolean hasPotion;
	
	private String description;
	private Integer actions;
	private Integer coins;
	private Integer cards;
	private Integer buys;
	private Integer victoryPoints;
	
	private Integer standardCount;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                          Getters and Setters
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public Boolean getAttack() {
		return attack;
	}

	public void setAttack(Boolean attack) {
		this.attack = attack;
	}

	public Boolean getAction() {
		return action;
	}

	public void setAction(Boolean action) {
		this.action = action;
	}

	public Boolean getVictory() {
		return victory;
	}

	public void setVictory(Boolean victory) {
		this.victory = victory;
	}

	public Boolean getTreasure() {
		return treasure;
	}

	public void setTreasure(Boolean treasure) {
		this.treasure = treasure;
	}

	public Boolean getDuration() {
		return duration;
	}

	public void setDuration(Boolean duration) {
		this.duration = duration;
	}

	public Boolean getReaction() {
		return reaction;
	}

	public void setReaction(Boolean reaction) {
		this.reaction = reaction;
	}

	public Boolean getOverpay() {
		return overpay;
	}

	public void setOverpay(Boolean overpay) {
		this.overpay = overpay;
	}

	public Boolean getLooter() {
		return looter;
	}

	public void setLooter(Boolean looter) {
		this.looter = looter;
	}

	public Boolean getExcludeFromSupply() {
		return excludeFromSupply;
	}

	public void setExcludeFromSupply(Boolean excludeFromSupply) {
		this.excludeFromSupply = excludeFromSupply;
	}

	public Boolean getPrize() {
		return prize;
	}

	public void setPrize(Boolean prize) {
		this.prize = prize;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Boolean getHasPotion() {
		return hasPotion;
	}

	public void setHasPotion(Boolean hasPotion) {
		this.hasPotion = hasPotion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getActions() {
		return actions;
	}

	public void setActions(Integer actions) {
		this.actions = actions;
	}

	public Integer getCoins() {
		return coins;
	}

	public void setCoins(Integer coins) {
		this.coins = coins;
	}

	public Integer getCards() {
		return cards;
	}

	public void setCards(Integer cards) {
		this.cards = cards;
	}

	public Integer getBuys() {
		return buys;
	}

	public void setBuys(Integer buys) {
		this.buys = buys;
	}

	public Integer getVictoryPoints() {
		return victoryPoints;
	}

	public void setVictoryPoints(Integer victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	public Integer getStandardCount() {
		return standardCount;
	}

	public void setStandardCount(Integer standardCount) {
		this.standardCount = standardCount;
	}
	
}
