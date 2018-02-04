package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Configuration {
	@JsonProperty("X_Dimension")
	private String xDimension;
	
	@JsonProperty("Legend")
	private String legend;
	
	@JsonProperty("Summarize_Function")
	private List<String> summarizeFunction;
	
	@JsonProperty("Metrics")
	private List<String> metrics;
	
	@JsonProperty("Title")
	private String title;
	
	@JsonProperty("Metric_Scale")
	private Integer metricScale;
	
	@JsonProperty("Min_Iteration")
	private Integer minIteration;
	
	@JsonProperty("Max_Iteration")
	private Integer maxIteration;
	
	@JsonProperty("Y_Label")
	private String yLabel;
	
	@JsonProperty("X_Axis_Scale")
	private String xAxisScale;
	
	@JsonProperty("Y_Axis_Scale")
	private String yAxisScale;


	public String getxDimension() {
		return xDimension;
	}


	public void setxDimension(String xDimension) {
		this.xDimension = xDimension;
	}


	public String getLegend() {
		return legend;
	}


	public void setLegend(String legend) {
		this.legend = legend;
	}


	public List<String> getSummarizeFunction() {
		return summarizeFunction;
	}


	public void setSummarizeFunction(List<String> summarizeFunction) {
		this.summarizeFunction = summarizeFunction;
	}


	public List<String> getMetrics() {
		return metrics;
	}


	public void setMetrics(List<String> metrics) {
		this.metrics = metrics;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Integer getMetricScale() {
		return metricScale;
	}


	public void setMetricScale(Integer metricScale) {
		this.metricScale = metricScale;
	}


	public Integer getMinIteration() {
		return minIteration;
	}


	public void setMinIteration(Integer minIteration) {
		this.minIteration = minIteration;
	}


	public Integer getMaxIteration() {
		return maxIteration;
	}


	public void setMaxIteration(Integer maxIteration) {
		this.maxIteration = maxIteration;
	}


	public String getyLabel() {
		return yLabel;
	}


	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}


	public String getxAxisScale() {
		return xAxisScale;
	}


	public void setxAxisScale(String xAxisScale) {
		this.xAxisScale = xAxisScale;
	}


	public String getyAxisScale() {
		return yAxisScale;
	}


	public void setyAxisScale(String yAxisScale) {
		this.yAxisScale = yAxisScale;
	}


	public Configuration() {
		summarizeFunction = new ArrayList<>();
		metrics = new ArrayList<>();
	}

}
