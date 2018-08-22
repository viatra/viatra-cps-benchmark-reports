package com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric;

import com.viatra.cps.benchmark.reports.processing.verticles.operation.OperationDescriptor;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.OperationType;

public class NumericOperationDescriptor extends OperationDescriptor {
	protected NumericOperationType operationType;

	public NumericOperationType getOperationType() {
		return operationType;
	}

	public NumericOperationDescriptor(String id, String next, OperationType type, NumericOperationType operationType) {
		super(id, next, type);
		this.operationType = operationType;
	}

}
