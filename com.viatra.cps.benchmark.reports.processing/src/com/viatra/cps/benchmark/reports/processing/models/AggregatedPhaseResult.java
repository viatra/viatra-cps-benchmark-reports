package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

public class AggregatedPhaseResult extends PhaseResult {
	String operation;

	public AggregatedPhaseResult(String name, int sequence, List<Metric> metrics, String operation) {
		super(name, sequence, metrics);
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}
	
	@Override
	public void print() {
		System.out.println("\t\tPhaseResult: \n\t\t{");
		System.out.println("\t\t\tName: " + name);
		System.out.println("\t\t\tSequence: " + sequence);
		System.out.println("\t\t\tMetrics: \n\t\t\t{[");
		for(Metric m : metrics) {
			m.print();
		}
		System.out.println("\t\t\t]},");
		System.out.println("\t\t\tOperation: " + operation);
		System.out.println("\t\t}");
	}
}
