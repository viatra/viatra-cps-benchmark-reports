package com.viatra.cps.benchmark.reports.processing.models;

public class Metric {
	private String name;
	private Integer value;

	public Metric(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}
	
	public void print() {
		System.out.println("\t\t\tMetric: \n\t\t\t{");
		System.out.println("\t\t\t\tMetricName: " + name);
		System.out.println("\t\t\t\tMetricValue: " + value);
		System.out.println("\t\t\t}");
	}
}
