package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Benchmark {

	@JsonProperty("scenario")
	String scenario;

	@JsonProperty("cases")
	List<String> cases;

	@JsonProperty("transformation_types")
	List<String> transformationTypes;

	@JsonProperty("generator_types")
	List<String> generatorTypes;

	@JsonProperty("scales")
	List<Integer> scales;

	@JsonProperty("timeout")
	Integer timeout;

	@JsonProperty("runs")
	Integer runs;

	public Benchmark() {
		cases = new ArrayList<>();
		transformationTypes = new ArrayList<>();
		generatorTypes = new ArrayList<>();
	}

	public List<String> getToolNames() {
		List<String> toolNames = new ArrayList<>();
		transformationTypes.forEach(transformationType -> {
			generatorTypes.forEach(generatorType -> {
				toolNames.add(transformationType + "-" + generatorType);
			});
		});
		return toolNames;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public List<String> getCases() {
		return cases;
	}

	public void setCases(List<String> cases) {
		this.cases = cases;
	}

	public List<String> getTransformationTypes() {
		return transformationTypes;
	}

	public void setTransformationTypes(List<String> transformationTypes) {
		this.transformationTypes = transformationTypes;
	}

	public List<String> getGeneratorTypes() {
		return generatorTypes;
	}

	public void setGeneratorTypes(List<String> generatorTypes) {
		this.generatorTypes = generatorTypes;
	}

	public List<Integer> getScales() {
		return scales;
	}

	public void setScales(List<Integer> scales) {
		this.scales = scales;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getRuns() {
		return runs;
	}

	public void setRuns(Integer runs) {
		this.runs = runs;
	}

}