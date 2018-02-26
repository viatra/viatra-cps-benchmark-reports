package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

public class OperationFactory {
	public static <T> Operation<T> createOperation(OperationType operationType,List<T> filter,String attribute) {
		switch (operationType) {
		case Average:
			return new Average<T>(attribute, filter);
		case Mean:
			return new Mean<T>(attribute, filter);
		case Summary:
			return new Summary<T>(attribute, filter);
		default:
			return null;
		}
	}
}
