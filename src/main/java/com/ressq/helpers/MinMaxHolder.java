package com.ressq.helpers;

import java.util.function.Function;
import java.util.stream.Collector;

public class MinMaxHolder<T extends Comparable<T>> {

	private T min;
	private T max;
	
	public MinMaxHolder() {
		min = null;
		max = null;
	}
	
	public void accept(T value) {
		if ((min == null) || (value.compareTo(min) < 0)) {
			min = value;
		}
		
		if ((max == null) || (max.compareTo(value) < 0)) {
			max = value;
		}
	}
	
	public MinMaxHolder<T> merge(MinMaxHolder<T> other) {
		accept(other.getMin());
		accept(other.getMax());
		return this;
	}
	
	public T getMin() {
		return min;
	}
	
	public T getMax() {
		return max;
	}
	
	public static <T extends Comparable<T>, U> 
	Collector<T, MinMaxHolder<T>, U> collectThen(Function<MinMaxHolder<T>, U> collect) {
		return Collector.of(
				MinMaxHolder::new,
				MinMaxHolder::accept, 
				MinMaxHolder::merge, 
				collect);
	}
}
