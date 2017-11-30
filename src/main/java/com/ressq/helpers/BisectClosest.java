package com.ressq.helpers;

import java.util.function.Function;
import java.util.stream.Collector;

public class BisectClosest<T extends Comparable<T>> {
	
	private T closestLessThan;
	private T closestGreaterThan;
	
	private T bisectValue;
	
	public BisectClosest(T bisectValue) {
		this.bisectValue = bisectValue;
	}

	public void accept(T value) {
		if (value.compareTo(bisectValue) < 0) {
			if ((closestLessThan == null) || (closestLessThan.compareTo(value) < 0)) {
				closestLessThan = value;
			}
		} else {
			if ((closestGreaterThan == null) || (value.compareTo(closestGreaterThan) < 0)) {
				closestGreaterThan = value;
			}
		}
	}
	
	public BisectClosest<T> merge(BisectClosest<T> other) {
		accept(other.getClosestLessThan());
		accept(other.getClosestGreaterThan());
		return this;
	}

	public T getClosestLessThan() {
		return closestLessThan;
	}

	public T getClosestGreaterThan() {
		return closestGreaterThan;
	}
	
	public static <T extends Comparable<T>, U> Collector<T, BisectClosest<T>, U> bisect(
			T on, 
			Function<BisectClosest<T>, U> then) 
	{
		return Collector.of(
				() -> new BisectClosest<T>(on),
				BisectClosest::accept, 
				BisectClosest::merge, 
				then);
	}
	
}
