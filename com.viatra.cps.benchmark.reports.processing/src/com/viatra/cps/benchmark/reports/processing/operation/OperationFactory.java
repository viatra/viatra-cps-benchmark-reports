package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

public class OperationFactory {
	public static Operation createOperation(OperationType operationType,List<Object> filter,String attribute) {
		switch (operationType) {
		case Average:
			return new Average(attribute, filter);
		case Mean:
			return new Mean(attribute, filter);
		case Summary:
			return new Summary(attribute, filter);
		default:
			return null;
		}  
	}
}
