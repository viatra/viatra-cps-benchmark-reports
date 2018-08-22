package com.viatra.cps.benchmark.reports.processing.verticles.operation;

public abstract class OperationDescriptor {
	protected String Id;
	protected String next;
	protected OperationType type;

	public String getId() {
		return Id;
	}

	public String getNext() {
		return next;
	}

	public OperationType getType() {
		return type;
	}

	public OperationDescriptor(String id, String next, OperationType type) {
		this.Id = id;
		this.next = next;
		this.type = type;
	}

}