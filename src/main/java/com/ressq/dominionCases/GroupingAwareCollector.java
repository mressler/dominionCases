package com.ressq.dominionCases;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import com.ressq.dominionCases.dto.CardInfo;

public class GroupingAwareCollector {

	private Map<String, CardInfo> groupNameToInfo;
	
	public GroupingAwareCollector() {
		groupNameToInfo = new HashMap<>();
	}
	
	public void accept(CardInfo value) {
		
	}
	
	public GroupingAwareCollector merge(GroupingAwareCollector other) {
		return null;
	}
	
	public Stream<CardInfo> toStream() {
		return groupNameToInfo.values().stream();
	}
	
	public static <U> 
	Collector<CardInfo, GroupingAwareCollector, U> collectThen(Function<GroupingAwareCollector, U> collect) {
		return Collector.of(
				GroupingAwareCollector::new,
				GroupingAwareCollector::accept, 
				GroupingAwareCollector::merge, 
				collect);
	}
	
	public static
	Collector<CardInfo, GroupingAwareCollector, Stream<CardInfo>> collectToStream() {
		return Collector.of(
				GroupingAwareCollector::new,
				GroupingAwareCollector::accept, 
				GroupingAwareCollector::merge, 
				GroupingAwareCollector::toStream);
	}
}
