package com.ressq.dominionCases.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardInfo implements DisplayableCardInfo {

	private String name;
	
	private Integer setId;
	@JsonProperty("isAttack")
	private Boolean attack;
	@JsonProperty("isAction")
	private Boolean action;
	@JsonProperty("isVictory")
	private Boolean victory;
	@JsonProperty("isTreasure")
	private Boolean treasure;
	@JsonProperty("isDuration")
	private Boolean duration;
	@JsonProperty("isReaction")
	private Boolean reaction;
	@JsonProperty("isReserve")
	private Boolean reserve;
	@JsonProperty("isEvent")
	private Boolean event;
	@JsonProperty("isTraveller")
	private Boolean traveler;
	@JsonProperty("isLandmark")
	private Boolean landmark;
	@JsonProperty("isState")
	private Boolean state;
	@JsonProperty("isHex")
	private Boolean hex;
	@JsonProperty("isNight")
	private Boolean night;
	@JsonProperty("isSpirit")
	private Boolean spirit;
	@JsonProperty("isZombie")
	private Boolean zombie;
	@JsonProperty("isFate")
	private Boolean fate;
	@JsonProperty("isDoom")
	private Boolean doom;
	@JsonProperty("isHeirloom")
	private Boolean heirloom;
	@JsonProperty("isBoon")
	private Boolean boon;
	@JsonProperty("isOverpay")
	private Boolean overpay;
	@JsonProperty("isLooter")
	private Boolean looter;
	private Boolean excludeFromSupply;
	@JsonProperty("isPrize")
	private Boolean prize;
	@JsonProperty("isArtifact")
	private Boolean artifact;
	@JsonProperty("isProject")
	private Boolean project;
	
	private Integer cost;
	private Boolean potion;
	private Integer debt;
	private Integer villagers;
	private Integer coffers;
	
	private String description;
	private String errata;
	private Integer actions;
	private Integer coins;
	private Integer cards;
	private Integer buys;
	private Integer victoryPoints;
	
	private Integer standardCount;
	
	public CardInfo() {}
	public CardInfo(String name, int count) {
		setName(name);
		setStandardCount(count);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
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

	public Boolean isAttack() {
		return attack;
	}

	public void setAttack(Boolean attack) {
		this.attack = attack;
	}

	public Boolean isAction() {
		return action;
	}

	public void setAction(Boolean action) {
		this.action = action;
	}

	public Boolean isVictory() {
		return victory;
	}

	public void setVictory(Boolean victory) {
		this.victory = victory;
	}

	public Boolean isTreasure() {
		return treasure;
	}

	public void setTreasure(Boolean treasure) {
		this.treasure = treasure;
	}

	public Boolean isDuration() {
		return duration;
	}

	public void setDuration(Boolean duration) {
		this.duration = duration;
	}

	public Boolean isReaction() {
		return reaction;
	}

	public void setReaction(Boolean reaction) {
		this.reaction = reaction;
	}

	public Boolean isReserve() {
		return reserve;
	}

	public void setReserve(Boolean reserve) {
		this.reserve = reserve;
	}

	public Boolean isEvent() {
		return event;
	}

	public void setEvent(Boolean event) {
		this.event = event;
	}

	public Boolean isTraveler() {
		return traveler;
	}

	public void setTraveler(Boolean traveler) {
		this.traveler = traveler;
	}

	public Boolean isLandmark() {
		return landmark;
	}

	public void setLandmark(Boolean landmark) {
		this.landmark = landmark;
	}

	public Boolean isState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public Boolean isHex() {
		return hex;
	}

	public void setHex(Boolean hex) {
		this.hex = hex;
	}

	public Boolean isNight() {
		return night;
	}

	public void setNight(Boolean night) {
		this.night = night;
	}

	public Boolean isSpirit() {
		return spirit;
	}
	
	public void setSpirit(Boolean spirit) {
		this.spirit = spirit;
	}
	
	public Boolean isZombie() {
		return zombie;
	}
	
	public void setZombie(Boolean zombie) {
		this.zombie = zombie;
	}
	
	public Boolean isFate() {
		return fate;
	}

	public void setFate(Boolean fate) {
		this.fate = fate;
	}

	public Boolean isDoom() {
		return doom;
	}

	public void setDoom(Boolean doom) {
		this.doom = doom;
	}

	public Boolean isHeirloom() {
		return heirloom;
	}

	public void setHeirloom(Boolean heirloom) {
		this.heirloom = heirloom;
	}

	public Boolean isBoon() {
		return boon;
	}

	public void setBoon(Boolean boon) {
		this.boon = boon;
	}

	public Boolean isOverpay() {
		return overpay;
	}

	public void setOverpay(Boolean overpay) {
		this.overpay = overpay;
	}

	public Boolean isLooter() {
		return looter;
	}

	public void setLooter(Boolean looter) {
		this.looter = looter;
	}

	public Boolean isExcludeFromSupply() {
		return excludeFromSupply;
	}

	public void setExcludeFromSupply(Boolean excludeFromSupply) {
		this.excludeFromSupply = excludeFromSupply;
	}

	public Boolean isPrize() {
		return prize;
	}

	public void setPrize(Boolean prize) {
		this.prize = prize;
	}

	public Boolean isArtifact() {
		return artifact;
	}
	
	public void setArtifact(Boolean artifact) {
		this.artifact = artifact;
	}
	
	public Boolean getProject() {
		return project;
	}
	
	public void setProject(Boolean project) {
		this.project = project;
	}
	
	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Boolean getPotion() {
		return potion;
	}

	public void setPotion(Boolean potion) {
		this.potion = potion;
	}

	public Integer getDebt() {
		return debt;
	}

	public void setDebt(Integer debt) {
		this.debt = debt;
	}

	public Integer getVillagers() {
		return villagers;
	}
	
	public void setVillagers(Integer villagers) {
		this.villagers = villagers;
	}
	
	public Integer getCoffers() {
		return coffers;
	}
	
	public void setCoffers(Integer coffers) {
		this.coffers = coffers;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getErrata() {
		return errata;
	}

	public void setErrata(String errata) {
		this.errata = errata;
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
		return standardCount != null ? standardCount : 10;
	}

	public void setStandardCount(Integer standardCount) {
		this.standardCount = standardCount;
	}
	
}
