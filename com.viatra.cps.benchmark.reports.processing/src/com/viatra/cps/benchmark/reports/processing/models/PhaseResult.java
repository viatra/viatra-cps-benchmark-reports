package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

public class PhaseResult {
	protected String name;
	protected int sequence;
	protected List<Metric> metrics;

	public PhaseResult(String name, int sequence, List<Metric> metrics) {
		this.name = name;
		this.sequence = sequence;
		this.metrics = metrics;
	}

	public String getName() {
		return name;
	}

	public int getSequence() {
		return sequence;
	}

	public List<Metric> getMetrics() {
		return metrics;
	}
	
	public void print() {
		System.out.println("\t\tPhaseResult: \n\t\t{");
		System.out.println("\t\t\tName: " + name);
		System.out.println("\t\t\tSequence: " + sequence);
		System.out.println("\t\t\tMetrics: \n\t\t\t{[");
		for(Metric m : metrics) {
			m.print();
		}
		System.out.println("\t\t\t]}\n\t\t},");
	}
}
