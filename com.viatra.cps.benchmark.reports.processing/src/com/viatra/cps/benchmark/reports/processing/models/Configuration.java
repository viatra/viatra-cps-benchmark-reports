package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

public class Configuration {
	private String xDimension;
	private String legend;
	private List<String> summarizeFunction;
	private String title;
	private Integer metricScale;
	private Integer minIteration;
	private Integer maxIteration;
	private String yLabel;
	private String xAxisScale;
	private String yAxisScale;

	public Configuration(String xDimension, String tool, List<String> summarizeFunction, String title,
			Integer metricScale, Integer minIteration, Integer maxIteration, String yLabel, String xAxisScale,
			String yAxisScale) {
		this.xDimension = xDimension;
		this.legend = tool;
		this.summarizeFunction = summarizeFunction;
		this.title = title;
		this.metricScale = metricScale;
		this.minIteration = minIteration;
		this.maxIteration = maxIteration;
		this.yLabel = yLabel;
		this.xAxisScale = xAxisScale;
		this.yAxisScale = yAxisScale;
	}

	public String getxDimension() {
		return xDimension;
	}

	public String getTool() {
		return legend;
	}

	public List<String> getSummarizeFunction() {
		return summarizeFunction;
	}

	public String getTitle() {
		return title;
	}

	public Integer getMetricScale() {
		return metricScale;
	}

	public Integer getMinIteration() {
		return minIteration;
	}

	public Integer getMaxIteration() {
		return maxIteration;
	}

	public String getyLabel() {
		return yLabel;
	}

	public String getxAxisScale() {
		return xAxisScale;
	}

	public String getyAxisScale() {
		return yAxisScale;
	}
	
	public void print() {
		System.out.println("xDimension: " + xDimension);
		System.out.println("legend: " + legend);
		System.out.println("summarizeFunction: ");
		for(String func : summarizeFunction) {
			System.out.print(func + ", ");
		}	
		System.out.println();
		System.out.println("title: " + title);
		System.out.println("metricScale: " + metricScale);
		System.out.println("minIteration: " + minIteration);
		System.out.println("maxIteration: " + maxIteration);
		System.out.println("yLabel: " + yLabel);
		System.out.println("xAxisScale: " + xAxisScale);
		System.out.println("yAxisScale: " + yAxisScale);
	}
}
