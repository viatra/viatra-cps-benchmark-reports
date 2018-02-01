package com.viatra.cps.benchmark.reports.processing.models;

public class Metric {
	private String name;
	private Double value;

	public Metric(String name, Double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}

	public void print() {
		System.out.println("\t\t\tMetric: \n\t\t\t{");
		System.out.println("\t\t\t\tMetricName: " + name);
		System.out.println("\t\t\t\tMetricValue: " + value);
		System.out.println("\t\t\t}");
	}
}
