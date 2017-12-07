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

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStandardCount() {
		return standardCount;
	}

	public void setStandardCount(Integer standardCount) {
		this.standardCount = standardCount;
	}
	
}
